package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mdd.admin.service.ISportsCategoryService;
import com.mdd.admin.validate.course.SportsCategoryCreateValidate;
import com.mdd.admin.validate.course.SportsCategoryUpdateValidate;
import com.mdd.common.entity.course.SportsCategory;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.course.SportsCategoryMapper;
import com.mdd.common.util.TimeUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SportsCategoryServiceImpl implements ISportsCategoryService {
    /** API 兼容哨兵：入参 0 表示根级，入库统一写 NULL */
    private static final int ROOT_PARENT_ID = 0;
    /** 根级在数据库中的表示 */
    private static final Integer ROOT = null;
    private static final int STATUS_HIDDEN = 0;
    private static final int STATUS_VISIBLE = 1;
    private static final int DEFAULT_SORT = 0;
    private static final int MAX_DEPTH = 20;
    private static final int NAME_MAX_LENGTH = 20;
    private static final String NAME_LENGTH_MESSAGE = "项目名称在1-20个字符之间";
    /** 首尾空白：ASCII 空白 + 全角空格(U+3000)，裸 trim() 不处理全角空格 */
    private static final Pattern EDGE_BLANK_PATTERN = Pattern.compile("^[\\s\\u3000]+|[\\s\\u3000]+$");

    @Resource
    SportsCategoryMapper sportsCategoryMapper;

    @Override
    public List<SportsCategory> listAll(boolean includeHidden, boolean asTree) {
        QueryWrapper<SportsCategory> queryWrapper = new QueryWrapper<SportsCategory>();
        if (!includeHidden) {
            queryWrapper.eq("is_show", STATUS_VISIBLE);
        }
        queryWrapper.orderByDesc("sort").orderByDesc("id");
        List<SportsCategory> categories = this.sportsCategoryMapper.selectList(queryWrapper);
        return asTree ? this.buildTree(categories) : categories;
    }

    @Override
    public void add(SportsCategoryCreateValidate createValidate) {
        Integer parentId = this.normalizeParentId(createValidate.getParentId());
        Integer isShow = createValidate.getIsShow() == null ? STATUS_VISIBLE : createValidate.getIsShow();
        String name = this.validateInput(createValidate.getName(), parentId, isShow, null);

        SportsCategory entity = new SportsCategory();
        entity.setParentId(parentId);
        entity.setName(name);
        entity.setIcon(createValidate.getIcon());
        entity.setSort(createValidate.getSort() == null ? DEFAULT_SORT : createValidate.getSort());
        entity.setIsShow(isShow);
        int now = TimeUtils.timestamp().intValue();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        this.sportsCategoryMapper.insert(entity);
    }

    @Override
    public void edit(SportsCategoryUpdateValidate updateValidate) {
        SportsCategory entity = this.sportsCategoryMapper.selectById(updateValidate.getId());
        if (entity == null) {
            throw new OperateException("分类不存在");
        }

        Integer parentId = updateValidate.getParentId() == null
            ? this.normalizeParentId(entity.getParentId())
            : this.normalizeParentId(updateValidate.getParentId());
        Integer isShow = updateValidate.getIsShow() == null
            ? this.effectiveStatus(entity.getIsShow())
            : updateValidate.getIsShow();
        String rawName = updateValidate.getName() == null ? entity.getName() : updateValidate.getName();
        String name = this.validateInput(rawName, parentId, isShow, updateValidate.getId());

        int oldIsShow = this.effectiveStatus(entity.getIsShow());
        if (isShow == STATUS_HIDDEN && oldIsShow != STATUS_HIDDEN) {
            this.ensureNoVisibleChildren(updateValidate.getId());
        }

        int now = TimeUtils.timestamp().intValue();
        LambdaUpdateWrapper<SportsCategory> updateWrapper = new LambdaUpdateWrapper<SportsCategory>();
        updateWrapper.eq(SportsCategory::getId, updateValidate.getId())
            .set(SportsCategory::getUpdateTime, now);
        if (updateValidate.getName() != null) {
            updateWrapper.set(SportsCategory::getName, name);
        }
        if (updateValidate.getParentId() != null) {
            if (parentId == null) {
                updateWrapper.setSql("parent_id = NULL");
            } else {
                updateWrapper.set(SportsCategory::getParentId, parentId);
            }
        }
        if (updateValidate.getIcon() != null) {
            updateWrapper.set(SportsCategory::getIcon, updateValidate.getIcon());
        }
        if (updateValidate.getSort() != null) {
            updateWrapper.set(SportsCategory::getSort, updateValidate.getSort());
        }
        if (updateValidate.getIsShow() != null) {
            updateWrapper.set(SportsCategory::getIsShow, updateValidate.getIsShow());
        }
        this.sportsCategoryMapper.update(null, updateWrapper);
    }

    /**
     * add/edit 共用的校验入口：名称长度、父级、可见性、同级重名。
     *
     * <p>名称按“去除首尾空白后”的长度校验：1–20 个字符，
     * 全角空格也视为空白，避免只改长度注解时两边规则漂移。</p>
     *
     * @return 去除首尾空白后的名称
     */
    private String validateInput(String name, Integer parentId, Integer isShow, Integer selfId) {
        String trimmedName = name == null ? "" : EDGE_BLANK_PATTERN.matcher(name).replaceAll("");
        if (trimmedName.isEmpty()) {
            throw new OperateException("项目名称不能为空");
        }
        if (trimmedName.length() > NAME_MAX_LENGTH) {
            throw new OperateException(NAME_LENGTH_MESSAGE);
        }
        SportsCategory parent = this.ensureParentValid(parentId, selfId);
        this.ensureParentVisible(parent, isShow);
        this.ensureNameUnique(trimmedName, parentId, selfId);
        return trimmedName;
    }

    /**
     * 校验父级存在、非自身、无环、层级不超限，并返回父级实体供后续复用（父级只查一次）。
     */
    private SportsCategory ensureParentValid(Integer parentId, Integer selfId) {
        if (parentId == null) {
            return null;
        }
        if (parentId.equals(selfId)) {
            throw new OperateException("不能把自己设为父级");
        }
        SportsCategory parent = this.sportsCategoryMapper.selectById(parentId);
        if (parent == null) {
            throw new OperateException("父级分类不存在");
        }

        Set<Integer> visited = new HashSet<Integer>();
        visited.add(parentId);
        Integer cursorId = this.normalizeParentId(parent.getParentId());
        int depth = 1;
        while (cursorId != null) {
            if (cursorId.equals(selfId)) {
                throw new OperateException("不能形成循环层级");
            }
            if (!visited.add(cursorId)) {
                throw new OperateException("不能形成循环层级");
            }
            if (depth >= MAX_DEPTH) {
                throw new OperateException("分类层级过深");
            }
            SportsCategory cursor = this.sportsCategoryMapper.selectById(cursorId);
            if (cursor == null) {
                break;
            }
            cursorId = this.normalizeParentId(cursor.getParentId());
            depth++;
        }
        return parent;
    }

    private void ensureParentVisible(SportsCategory parent, Integer isShow) {
        if (parent == null || isShow == null || isShow == STATUS_HIDDEN) {
            return;
        }
        if (parent.getIsShow() != null && parent.getIsShow() == STATUS_HIDDEN) {
            throw new OperateException("父级已隐藏，请先显示父级");
        }
    }

    private void ensureNameUnique(String name, Integer parentId, Integer selfId) {
        QueryWrapper<SportsCategory> queryWrapper = new QueryWrapper<SportsCategory>();
        queryWrapper.eq("name", name);
        if (parentId == null) {
            queryWrapper.isNull("parent_id");
        } else {
            queryWrapper.eq("parent_id", parentId);
        }
        if (selfId != null) {
            queryWrapper.ne("id", selfId);
        }
        Long count = this.sportsCategoryMapper.selectCount(queryWrapper);
        if (count != null && count > 0) {
            throw new OperateException("同级下已存在同名分类");
        }
    }

    private void ensureNoVisibleChildren(Integer id) {
        Long visibleChildren = this.sportsCategoryMapper.selectCount(
            new QueryWrapper<SportsCategory>().eq("parent_id", id).eq("is_show", STATUS_VISIBLE)
        );
        if (visibleChildren != null && visibleChildren > 0) {
            throw new OperateException("请先隐藏子分类");
        }
    }

    private Integer normalizeParentId(Integer parentId) {
        if (parentId == null || parentId == ROOT_PARENT_ID) {
            return ROOT;
        }
        return parentId;
    }

    private int effectiveStatus(Integer status) {
        return status == null ? STATUS_VISIBLE : status;
    }

    private List<SportsCategory> buildTree(List<SportsCategory> categories) {
        Map<Integer, SportsCategory> nodes = new LinkedHashMap<Integer, SportsCategory>();
        for (SportsCategory category : categories) {
            category.setChildren(null);
            nodes.put(category.getId(), category);
        }

        List<SportsCategory> roots = new ArrayList<SportsCategory>();
        for (SportsCategory category : categories) {
            Integer parentId = this.normalizeParentId(category.getParentId());
            SportsCategory parent = parentId == null ? null : nodes.get(parentId);
            if (parent == null || parent == category || this.isCyclic(category, nodes)) {
                roots.add(category);
            } else {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<SportsCategory>());
                }
                parent.getChildren().add(category);
            }
        }
        this.sortNodes(roots);
        return roots;
    }

    /** 存量数据若成环，把环节点降为根，避免递归建树时死循环。 */
    private boolean isCyclic(SportsCategory category, Map<Integer, SportsCategory> nodes) {
        Set<Integer> visited = new HashSet<Integer>();
        SportsCategory cursor = category;
        int depth = 0;
        while (cursor != null && depth <= MAX_DEPTH) {
            if (!visited.add(cursor.getId())) {
                return true;
            }
            Integer parentId = this.normalizeParentId(cursor.getParentId());
            if (parentId == null) {
                return false;
            }
            cursor = nodes.get(parentId);
            depth++;
        }
        return depth > MAX_DEPTH;
    }

    private void sortNodes(List<SportsCategory> nodes) {
        nodes.sort(new Comparator<SportsCategory>() {
            @Override
            public int compare(SportsCategory left, SportsCategory right) {
                int leftSort = left.getSort() == null ? DEFAULT_SORT : left.getSort();
                int rightSort = right.getSort() == null ? DEFAULT_SORT : right.getSort();
                if (leftSort != rightSort) {
                    return Integer.compare(rightSort, leftSort);
                }
                return Integer.compare(left.getId(), right.getId());
            }
        });
        for (SportsCategory node : nodes) {
            if (node.getChildren() != null) {
                this.sortNodes(node.getChildren());
            }
        }
    }
}
