package com.mdd.admin.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.common.entity.admin.AdminRole;
import com.mdd.common.mapper.admin.AdminRoleMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleServiceImpl implements IAdminRoleService {
   @Resource
   AdminRoleMapper adminRoleMapper;

   public AdminRoleServiceImpl() {
   }

   @Override
   public List<Integer> getRoleIdAttr(Integer adminId) {
      List<Integer> ret = new ArrayList<>();
      List<AdminRole> rolesList = this.adminRoleMapper.selectList(new QueryWrapper<AdminRole>().eq("admin_id", adminId).select(new String[]{"role_id"}));
      if (rolesList.size() > 0) {
         for (AdminRole adminRole : rolesList) {
            ret.add(adminRole.getRoleId());
         }
      }

      return ret;
   }

   @Override
   public List<AdminRole> getAdminIdByRoleId(Integer roleId) {
      return this.adminRoleMapper.selectList(new QueryWrapper<AdminRole>().eq("role_id", roleId).select(new String[]{"admin_id"}));
   }

   @Override
   public void batchInsert(Integer adminId, List<Integer> roleIds) {
      this.deleteByAdminId(adminId);
      if (roleIds != null && !roleIds.isEmpty()) {
         roleIds.forEach(item -> this.adminRoleMapper.insert(new AdminRole() {
               {
                  this.setAdminId(adminId);
                  this.setRoleId(item);
               }
            }));
      }
   }

   @Override
   public void deleteByAdminId(Integer adminId) {
      this.adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("admin_id", adminId));
   }
}
