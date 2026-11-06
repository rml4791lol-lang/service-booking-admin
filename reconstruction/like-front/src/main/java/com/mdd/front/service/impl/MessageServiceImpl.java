/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.Message
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.util.MessageUtil
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.front.service.IMessageService
 *  com.mdd.front.service.impl.MessageServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.Message;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.util.MessageUtil;
import com.mdd.common.util.ParamUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.IMessageService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl
implements IMessageService {
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    MessageMapper messageMapper;

    public void send(Integer senderId, String senderType, Map<String, Object> params) {
        String title;
        Integer type = ParamUtils.intOrDefault(params.get("type"), 0, "消息类型");
        String content = params.getOrDefault("content", "").toString();
        String senderName = "\u672a\u77e5";
        if ("instructor".equals(senderType)) {
            Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)senderId);
            senderName = instructor != null ? instructor.getName() : "\u672a\u77e5\u6559\u7ec3";
        }
        switch (type) {
            case 1: {
                title = "\u4e0a\u8bfe\u63d0\u9192";
                break;
            }
            case 2: {
                title = "\u8bf7\u5047\u5ba1\u6279\u901a\u77e5";
                break;
            }
            case 3: {
                title = "\u4ed8\u6b3e\u8d2d\u4e70\u6210\u529f";
                break;
            }
            case 4: {
                title = "\u6559\u7ec3\u7533\u8bf7\u5f00\u529e\u65b0\u73ed";
                break;
            }
            default: {
                title = "\u7cfb\u7edf\u6d88\u606f";
            }
        }
        MessageUtil.send((int)type, (String)title, (String)content, (Integer)senderId, (String)senderType, (String)senderName, (Integer)0, (String)"admin");
    }

    public Integer unreadCount(Integer userId) {
        return Math.toIntExact(this.messageMapper.selectCount(new QueryWrapper<Message>().eq("receiver_id", (Object)userId).eq("is_read", (Object)0).isNull("delete_time")));
    }

    public Map<String, Object> list(Integer pageNo, Integer pageSize, Integer userId) {
        IPage<Message> page = new Page<Message>((long)pageNo.intValue(), (long)pageSize.intValue());
        page = this.messageMapper.selectPage((IPage)page, new QueryWrapper<Message>().eq("receiver_id", (Object)userId).isNull("delete_time").orderByDesc("id"));
        ArrayList list = new ArrayList();
        for (Message msg : page.getRecords()) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", msg.getId());
            map.put("title", msg.getTitle());
            map.put("content", msg.getContent());
            map.put("isRead", msg.getIsRead());
            map.put("createTime", msg.getCreateTime() != null ? TimeUtils.timestampToDate((Long)msg.getCreateTime()) : "");
            list.add(map);
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("count", page.getTotal());
        result.put("lists", list);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        return result;
    }

    public void read(Integer messageId, Integer userId) {
        Message msg = (Message)this.messageMapper.selectOne(new QueryWrapper<Message>().eq("id", (Object)messageId).eq("receiver_id", (Object)userId).last("limit 1"));
        if (msg != null && (msg.getIsRead() == null || msg.getIsRead() == 0)) {
            msg.setIsRead(Integer.valueOf(1));
            this.messageMapper.updateById(msg);
        }
    }
}

