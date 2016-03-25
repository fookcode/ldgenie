package com.vrv.ldgenie.common.sdk.action;

import android.text.TextUtils;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.imsdk.model.Contact;
import com.vrv.imsdk.model.Group;
import com.vrv.imsdk.model.SystemMsg;

import java.util.ArrayList;

/**
 * 请求同一管理入口
 * Created by Yang on 2015/10/29 029.
 */
public class RequestHelper {

    /**
     * 上次登录信息自动登陆
     *
     * @param handler
     * @return
     */
    public static boolean autoLogin(RequestHandler handler) {
        return SDKManager.instance().getAuth().authLogin(CallBackHelper.buildCallBack(handler));
    }

    /**
     * 手机登录
     */
    public static boolean login(String user, String pwd, String entArea, String nationalCode, RequestHandler handler) {
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(entArea)) {
            return false;
        }
        return SDKManager.instance().getAuth().login(user, pwd, entArea, nationalCode, CallBackHelper.buildCallBack(handler));
    }

    public static boolean loginVerify(boolean next, String userName, String code, RequestHandler handler) {
        return SDKManager.instance().getAuth().loginVerify(next, userName, code, CallBackHelper.buildCallBack(handler));
    }

    //退出
    public static boolean logout(RequestHandler handler) {
        return SDKManager.instance().getAuth().logout(CallBackHelper.buildCallBack(handler));
    }

    /**
     * 注册或忘记密码找回
     */
    public static boolean register(boolean register, String user, String entArea, String nationalCode, RequestHandler handler) {
        if (register) {
            return SDKManager.instance().getAuth().register(user, entArea, nationalCode, CallBackHelper.buildCallBack(handler));
        } else {
            return SDKManager.instance().getAuth().forgetPassword(user, entArea, nationalCode, CallBackHelper.buildCallBack(handler));
        }
    }

    //验证校验码
    public static boolean verifyCode(boolean register, long registerID, String authCode, RequestHandler handler) {
        if (register) {
            return SDKManager.instance().getAuth().registerVerify(registerID, authCode, CallBackHelper.buildCallBack(handler));
        } else {
            return SDKManager.instance().getAuth().forgetPasswordVerify(registerID, authCode, CallBackHelper.buildCallBack(handler));
        }
    }

    //注册/找回密码第2步
    public static boolean registerStep(boolean register, long registerID, String name, String psw, RequestHandler handler) {
        if (register) {
            return SDKManager.instance().getAuth().registerStep(registerID, name, psw, CallBackHelper.buildCallBack(handler));
        } else {
            return SDKManager.instance().getAuth().forgerPasswordStep(registerID, "", psw, CallBackHelper.buildCallBack(handler));
        }
    }

    //删除聊天
    public static boolean deleteChat(long chatID) {
        return SDKManager.instance().getChatList().delete(chatID);
    }

    //设置系统消息已读
    public static boolean setSysMsgRead(ArrayList<SystemMsg> list) {
        return SDKManager.instance().getSysMsgList().setRead(list);
    }

    //获取历史聊天记录
    public static boolean getChatHistory(long targetID, long lastMsgID, int offset, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().getHistoryMsg(targetID, lastMsgID, offset, CallBackHelper.buildCallBack(handler));
    }

    //设置消息已读
    public static boolean setMsgRead(long targetID, long messageID) {
        return SDKManager.instance().getChatMsgList().setMsgRead(targetID, messageID);
    }

    //发送文本消息
    public static boolean sendTxt(long targetID, String text, ArrayList<Long> relatedUsers, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().sendTxt(targetID, text, relatedUsers, CallBackHelper.buildCallBack(handler));
    }

    //发送图片
    public static boolean sendImg(long targetID, String imgPath, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().sendImage(targetID, imgPath, CallBackHelper.buildCallBack(handler));
    }

    //发送文件
    public static boolean sendFile(long targetID, String filePath, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().sendFile(targetID, filePath, CallBackHelper.buildCallBack(handler));
    }

    //发送名片
    public static boolean sendCard(long targetID, long userID, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().sendCard(targetID, userID, CallBackHelper.buildCallBack(handler));
    }

    //发送位置
    public static boolean sendPosition(long targetID, String address, String latitude, String longitude, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().sendPosition(targetID, address, latitude, longitude, CallBackHelper.buildCallBack(handler));
    }

    //发送语音
    public static boolean sendAudio(long targetID, String audioPath, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().sendAudio(targetID, audioPath, CallBackHelper.buildCallBack(handler));
    }

    //重发失败消息
    public static boolean reSend(ChatMsg chatMsg, RequestHandler handler) {
        return SDKManager.instance().getChatMsgList().reSendFailureMessage(chatMsg, CallBackHelper.buildCallBack(handler));
    }

    //设置标星
    public static boolean addStar(Contact contact, RequestHandler handler) {
        return contact.addStar(CallBackHelper.buildCallBack(handler));
    }

    //移除标星
    public static boolean removeStar(Contact contact, RequestHandler handler) {
        return contact.removeStar(CallBackHelper.buildCallBack(handler));
    }

    //修改备注
    public static boolean modifyRemark(Contact contact, RequestHandler handler) {
        return contact.modifyRemark(CallBackHelper.buildCallBack(handler));
    }

    //获取好友验证方式
    public static boolean getContactVerifyType(long userID, RequestHandler handler) {
        return SDKManager.instance().getContactList().getVerifyType(userID, CallBackHelper.buildCallBack(handler));
    }

    //添加好友
    public static boolean addContact(long userID, String verifyInfo, String remark, RequestHandler handler) {
        return SDKManager.instance().getContactList().addContact(userID, verifyInfo, remark, CallBackHelper.buildCallBack(handler));
    }

    //删除好友
    public static boolean removeContact(long userID, RequestHandler handler) {
        return SDKManager.instance().getContactList().removeContact(userID, CallBackHelper.buildCallBack(handler));
    }

    //创建群
    public static boolean createGroup(ArrayList<Long> inviteIDs, RequestHandler handler) {
        return SDKManager.instance().getGroupList().createGroupByID(inviteIDs, CallBackHelper.buildCallBack(handler));
    }

    //转让群
    public static boolean transferGroup(Group group, Contact contact, RequestHandler handler) {
        return group.transfer(contact, CallBackHelper.buildCallBack(handler));
    }

    //获取群验证方式
    public static boolean getGroupVerifyType(long groupID, RequestHandler handler) {
        return SDKManager.instance().getGroupList().getVerifyType(groupID, CallBackHelper.buildCallBack(handler));
    }

    //申请加群
    public static boolean addGroup(long groupID, String verifyInfo, RequestHandler handler) {
        return SDKManager.instance().getGroupList().addGroup(groupID, verifyInfo, CallBackHelper.buildCallBack(handler));
    }

    //删除退出解散群
    public static boolean deleteGroup(Group group, RequestHandler handler) {
        return group.exit(CallBackHelper.buildCallBack(handler));
    }

    //设置群消息提醒方式
    public static boolean setGroupMsgReminderType(long targetID, byte type, String period, RequestHandler handler) {
        /*memberBean.getShield().setPhoneMsgSet(shieldTypes[i]);
        GroupShieldSetParam param = new GroupShieldSetParam();
        param.setGroupid(groupID);
        param.setReceiveMsgType(shieldTypes[i]);
        param.setReceiveTimePeriod("");
        GroupHandler msgSetHandler = new GroupHandler(false, context);
        ContactRequest.setGroupMsgReminderType(param, CallBackHelper.createCallBack(msgSetHandler));*/
        return false;
    }

    //获取群成员列表
    public static boolean getGroupMembers(Group group, RequestHandler handler) {
        return group.getMemberList(CallBackHelper.buildCallBack(handler));
    }

    //添加群成员
    public static boolean addMembers(Group group, ArrayList<Long> inviteIDs, RequestHandler handler) {
        return group.addMemberListByID(inviteIDs, CallBackHelper.buildCallBack(handler));
    }

    //移除群成员
    public static boolean removeMember(Group group, Contact member, RequestHandler handler) {
        return group.removeMember(member, CallBackHelper.buildCallBack(handler));
    }

    //移除群成员列表
    public static boolean removeMembers(Group group, ArrayList<Contact> members, RequestHandler handler) {
        return group.removeMemberList(members, CallBackHelper.buildCallBack(handler));
    }

    //修改群成员名片
    public static boolean modifyMemberRemark() {
        /*GroupMemberInfoSetParam param = new GroupMemberInfoSetParam();
        param.setGroupId(groupBean.getId());
        param.setMemberId(memberBean.getId());
        param.setMemberName(remark);
        param.setChatContext("");
        ContactRequest.setGroupMemberInfo(param, CallBackHelper.createCallBack(requestHandler));*/
        return false;
    }

    //下载缩略图
    public static boolean downloadThumbImg(ChatMsg chatMsg, RequestHandler handler) {
        return chatMsg.downloadThumbImg(CallBackHelper.buildCallBack(handler));
    }

    //下载原图
    public static boolean downloadOrgImg(ChatMsg chatMsg, RequestHandler handler) {
        return chatMsg.downloadOrgImg(CallBackHelper.buildCallBack(handler));
    }

    //下载文件
    public static boolean downloadFile(ChatMsg chatMsg, RequestHandler handler) {
        return chatMsg.downloadFile(CallBackHelper.buildCallBack(handler));
    }

    public static boolean uploadFile(String path, boolean encrypt, RequestHandler uploadHandler) {
        return false;
    }

    //设置个人信息
    public static boolean setMyInfo(Contact contact, RequestHandler handler) {
        return SDKManager.instance().getAuth().setInfo(contact,CallBackHelper.buildCallBack(handler));
    }

    //获取clientKey
    public static boolean getClientKey(RequestHandler handler) {
        return false;
    }

    //获取陌生人信息
    public static boolean getUserInfo(long userID, RequestHandler handler) {
        return SDKManager.instance().getContactList().getInfo(userID, CallBackHelper.buildCallBack(handler));
    }

    //获取群信息
    public static boolean getGroupInfo(long groupID,RequestHandler handler){
        return SDKManager.instance().getGroupList().getInfo(groupID,CallBackHelper.buildCallBack(handler));
    }

    //获取企业列表
    public static boolean getEnterpriseList(RequestHandler handler) {
        return false;
    }

    //获取组织和用户列表
    public static boolean getOrgAndUserList(long enterpriseID, long organizeID, RequestHandler handler) {
        return false;
    }


    public static boolean searchNet(String key, RequestHandler handler) {
        return SDKManager.instance().getContactList().searchNet(key, CallBackHelper.buildCallBack(handler));
    }

    //系统消息
    public static boolean getSystemMsgList(long time, int offset, RequestHandler handler) {
        return SDKManager.instance().getSysMsgList().getList(time, offset, CallBackHelper.buildCallBack(handler));
    }

    //同意添加好友请求
    public static boolean responseContact(SystemMsg systemMsg, RequestHandler handler) {
        return SDKManager.instance().getSysMsgList().agreeContact(systemMsg.getId(), systemMsg.getUserID(), systemMsg.getName(), CallBackHelper.buildCallBack(handler));
    }
}
