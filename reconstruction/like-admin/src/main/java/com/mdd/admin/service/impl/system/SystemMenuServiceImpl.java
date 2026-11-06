package com.mdd.admin.service.impl.system;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.system.ISystemMenuService;
import com.mdd.admin.service.system.ISystemRoleMenuService;
import com.mdd.admin.validate.system.SystemMenuCreateValidate;
import com.mdd.admin.validate.system.SystemMenuUpdateValidate;
import com.mdd.admin.vo.system.SystemAuthMenuVo;
import com.mdd.admin.vo.system.SystemMenuListedVo;
import com.mdd.common.entity.system.SystemMenu;
import com.mdd.common.mapper.system.SystemMenuMapper;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.TimeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SystemMenuServiceImpl implements ISystemMenuService {
   @Resource
   SystemMenuMapper systemAuthMenuMapper;
   @Resource
   ISystemRoleMenuService iSystemRoleMenuService;

   public SystemMenuServiceImpl() {
   }

   @Override
   public JSONArray selectMenuByRoleId(List<Integer> roleIds) {
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      List<Integer> menuIds = this.iSystemRoleMenuService.selectMenuIdsByRoleId(roleIds);
      QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<SystemMenu>();
      queryWrapper.in("type", Arrays.asList("M", "C"));
      queryWrapper.orderByDesc("sort");
      queryWrapper.orderByAsc("id");
      if (!adminId.equals(1)) {
         if (menuIds.size() <= 0) {
            menuIds.add(0);
         }

         queryWrapper.in("id", menuIds);
      }

      List<SystemMenu> systemAuthMenus = this.systemAuthMenuMapper.selectList(queryWrapper);
      List<SystemAuthMenuVo> lists = new ArrayList<>();

      for (SystemMenu systemAuthMenu : systemAuthMenus) {
         SystemAuthMenuVo vo = new SystemAuthMenuVo();
         BeanUtils.copyProperties(systemAuthMenu, vo);
         vo.setUpdateTime(TimeUtils.timestampToDate(systemAuthMenu.getUpdateTime()));
         vo.setCreateTime(TimeUtils.timestampToDate(systemAuthMenu.getCreateTime()));
         lists.add(vo);
      }

      JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(lists, new Feature[0]), new com.alibaba.fastjson2.JSONReader.Feature[0]);
      return ListUtils.listToTree(jsonArray, "id", "pid", "children");
   }

   @Override
   public JSONObject list() {
      return new JSONObject() {
         {
            this.put("lists", SystemMenuServiceImpl.this.all());
         }
      };
   }

   @Override
   public JSONArray systemMenuLists() {
      QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<SystemMenu>();
      queryWrapper.orderByDesc("sort");
      queryWrapper.orderByAsc("id");
      List<SystemMenu> systemAuthMenus = this.systemAuthMenuMapper.selectList(queryWrapper);
      List<SystemMenuListedVo> lists = new ArrayList<>();

      for (SystemMenu systemAuthMenu : systemAuthMenus) {
         SystemMenuListedVo vo = new SystemMenuListedVo();
         BeanUtils.copyProperties(systemAuthMenu, vo);
         vo.setCreateTime(TimeUtils.timestampToDate(systemAuthMenu.getCreateTime()));
         vo.setUpdateTime(TimeUtils.timestampToDate(systemAuthMenu.getUpdateTime()));
         lists.add(vo);
      }

      JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(lists, new Feature[0]), new com.alibaba.fastjson2.JSONReader.Feature[0]);
      return ListUtils.listToTree(jsonArray, "id", "pid", "children");
   }

   @Override
   public JSONArray all() {
      QueryWrapper<SystemMenu> queryWrapper = new QueryWrapper<SystemMenu>();
      queryWrapper.orderByDesc("sort");
      queryWrapper.orderByAsc("id");
      List<SystemMenu> systemAuthMenus = this.systemAuthMenuMapper.selectList(queryWrapper);
      List<SystemAuthMenuVo> lists = new ArrayList<>();

      for (SystemMenu systemAuthMenu : systemAuthMenus) {
         SystemAuthMenuVo vo = new SystemAuthMenuVo();
         BeanUtils.copyProperties(systemAuthMenu, vo);
         vo.setCreateTime(TimeUtils.timestampToDate(systemAuthMenu.getCreateTime()));
         vo.setUpdateTime(TimeUtils.timestampToDate(systemAuthMenu.getUpdateTime()));
         lists.add(vo);
      }

      JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(lists, new Feature[0]), new com.alibaba.fastjson2.JSONReader.Feature[0]);
      return ListUtils.listToTree(jsonArray, "id", "pid", "children");
   }

   @Override
   public SystemAuthMenuVo detail(Integer id) {
      SystemMenu systemAuthMenu = (SystemMenu)this.systemAuthMenuMapper.selectOne(new QueryWrapper<SystemMenu>().eq("id", id));
      Assert.notNull(systemAuthMenu, "菜单已不存在!", new Object[0]);
      SystemAuthMenuVo vo = new SystemAuthMenuVo();
      BeanUtils.copyProperties(systemAuthMenu, vo);
      vo.setCreateTime(TimeUtils.timestampToDate(systemAuthMenu.getCreateTime()));
      vo.setUpdateTime(TimeUtils.timestampToDate(systemAuthMenu.getUpdateTime()));
      return vo;
   }

   @Override
   public void add(SystemMenuCreateValidate createValidate) {
      SystemMenu model = new SystemMenu();
      model.setPid(createValidate.getPid());
      model.setType(createValidate.getType());
      model.setName(createValidate.getName());
      model.setIcon(createValidate.getIcon());
      model.setSort(createValidate.getSort());
      model.setPerms(createValidate.getPerms());
      model.setPaths(createValidate.getPaths());
      model.setComponent(createValidate.getComponent());
      model.setSelected(createValidate.getSelected());
      model.setParams(createValidate.getParams());
      model.setIsCache(createValidate.getIsCache());
      model.setIsShow(createValidate.getIsShow());
      model.setIsDisable(createValidate.getIsDisable());
      model.setCreateTime(System.currentTimeMillis() / 1000L);
      model.setUpdateTime(System.currentTimeMillis() / 1000L);
      this.systemAuthMenuMapper.insert(model);
   }

   @Override
   public void edit(SystemMenuUpdateValidate updateValidate) {
      SystemMenu model = (SystemMenu)this.systemAuthMenuMapper.selectOne(new QueryWrapper<SystemMenu>().eq("id", updateValidate.getId()));
      Assert.notNull(model, "菜单已不存在!", new Object[0]);
      model.setType(updateValidate.getType());
      model.setName(updateValidate.getName());
      model.setIcon(updateValidate.getIcon());
      model.setSort(updateValidate.getSort());
      model.setPaths(updateValidate.getPaths());
      model.setPerms(updateValidate.getPerms());
      model.setComponent(updateValidate.getComponent());
      model.setPid(updateValidate.getPid());
      model.setSelected(updateValidate.getSelected());
      model.setParams(updateValidate.getParams());
      model.setIsCache(updateValidate.getIsCache());
      model.setIsShow(updateValidate.getIsShow());
      model.setIsDisable(updateValidate.getIsDisable());
      model.setUpdateTime(System.currentTimeMillis() / 1000L);
      this.systemAuthMenuMapper.updateById(model);
   }

   @Override
   public void del(Integer id) {
      SystemMenu model = (SystemMenu)this.systemAuthMenuMapper
         .selectOne(new QueryWrapper<SystemMenu>().select(new String[]{"id,pid,name"}).eq("id", id).last("limit 1"));
      Assert.notNull(model, "菜单已不存在!", new Object[0]);
      Assert.isNull(
         this.systemAuthMenuMapper.selectOne(new QueryWrapper<SystemMenu>().eq("pid", id).last("limit 1")), "请先删除子菜单再操作!", new Object[0]
      );
      this.systemAuthMenuMapper.deleteById(id);
      this.iSystemRoleMenuService.batchDeleteByMenuId(id);
   }
}
