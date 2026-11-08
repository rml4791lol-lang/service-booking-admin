/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.service.IChannelOaCallBackService
 *  com.mdd.admin.service.impl.ChannelOaCallBackServiceImpl
 *  com.mdd.common.entity.OfficialAccountReply
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.OfficialAccountReplyMapper
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  javax.annotation.Resource
 *  me.chanjar.weixin.mp.api.WxMpService
 *  me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
 *  me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage
 *  me.chanjar.weixin.mp.builder.outxml.TextBuilder
 *  me.chanjar.weixin.mp.config.WxMpConfigStorage
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.IChannelOaCallBackService;
import com.mdd.common.entity.OfficialAccountReply;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.OfficialAccountReplyMapper;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import java.util.List;
import javax.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.stereotype.Service;

@Service
public class ChannelOaCallBackServiceImpl
implements IChannelOaCallBackService {
    @Resource
    private OfficialAccountReplyMapper officialAccountReplyMapper;

    public String checkSignature(String signature, String timestamp, String nonce, String echostr) {
        WxMpService wxMpService = WxMnpDriver.oa();
        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "\u975e\u6cd5\u8bf7\u6c42";
    }

    public String post(String requestBody, String signature, String timestamp, String nonce, String encType, String msgSignature) {
        WxMpService wxMpService = WxMnpDriver.oa();
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("\u975e\u6cd5\u8bf7\u6c42\uff0c\u53ef\u80fd\u5c5e\u4e8e\u4f2a\u9020\u7684\u8bf7\u6c42\uff01");
        }
        String outMsg = null;
        if (encType == null) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml((String)requestBody);
            WxMpXmlOutMessage outMessage = this.msgHandler(inMessage);
            if (outMessage == null) {
                return "";
            }
            outMsg = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml((String)requestBody, (WxMpConfigStorage)wxMpService.getWxMpConfigStorage(), (String)timestamp, (String)nonce, (String)msgSignature);
            WxMpXmlOutMessage outMessage = this.msgHandler(inMessage);
            if (outMessage == null) {
                return "";
            }
            outMsg = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
        }
        return outMsg;
    }

    private WxMpXmlOutMessage msgHandler(WxMpXmlMessage wxMessage) {
        try {
            if (wxMessage.getMsgType().equals("text")) {
                String msg = this.keyMsg(wxMessage);
                return this.textBuild(msg, wxMessage);
            }
            if (wxMessage.getMsgType().equals("event") && wxMessage.getEvent().equals("subscribe")) {
                String msg = this.subMsg();
                return this.textBuild(msg, wxMessage);
            }
        }
        catch (Exception e) {
            throw new OperateException("\u516c\u4f17\u53f7\u6d88\u606f\u56de\u8c03\u9519\u8bef" + e.getMessage());
        }
        return null;
    }

    private WxMpXmlOutMessage textBuild(String content, WxMpXmlMessage wxMessage) {
        return ((TextBuilder)((TextBuilder)WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).build();
    }

    private String keyMsg(WxMpXmlMessage wxMessage) {
        List<OfficialAccountReply> oaReplyList = this.officialAccountReplyMapper.selectList(new QueryWrapper<OfficialAccountReply>().eq("reply_type", (Object)2).isNull("delete_time").eq("status", (Object)1).orderByAsc("id"));
        String msg = null;
        for (OfficialAccountReply oaReply : oaReplyList) {
            if (oaReply.getMatchingType() == 1 && oaReply.getKeyword().equals(wxMessage.getContent())) {
                msg = oaReply.getContent();
            }
            if (oaReply.getMatchingType() != 2 || !wxMessage.getContent().contains(oaReply.getKeyword())) continue;
            msg = oaReply.getContent();
        }
        return msg == null ? this.defaultMsg() : msg;
    }

    private String defaultMsg() {
        OfficialAccountReply officialAccountReply = (OfficialAccountReply)this.officialAccountReplyMapper.selectOne(new QueryWrapper<OfficialAccountReply>().eq("reply_type", (Object)3).isNull("delete_time").eq("status", (Object)1).last("limit 1"));
        if (officialAccountReply == null) {
            return null;
        }
        return officialAccountReply.getContent();
    }

    private String subMsg() {
        OfficialAccountReply officialAccountReply = (OfficialAccountReply)this.officialAccountReplyMapper.selectOne(new QueryWrapper<OfficialAccountReply>().eq("reply_type", (Object)1).isNull("delete_time").eq("status", (Object)1).last("limit 1"));
        if (officialAccountReply == null) {
            return this.defaultMsg();
        }
        return officialAccountReply.getContent();
    }
}

