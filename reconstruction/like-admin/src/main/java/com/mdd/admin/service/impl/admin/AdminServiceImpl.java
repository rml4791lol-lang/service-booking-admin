/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.dev33.satoken.stp.StpUtil
 *  com.alibaba.excel.EasyExcel
 *  com.alibaba.excel.support.ExcelTypeEnum
 *  com.alibaba.excel.write.builder.ExcelWriterBuilder
 *  com.alibaba.fastjson2.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.github.yulichang.interfaces.MPJBaseJoin
 *  com.github.yulichang.query.MPJQueryWrapper
 *  com.mdd.admin.service.admin.IAdminDeptService
 *  com.mdd.admin.service.admin.IAdminJobsService
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.admin.service.admin.IAdminService
 *  com.mdd.admin.service.admin.IAuthService
 *  com.mdd.admin.service.impl.admin.AdminServiceImpl
 *  com.mdd.admin.service.system.ISystemMenuService
 *  com.mdd.admin.service.system.ISystemRoleMenuService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemAdminCreateValidate
 *  com.mdd.admin.validate.system.SystemAdminSearchValidate
 *  com.mdd.admin.validate.system.SystemAdminUpInfoValidate
 *  com.mdd.admin.validate.system.SystemAdminUpdateValidate
 *  com.mdd.admin.validate.system.SystemEditSelfValidate
 *  com.mdd.admin.vo.auth.AdminMySelfVo
 *  com.mdd.admin.vo.auth.AuthMySelfVo
 *  com.mdd.admin.vo.system.SystemAuthAdminDetailVo
 *  com.mdd.admin.vo.system.SystemAuthAdminInformVo
 *  com.mdd.admin.vo.system.SystemAuthAdminListedExportVo
 *  com.mdd.admin.vo.system.SystemAuthAdminListedVo
 *  com.mdd.admin.vo.system.SystemAuthAdminSelvesVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.system.SystemMenu
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.admin.DeptMapper
 *  com.mdd.common.mapper.admin.JobsMapper
 *  com.mdd.common.mapper.system.SystemMenuMapper
 *  com.mdd.common.mapper.system.SystemRoleMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl.admin;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.admin.service.admin.IAdminDeptService;
import com.mdd.admin.service.admin.IAdminJobsService;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.admin.service.admin.IAdminService;
import com.mdd.admin.service.admin.IAuthService;
import com.mdd.admin.service.system.ISystemMenuService;
import com.mdd.admin.service.system.ISystemRoleMenuService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemAdminCreateValidate;
import com.mdd.admin.validate.system.SystemAdminSearchValidate;
import com.mdd.admin.validate.system.SystemAdminUpInfoValidate;
import com.mdd.admin.validate.system.SystemAdminUpdateValidate;
import com.mdd.admin.validate.system.SystemEditSelfValidate;
import com.mdd.admin.vo.auth.AdminMySelfVo;
import com.mdd.admin.vo.auth.AuthMySelfVo;
import com.mdd.admin.vo.system.SystemAuthAdminDetailVo;
import com.mdd.admin.vo.system.SystemAuthAdminInformVo;
import com.mdd.admin.vo.system.SystemAuthAdminListedExportVo;
import com.mdd.admin.vo.system.SystemAuthAdminListedVo;
import com.mdd.admin.vo.system.SystemAuthAdminSelvesVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.system.SystemMenu;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.admin.DeptMapper;
import com.mdd.common.mapper.admin.JobsMapper;
import com.mdd.common.mapper.system.SystemMenuMapper;
import com.mdd.common.mapper.system.SystemRoleMapper;
import com.mdd.common.util.PasswordUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mdd.common.entity.system.SystemRole;
import com.mdd.common.entity.admin.Jobs;
import com.mdd.common.entity.admin.Dept;

@Service
public class AdminServiceImpl
implements IAdminService {
    @Resource
    AdminMapper systemAuthAdminMapper;
    @Resource
    SystemMenuMapper systemAuthMenuMapper;
    @Resource
    SystemRoleMapper systemAuthRoleMapper;
    @Resource
    ISystemRoleMenuService iSystemRoleMenuService;
    @Resource
    IAdminRoleService iAdminRoleService;
    @Resource
    IAdminDeptService iAdminDeptService;
    @Resource
    IAdminJobsService iAdminJobsService;
    @Resource
    JobsMapper jobsMapper;
    @Resource
    DeptMapper deptMapper;
    @Resource
    IAuthService iAuthService;
    @Resource
    ISystemMenuService systemMenuService;

    public PageResult<SystemAuthAdminListedVo> list(PageValidate pageValidate, SystemAdminSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        MPJQueryWrapper<Admin> mpjQueryWrapper = new MPJQueryWrapper<>();
        mpjQueryWrapper.select(new String[]{"distinct t.id,t.account,t.name,t.avatar,t.multipoint_login,t.disable,t.login_ip,t.login_time,t.create_time,t.update_time"}).leftJoin("la_admin_role lar ON lar.admin_id = t.id").isNull("t.delete_time").orderByDesc(Arrays.asList("t.id"));
        this.systemAuthAdminMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"like:account:str", "like:name:str"});
        if (StringUtils.isNotNull(searchValidate.getRole_id())) {
            mpjQueryWrapper.in("lar.role_id", new Object[]{searchValidate.getRole_id()});
        }
        IPage<SystemAuthAdminListedVo> iPage = this.systemAuthAdminMapper.selectJoinPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), SystemAuthAdminListedVo.class, (MPJBaseJoin)mpjQueryWrapper);
        for (SystemAuthAdminListedVo vo : iPage.getRecords()) {
            if (vo.getId().equals(1)) {
                vo.setRoleName("\u7cfb\u7edf\u7ba1\u7406\u5458");
                vo.setDeptName("-");
            } else {
                List<SystemRole> roles = this.systemAuthRoleMapper.getByAdminId(vo.getId());
                ArrayList roleIds = new ArrayList();
                ArrayList roleNames = new ArrayList();
                if (!roles.isEmpty()) {
                    roles.forEach(item -> {
                        roleIds.add(item.getId());
                        roleNames.add(item.getName());
                    });
                    vo.setRoleId(roleIds);
                    vo.setRoleName(StringUtils.join(roleNames, (String)","));
                }
                List<Jobs> jobs = this.jobsMapper.getByAdminId(vo.getId());
                ArrayList jobsId = new ArrayList();
                ArrayList jobsNames = new ArrayList();
                if (!jobs.isEmpty()) {
                    jobs.forEach(item -> {
                        jobsId.add(item.getId());
                        jobsNames.add(item.getName());
                    });
                    vo.setJobsId(jobsId);
                    vo.setJobsName(StringUtils.join(jobsNames, (String)","));
                }
                List<Dept> depts = this.deptMapper.getByAdminId(vo.getId());
                ArrayList deptIds = new ArrayList();
                ArrayList deptNames = new ArrayList();
                if (!depts.isEmpty()) {
                    depts.forEach(item -> {
                        deptIds.add(item.getId());
                        deptNames.add(item.getName());
                    });
                    vo.setDeptId(deptIds);
                    vo.setDeptName(StringUtils.join(deptNames, (String)","));
                }
            }
            vo.setDisableDesc(vo.getDisable() != null && vo.getDisable().equals(0) ? "\u542f\u7528" : "\u7981\u7528");
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)vo.getAvatar()));
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((String)vo.getUpdateTime()));
            vo.setLoginTime(TimeUtils.timestampToDate((String)vo.getLoginTime()));
        }
        return PageResult.iPageHandle((IPage)iPage);
    }

    public SystemAuthAdminSelvesVo self(Integer adminId) {
        Admin sysAdmin = (Admin)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(Admin.class, info -> !info.getColumn().equals("password") && !info.getColumn().equals("delete_time")).isNull("delete_time").eq("id", (Object)adminId).last("limit 1"));
        SystemAuthAdminInformVo systemAuthAdminInformVo = new SystemAuthAdminInformVo();
        BeanUtils.copyProperties((Object)sysAdmin, (Object)systemAuthAdminInformVo);
        systemAuthAdminInformVo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)sysAdmin.getAvatar()));
        systemAuthAdminInformVo.setUpdateTime(TimeUtils.timestampToDate((Long)sysAdmin.getUpdateTime()));
        systemAuthAdminInformVo.setCreateTime(TimeUtils.timestampToDate((Long)sysAdmin.getCreateTime()));
        systemAuthAdminInformVo.setLastLoginTime(TimeUtils.timestampToDate((Long)sysAdmin.getLoginTime()));
        LinkedList<String> auths = new LinkedList<String>();
        if (adminId > 1) {
            ArrayList roleIds = new ArrayList();
            List<Integer> menuIds = this.iSystemRoleMenuService.selectMenuIdsByRoleId(roleIds);
            if (menuIds.size() > 0) {
                List<SystemMenu> systemAuthMenus = this.systemAuthMenuMapper.selectList(new QueryWrapper<SystemMenu>().eq("is_disable", (Object)0).in("id", (Collection)menuIds).in("menu_type", Arrays.asList("C", "A")).orderByAsc(Arrays.asList("menu_sort", "id")));
                for (SystemMenu item : systemAuthMenus) {
                    if (!StringUtils.isNotNull(item.getPerms()) || !StringUtils.isNotEmpty((String)item.getPerms())) continue;
                    auths.add(item.getPerms().trim());
                }
            }
            if (auths.size() <= 0) {
                auths.add("");
            }
        } else {
            auths.add("*");
        }
        SystemAuthAdminSelvesVo vo = new SystemAuthAdminSelvesVo();
        vo.setUser((Object)systemAuthAdminInformVo);
        vo.setPermissions(auths);
        return vo;
    }

    public SystemAuthAdminDetailVo detail(Integer id) {
        Admin sysAdmin = (Admin)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)sysAdmin, (String)"\u8d26\u53f7\u5df2\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        SystemAuthAdminDetailVo vo = new SystemAuthAdminDetailVo();
        BeanUtils.copyProperties((Object)sysAdmin, (Object)vo);
        vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)sysAdmin.getAvatar()));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)sysAdmin.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)sysAdmin.getUpdateTime()));
        vo.setLoginTime(TimeUtils.timestampToDate((Long)sysAdmin.getLoginTime()));
        vo.setRoleId(this.iAdminRoleService.getRoleIdAttr(sysAdmin.getId()));
        vo.setJobsId(this.iAdminJobsService.getJobIdAttr(sysAdmin.getId()));
        vo.setDeptId(this.iAdminDeptService.getDeptIdAttr(sysAdmin.getId()));
        vo.setFirstLogin(sysAdmin.getFirstLogin());
        return vo;
    }

    public void add(SystemAdminCreateValidate createValidate) {
        String[] field = new String[]{"id", "account", "name"};
        Assert.isNull(this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(field).isNull("delete_time").eq("account", (Object)createValidate.getAccount()).last("limit 1")), (String)"\u8d26\u53f7\u5df2\u5b58\u5728\u6362\u4e00\u4e2a\u5427\uff01", (Object[])new Object[0]);
        Assert.isNull(this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(field).isNull("delete_time").eq("name", (Object)createValidate.getName()).last("limit 1")), (String)"\u6635\u79f0\u5df2\u5b58\u5728\u6362\u4e00\u4e2a\u5427\uff01", (Object[])new Object[0]);
        PasswordUtils.validateAdminPassword(createValidate.getPassword());
        String pwd = ToolUtils.makePassword((String)createValidate.getPassword().trim());
        String createAvatar = createValidate.getAvatar();
        String defaultAvatar = "/api/static/backend_avatar.png";
        String avatar = StringUtils.isNotEmpty((String)createValidate.getAvatar()) ? UrlUtils.toRelativeUrl((String)createAvatar) : defaultAvatar;
        Admin model = new Admin();
        model.setAccount(createValidate.getAccount());
        model.setName(createValidate.getName());
        model.setAvatar(avatar);
        model.setPassword(pwd);
        model.setMultipointLogin(createValidate.getMultipointLogin());
        model.setDisable(createValidate.getDisable());
        model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.systemAuthAdminMapper.insert(model);
        List<Integer> deptIds = createValidate.getDeptId();
        List<Integer> jobsIds = createValidate.getJobsId();
        List<Integer> roleIds = createValidate.getRoleId();
        this.iAdminDeptService.batchInsert(model.getId(), deptIds);
        this.iAdminJobsService.batchInsert(model.getId(), jobsIds);
        this.iAdminRoleService.batchInsert(model.getId(), roleIds);
    }

    public void edit(SystemAdminUpdateValidate updateValidate, Integer adminId) {
        if (!adminId.equals(1) && updateValidate.getId().equals(1)) {
            throw new OperateException("\u60a8\u65e0\u6743\u9650\u7f16\u8f91\u7cfb\u7edf\u7ba1\u7406\u5458!");
        }
        boolean isEditInfo = false;
        if (updateValidate.getPassword() != null) {
            isEditInfo = true;
        }
        String[] field = new String[]{"id", "account", "name"};
        Assert.notNull((Object)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(field).eq("id", (Object)updateValidate.getId()).isNull("delete_time").last("limit 1")), (String)"\u8d26\u53f7\u4e0d\u5b58\u5728\u4e86!", (Object[])new Object[0]);
        Assert.isNull(this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(field).isNull("delete_time").eq("account", (Object)updateValidate.getAccount()).ne("id", (Object)updateValidate.getId()).last("limit 1")), (String)"\u8d26\u53f7\u5df2\u5b58\u5728\u6362\u4e00\u4e2a\u5427!", (Object[])new Object[0]);
        Assert.isNull(this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(field).isNull("delete_time").eq("name", (Object)updateValidate.getName()).ne("id", (Object)updateValidate.getId()).last("limit 1")), (String)"\u6635\u79f0\u5df2\u5b58\u5728\u6362\u4e00\u4e2a\u5427!", (Object[])new Object[0]);
        Admin admin = (Admin)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().eq("id", (Object)updateValidate.getId()).isNull("delete_time"));
        if (admin.getRoot().equals(1) && updateValidate.getDisable().equals(1)) {
            throw new OperateException("\u8d85\u7ea7\u7ba1\u7406\u5458\u4e0d\u80fd\u8bbe\u4e3a\u505c\u7528");
        }
        Admin model = new Admin();
        model.setId(updateValidate.getId());
        if (isEditInfo) {
            model.setName(updateValidate.getName());
            model.setAvatar(UrlUtils.toRelativeUrl((String)updateValidate.getAvatar()));
            model.setMultipointLogin(updateValidate.getMultipointLogin());
            model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            if (!updateValidate.getId().equals(1)) {
                model.setAccount(updateValidate.getAccount());
            }
            if (StringUtils.isNotNull(updateValidate.getPassword()) && StringUtils.isNotEmpty((String)updateValidate.getPassword())) {
                PasswordUtils.validateAdminPassword(updateValidate.getPassword());
                String pwd = ToolUtils.makePassword((String)updateValidate.getPassword().trim());
                model.setPassword(pwd);
            }
        } else {
            model.setDisable(updateValidate.getDisable());
        }
        this.systemAuthAdminMapper.updateById(model);
        if (isEditInfo) {
            if (StringUtils.isNotNull(updateValidate.getPassword()) && StringUtils.isNotEmpty((String)updateValidate.getPassword())) {
                StpUtil.kickout((Object)updateValidate.getId());
            }
            List<Integer> deptIds = updateValidate.getDeptId();
            List<Integer> jobsIds = updateValidate.getJobsId();
            List<Integer> roleIds = updateValidate.getRoleId();
            this.iAdminDeptService.batchInsert(updateValidate.getId(), deptIds);
            this.iAdminJobsService.batchInsert(updateValidate.getId(), jobsIds);
            this.iAdminRoleService.batchInsert(updateValidate.getId(), roleIds);
        }
    }

    public void upInfo(SystemAdminUpInfoValidate upInfoValidate, Integer adminId) {
        Admin model = (Admin)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(new String[]{"id,username,nickname,password,salt"}).eq("id", (Object)adminId).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)model, (String)"\u8d26\u53f7\u4e0d\u5b58\u5728\u4e86!", (Object[])new Object[0]);
        String createAvatar = upInfoValidate.getAvatar();
        String defaultAvatar = "/api/static/backend_avatar.png";
        String avatar = StringUtils.isNotEmpty((String)upInfoValidate.getAvatar()) ? UrlUtils.toRelativeUrl((String)createAvatar) : defaultAvatar;
        model.setAvatar(avatar);
        model.setName(upInfoValidate.getNickname());
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        if (StringUtils.isNotNull(upInfoValidate.getPassword()) && StringUtils.isNotEmpty((String)upInfoValidate.getPassword())) {
            // 新盐优先、老系统盐兜底（2026-11-04 割接兼容：老库口令按盐 "null" 生成）
            Assert.isTrue(ToolUtils.passwordMatches((String)upInfoValidate.getCurrPassword(), model.getPassword()), (String)"\u5f53\u524d\u5bc6\u7801\u4e0d\u6b63\u786e!", (Object[])new Object[0]);
            PasswordUtils.validateAdminPassword(upInfoValidate.getPassword());
            String pwd = ToolUtils.makePassword((String)upInfoValidate.getPassword().trim());
            model.setPassword(pwd);
        }
        this.systemAuthAdminMapper.updateById(model);
        if (StringUtils.isNotNull(upInfoValidate.getPassword()) && StringUtils.isNotEmpty((String)upInfoValidate.getPassword())) {
            StpUtil.kickout((Object)adminId);
        }
    }

    public void del(Integer id, Integer adminId) {
        String[] field = new String[]{"id", "account", "name"};
        Assert.notNull((Object)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(field).eq("id", (Object)id).isNull("delete_time").last("limit 1")), (String)"\u8d26\u53f7\u5df2\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        Assert.isFalse((boolean)id.equals(1), (String)"\u7cfb\u7edf\u7ba1\u7406\u5458\u4e0d\u5141\u8bb8\u5220\u9664!", (Object[])new Object[0]);
        Assert.isFalse((boolean)id.equals(adminId), (String)"\u4e0d\u80fd\u5220\u9664\u81ea\u5df1!", (Object[])new Object[0]);
        Admin model = new Admin();
        model.setId(id);
        model.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.systemAuthAdminMapper.updateById(model);
        this.iAdminRoleService.deleteByAdminId(id);
        this.iAdminJobsService.deleteByAdminId(id);
        this.iAdminDeptService.deleteByAdminId(id);
        StpUtil.kickout((Object)id);
    }

    public void disable(Integer id, Integer adminId) {
        Admin systemAuthAdmin = (Admin)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().select(new String[]{"id,username,nickname,is_disable"}).eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)systemAuthAdmin, (String)"\u8d26\u53f7\u5df2\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        Assert.isFalse((boolean)id.equals(adminId), (String)"\u4e0d\u80fd\u7981\u7528\u81ea\u5df1!", (Object[])new Object[0]);
        Integer disable = systemAuthAdmin.getDisable() == 1 ? 0 : 1;
        systemAuthAdmin.setDisable(disable);
        systemAuthAdmin.setUpdateTime(TimeUtils.timestamp());
        this.systemAuthAdminMapper.updateById(systemAuthAdmin);
        if (disable.equals(1)) {
            StpUtil.kickout((Object)id);
        }
    }

    public AdminMySelfVo mySelf(Integer id, List<Integer> roleIds) {
        AdminMySelfVo ret = new AdminMySelfVo();
        ret.setMenu(this.systemMenuService.selectMenuByRoleId(roleIds));
        ret.setPermissions(this.iAuthService.getBtnAuthByRoleId(id));
        SystemAuthAdminDetailVo admin = this.detail(id);
        AuthMySelfVo user = new AuthMySelfVo();
        BeanUtils.copyProperties((Object)admin, (Object)user);
        user.setDeptId(admin.getDeptId());
        user.setJobsId(admin.getJobsId());
        user.setRoleId(admin.getRoleId());
        ret.setUser(user);
        ret.setFirstLogin(admin.getFirstLogin());
        return ret;
    }

    public JSONObject getExportData(PageValidate pageValidate, SystemAdminSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        PageResult userVoPageResult = this.list(pageValidate, searchValidate);
        JSONObject ret = ToolUtils.getExportData((Long)userVoPageResult.getCount(), (Integer)limit, (Integer)searchValidate.getPage_start(), (Integer)searchValidate.getPage_end(), (String)"\u7ba1\u7406\u5458\u8bb0\u5f55\u5217\u8868");
        return ret;
    }

    public String export(SystemAdminSearchValidate searchValidate) {
        PageValidate pageValidate = new PageValidate();
        if (StringUtils.isNotNull(searchValidate.getPage_start())) {
            pageValidate.setPage_no(searchValidate.getPage_start());
        } else {
            pageValidate.setPage_no(Integer.valueOf(1));
        }
        if (StringUtils.isNotNull(searchValidate.getPage_end()) && StringUtils.isNotNull(searchValidate.getPage_size())) {
            pageValidate.setPage_size(Integer.valueOf(searchValidate.getPage_end() * searchValidate.getPage_size()));
        } else {
            pageValidate.setPage_size(Integer.valueOf(20));
        }
        Boolean isAll = StringUtils.isNull(searchValidate.getPage_type()) || searchValidate.getPage_type().equals(0);
        List excellist = this.getExcellist(isAll.booleanValue(), pageValidate, searchValidate);
        String fileName = ToolUtils.safeExportFileName(searchValidate.getFile_name());
        String folderPath = "/excel/export/" + TimeUtils.timestampToDay((Long)(System.currentTimeMillis() / 1000L)) + "/";
        String path = folderPath + fileName + ".xlsx";
        String filePath = YmlUtils.get((String)"like.upload-directory") + path;
        File folder = new File(YmlUtils.get((String)"like.upload-directory") + folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new OperateException("\u521b\u5efa\u6587\u4ef6\u5939\u5931\u8d25");
        }
        ((ExcelWriterBuilder)EasyExcel.write((String)filePath).head(SystemAuthAdminListedExportVo.class)).excelType(ExcelTypeEnum.XLSX).sheet("\u7ba1\u7406\u5458\u8bb0\u5f55").doWrite((Collection)excellist);
        return UrlUtils.toAdminAbsoluteUrl((String)path);
    }

    @Transactional
    public void editSelf(SystemEditSelfValidate updateValidate, Integer adminId) {
        String newPassword;
        Admin model = (Admin)this.systemAuthAdminMapper.selectOne(new QueryWrapper<Admin>().eq("id", (Object)adminId).isNull("delete_time").last("limit 1"));
        model.setName(updateValidate.getName());
        model.setAvatar(UrlUtils.toRelativeUrl((String)updateValidate.getAvatar()));
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        if (StringUtils.isNotNull(updateValidate.getPassword()) && StringUtils.isNotEmpty((String)updateValidate.getPassword()) && StringUtils.isNotEmpty((String)(newPassword = updateValidate.getPassword().trim()))) {
            PasswordUtils.validateAdminPassword(newPassword);
            String pwd = ToolUtils.makePassword((String)newPassword);
            model.setPassword(pwd);
        }
        model.setFirstLogin(Integer.valueOf(0));
        this.systemAuthAdminMapper.updateById(model);
    }

    private List<SystemAuthAdminListedExportVo> getExcellist(boolean isAll, PageValidate pageValidate, SystemAdminSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        MPJQueryWrapper<Admin> mpjQueryWrapper = new MPJQueryWrapper<>();
        mpjQueryWrapper.select(new String[]{"distinct t.id,t.account,t.name,t.avatar,t.multipoint_login,t.disable,t.login_ip,t.login_time,t.create_time,t.update_time"}).leftJoin("la_admin_role lar ON lar.admin_id = t.id").isNull("t.delete_time").orderByDesc(Arrays.asList("t.id"));
        this.systemAuthAdminMapper.setSearch(mpjQueryWrapper, (Object)searchValidate, new String[]{"like:account:str", "like:name:str"});
        if (StringUtils.isNotNull(searchValidate.getRole_id())) {
            mpjQueryWrapper.in("lar.role_id", new Object[]{searchValidate.getRole_id()});
        }
        List<SystemAuthAdminListedExportVo> retList = new ArrayList();
        if (isAll) {
            retList = this.systemAuthAdminMapper.selectJoinList(SystemAuthAdminListedExportVo.class, (MPJBaseJoin)mpjQueryWrapper);
        } else {
            IPage<SystemAuthAdminListedExportVo> iPage = this.systemAuthAdminMapper.selectJoinPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), SystemAuthAdminListedExportVo.class, (MPJBaseJoin)mpjQueryWrapper);
            retList = iPage.getRecords();
        }
        for (SystemAuthAdminListedExportVo vo : retList) {
            if (vo.getId().equals(1)) {
                vo.setRoleName("\u7cfb\u7edf\u7ba1\u7406\u5458");
                vo.setDeptName("-");
            } else {
                List<SystemRole> roles = this.systemAuthRoleMapper.getByAdminId(vo.getId());
                ArrayList roleIds = new ArrayList();
                ArrayList roleNames = new ArrayList();
                if (!roles.isEmpty()) {
                    roles.forEach(item -> {
                        roleIds.add(item.getId());
                        roleNames.add(item.getName());
                    });
                    vo.setRoleId(roleIds);
                    vo.setRoleName(StringUtils.join(roleNames, (String)","));
                }
                List<Jobs> jobs = this.jobsMapper.getByAdminId(vo.getId());
                ArrayList jobsId = new ArrayList();
                ArrayList jobsNames = new ArrayList();
                if (!jobs.isEmpty()) {
                    jobs.forEach(item -> {
                        jobsId.add(item.getId());
                        jobsNames.add(item.getName());
                    });
                    vo.setJobsId(jobsId);
                    vo.setJobsName(StringUtils.join(jobsNames, (String)","));
                }
                List<Dept> depts = this.deptMapper.getByAdminId(vo.getId());
                ArrayList deptIds = new ArrayList();
                ArrayList deptNames = new ArrayList();
                if (!depts.isEmpty()) {
                    depts.forEach(item -> {
                        deptIds.add(item.getId());
                        deptNames.add(item.getName());
                    });
                    vo.setDeptId(deptIds);
                    vo.setDeptName(StringUtils.join(deptNames, (String)","));
                }
            }
            vo.setDisableDesc(vo.getDisable() != null && vo.getDisable().equals(0) ? "\u542f\u7528" : "\u7981\u7528");
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)vo.getAvatar()));
            vo.setCreateTime(TimeUtils.timestampToDate((String)vo.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((String)vo.getUpdateTime()));
            vo.setLoginTime(TimeUtils.timestampToDate((String)vo.getLoginTime()));
        }
        return retList;
    }
}

