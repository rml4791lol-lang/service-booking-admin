package com.mdd.admin.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.admin.IAdminJobsService;
import com.mdd.common.entity.admin.AdminJobs;
import com.mdd.common.mapper.admin.AdminJobsMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminJobsServiceImpl implements IAdminJobsService {
   @Resource
   AdminJobsMapper adminJobsMapper;

   public AdminJobsServiceImpl() {
   }

   @Override
   public List<Integer> getJobIdAttr(Integer adminId) {
      List<Integer> ret = new ArrayList<>();
      List<AdminJobs> rolesList = this.adminJobsMapper.selectList(new QueryWrapper<AdminJobs>().eq("admin_id", adminId).select(new String[]{"jobs_id"}));
      if (rolesList.size() > 0) {
         for (AdminJobs adminJobs : rolesList) {
            ret.add(adminJobs.getJobsId());
         }
      }

      return ret;
   }

   @Override
   public void batchInsert(Integer adminId, List<Integer> jobsIds) {
      this.deleteByAdminId(adminId);
      if (jobsIds != null && !jobsIds.isEmpty()) {
         jobsIds.forEach(item -> this.adminJobsMapper.insert(new AdminJobs() {
               {
                  this.setAdminId(adminId);
                  this.setJobsId(item);
               }
            }));
      }
   }

   @Override
   public void deleteByAdminId(Integer adminId) {
      this.adminJobsMapper.delete(new QueryWrapper<AdminJobs>().eq("admin_id", adminId));
   }
}
