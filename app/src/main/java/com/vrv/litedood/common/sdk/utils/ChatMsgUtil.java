package com.vrv.litedood.common.sdk.utils;

import android.content.Context;
import android.text.TextUtils;

import com.vrv.litedood.R;
import com.vrv.imsdk.api.ChatMsgApi;


/**
 * 聊天消息工具类
 * Created by Yang on 2015/8/30 030.
 */
public class ChatMsgUtil {

    private static final String TAG = ChatMsgUtil.class.getSimpleName();


    /**
     * 最近消息会话中显示消息简要
     *
     * @param context
     * @param msgType
     * @param msg
     * @return
     */
    public static String lastMsgBrief(Context context, int msgType, String msg) {
        msgType = ChatMsgApi.reCalculateMsgType(msgType);
        switch (msgType) {
            case ChatMsgApi.TYPE_HTML:
                return context.getString(R.string.html);
            case ChatMsgApi.TYPE_TEXT:
                return ChatMsgApi.parseTxtJson(msg);
            case ChatMsgApi.TYPE_AUDIO:
                return context.getString(R.string.audio);
            case ChatMsgApi.TYPE_POSITION:
                return context.getString(R.string.position);
            case ChatMsgApi.TYPE_IMAGE:
                return context.getString(R.string.image);
            case ChatMsgApi.TYPE_FILE:
                return context.getString(R.string.file);
            case ChatMsgApi.TYPE_CARD:
                return context.getString(R.string.card);
            case ChatMsgApi.TYPE_WEAK_HINT:
                String hint = ChatMsgApi.parseTxtJson(msg);
                return TextUtils.isEmpty(hint) ? context.getString(R.string.weakHint) : hint;
            case ChatMsgApi.TYPE_RED_ENVELOPE:
                return "[豆豆红包:]" + ChatMsgApi.parseTxtJson(msg);
            default:
                return context.getString(R.string.unknowMsg) + msgType;
        }
    }

}
