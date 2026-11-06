package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.ISettingPaymentService;
import com.mdd.admin.validate.setting.SettingPayConfigValidate;
import com.mdd.admin.vo.setting.SettingPaymentMethodVo;
import com.mdd.common.entity.setting.DevPayConfig;
import com.mdd.common.entity.setting.DevPayWay;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.mapper.setting.DevPayConfigMapper;
import com.mdd.common.mapper.setting.DevPayWayMapper;
import com.mdd.common.util.UrlUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class SettingPaymentServiceImpl implements ISettingPaymentService {
   @Resource
   DevPayConfigMapper devPayConfigMapper;
   @Resource
   DevPayWayMapper devPayWayMapper;

   public SettingPaymentServiceImpl() {
   }

   @Override
   public HashMap getPayWay() {
      List<DevPayWay> lists = this.devPayWayMapper.selectList(null);
      HashMap result = new LinkedHashMap();
      List<SettingPaymentMethodVo> mnp = new LinkedList<>();
      List<SettingPaymentMethodVo> oa = new LinkedList<>();
      List<SettingPaymentMethodVo> h5 = new LinkedList<>();

      for (DevPayWay devPayWay : lists) {
         DevPayConfig devPayConfig = (DevPayConfig)this.devPayConfigMapper
            .selectOne(new QueryWrapper<DevPayConfig>().eq("id", devPayWay.getPayConfigId()).last("limit 1"));
         SettingPaymentMethodVo vo = new SettingPaymentMethodVo();
         BeanUtils.copyProperties(devPayWay, vo);
         vo.setPayWayName(PaymentEnum.getPayWayMsg(devPayConfig.getPayWay()));
         vo.setIcon(UrlUtils.toAdminAbsoluteUrl(devPayConfig.getIcon()));
         switch (devPayWay.getScene()) {
            case 1:
               mnp.add(vo);
               break;
            case 2:
               oa.add(vo);
               break;
            case 3:
               h5.add(vo);
         }
      }

      result.put(1, mnp);
      result.put(2, oa);
      result.put(3, h5);
      return result;
   }

   @Override
   public JSONObject list() {
      final List<DevPayConfig> devPayConfigs = this.devPayConfigMapper.selectList(new QueryWrapper<DevPayConfig>().orderByDesc(Arrays.asList("sort", "id")));

      for (DevPayConfig dev : devPayConfigs) {
         dev.setPayWayName(PaymentEnum.getPayWayMsg(dev.getPayWay()));
         dev.setName(dev.getName());
         dev.setConfig(JSONObject.parse("{}", new Feature[0]));
         dev.setIcon(UrlUtils.toAdminAbsoluteUrl(dev.getIcon()));
      }

      JSONObject result = new JSONObject() {
         {
            this.put("lists", devPayConfigs);
         }
      };
      return result;
   }

   @Override
   public DevPayConfig getConfig(Integer id) {
      DevPayConfig devPayConfig = (DevPayConfig)this.devPayConfigMapper.selectOne(new QueryWrapper<DevPayConfig>().eq("id", id).last("limit 1"));
      devPayConfig.setName(PaymentEnum.getPayWayMsg(devPayConfig.getPayWay()));
      devPayConfig.setPayWayName(devPayConfig.getName());
      devPayConfig.setConfig(JSONObject.parse(devPayConfig.getConfig().toString(), new Feature[0]));
      devPayConfig.setIcon(UrlUtils.toAdminAbsoluteUrl(devPayConfig.getIcon()));
      return devPayConfig;
   }

   @Override
   public void setConfig(SettingPayConfigValidate configValidate) {
      DevPayConfig devPayConfig = (DevPayConfig)this.devPayConfigMapper
         .selectOne(new QueryWrapper<DevPayConfig>().eq("id", configValidate.getId()).last("limit 1"));
      Assert.notNull(devPayConfig, "数据不存在!");
      devPayConfig.setName(configValidate.getName());
      devPayConfig.setIcon(UrlUtils.toRelativeUrl(configValidate.getIcon()));
      devPayConfig.setSort(configValidate.getSort());
      devPayConfig.setRemark(configValidate.getRemark());
      if (devPayConfig.getPayWay().equals(1)) {
         devPayConfig.setConfig("{}");
      } else {
         // config 缺失时不能 NPE（task-7）：余额以外的支付方式允许先保存基础字段，参数留空
         devPayConfig.setConfig(configValidate.getConfig() == null
               ? "{}"
               : configValidate.getConfig().toJSONString(new com.alibaba.fastjson2.JSONWriter.Feature[0]));
      }

      this.devPayConfigMapper.updateById(devPayConfig);
   }

   @Transactional
   @Override
   public void setPayWay(HashMap<Integer, List<SettingPaymentMethodVo>> data) {
      for (Integer key : data.keySet()) {
         for (SettingPaymentMethodVo vo : data.get(key)) {
            DevPayWay way = new DevPayWay();
            way.setIsDefault(vo.getIsDefault());
            way.setStatus(vo.getStatus());
            this.devPayWayMapper.update(way, new QueryWrapper<DevPayWay>().eq("id", vo.getId()));
         }
      }
   }
}
