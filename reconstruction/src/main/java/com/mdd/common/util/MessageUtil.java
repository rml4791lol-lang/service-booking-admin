/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Component
 */
package com.mdd.common.util;

import com.mdd.common.entity.Message;
import com.mdd.common.mapper.MessageMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
    private static MessageMapper messageMapper;

    @Resource
    public void setMessageMapper(MessageMapper mapper) {
        messageMapper = mapper;
    }

    public static void send(int type, String title, String content, Integer senderId, String senderType, String senderName, Integer receiverId, String receiverType) {
        Message msg = new Message();
        msg.setType(type);
        msg.setTitle(title);
        msg.setContent(content);
        msg.setSenderId(senderId);
        msg.setSenderType(senderType);
        msg.setSenderName(senderName);
        msg.setReceiverId(receiverId);
        msg.setReceiverType(receiverType);
        msg.setIsRead(0);
        msg.setCreateTime(System.currentTimeMillis() / 1000L);
        msg.setUpdateTime(System.currentTimeMillis() / 1000L);
        messageMapper.insert(msg);
    }

    public static void sendLeaveAuditNotice(Integer receiverId, String receiverType, String auditName, boolean approved, String remark) {
        String status = approved ? "\u5df2\u901a\u8fc7" : "\u5df2\u62d2\u7edd";
        String title = "\u8bf7\u5047\u5ba1\u6279" + status;
        String content = auditName + " " + status + "\u4e86\u60a8\u7684\u8bf7\u5047\u7533\u8bf7";
        if (remark != null && !remark.isEmpty()) {
            content = content + "\uff0c\u5907\u6ce8\uff1a" + remark;
        }
        MessageUtil.send(2, title, content, null, "system", auditName, receiverId, receiverType);
    }

    public static void sendPaymentSuccess(Integer userId, String courseName, String orderSn) {
        String title = "\u4ed8\u6b3e\u8d2d\u4e70\u6210\u529f";
        String content = "\u60a8\u5df2\u6210\u529f\u8d2d\u4e70\u8bfe\u7a0b\u300c" + courseName + "\u300d\uff0c\u8ba2\u5355\u53f7\uff1a" + orderSn;
        MessageUtil.send(3, title, content, null, "system", "\u7cfb\u7edf", userId, "student");
    }
}

