package com.mdd.admin.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.admin.IAdminDeptService;
import com.mdd.common.entity.admin.AdminDept;
import com.mdd.common.mapper.admin.AdminDeptMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminDeptServiceImpl implements IAdminDeptService {
   @Resource
   AdminDeptMapper adminDeptMapper;

   public AdminDeptServiceImpl() {
   }

   @Override
   public List<Integer> getDeptIdAttr(Integer adminId) {
      List<Integer> ret = new ArrayList<>();
      List<AdminDept> rolesList = this.adminDeptMapper.selectList(new QueryWrapper<AdminDept>().eq("admin_id", adminId).select(new String[]{"dept_id"}));
      if (rolesList.size() > 0) {
         for (AdminDept adminDept : rolesList) {
            ret.add(adminDept.getDeptId());
         }
      }

      return ret;
   }

   @Override
   public void batchInsert(Integer adminId, List<Integer> deptIds) {
      this.deleteByAdminId(adminId);
      if (deptIds != null && !deptIds.isEmpty()) {
         deptIds.forEach(item -> this.adminDeptMapper.insert(new AdminDept() {
               {
                  this.setAdminId(adminId);
                  this.setDeptId(item);
               }
            }));
      }
   }

   @Override
   public void deleteByAdminId(Integer adminId) {
      this.adminDeptMapper.delete(new QueryWrapper<AdminDept>().eq("admin_id", adminId));
   }
}
