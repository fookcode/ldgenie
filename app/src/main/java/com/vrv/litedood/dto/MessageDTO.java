package com.vrv.litedood.dto;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.vrv.imsdk.model.ChatMsg;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kinee on 2016/4/11.
 */
public class  MessageDTO {
    public static final String TAG = MessageDTO.class.getSimpleName();
    public static final String TABLE_NAME = "t_message";

    //定义表字段
    public static final String TABLE_MESSAGE_COLUMN_ID = "id";
    public static final String TABLE_MESSAGE_COLUMN_MSGSTATUS = "msgstatus";
    public static final String TABLE_MESSAGE_COLUMN_SENDID = "sendid";
    public static final String TABLE_MESSAGE_COLUMN_RECEIVEID = "receiveid";
    public static final String TABLE_MESSAGE_COLUMN_SENDTIME = "sendtime";
    public static final String TABLE_MESSAGE_COLUMN_MESSAGE = "message";
    public static final String TABLE_MESSAGE_COLUMN_MESSAGETYPE = "messagetype";
    public static final String TABLE_MESSAGE_COLUMN_MESSAGEID = "messageid";
    public static final String TABLE_MESSAGE_COLUMN_LASTMESSAGEID = "lastmessageid";
    public static final String TABLE_MESSAGE_COLUMN_SENDERSESSIONID = "sendersessionid";
    public static final String TABLE_MESSAGE_COLUMN_LIMITRANGE = "limitrange";
    public static final String TABLE_MESSAGE_COLUMN_FORMAT = "format";
    public static final String TABLE_MESSAGE_COLUMN_MSGPROPERTIES = "msgproperties";
    public static final String TABLE_MESSAGE_COLUMN_ACTIVETYPE = "activetype";
    public static final String TABLE_MESSAGE_COLUMN_RELATEDUSERS = "relatedusers";
    public static final String TABLE_MESSAGE_COLUMN_RELATEDMSGID = "relatedmsgid";
    public static final String TABLE_MESSAGE_COLUMN_ITEMMODEL_ID = "itemmodelid";
    public static final String TABLE_MESSAGE_COLUMN_ITEMMODEL_NAME = "itemmodelname";
    public static final String TABLE_MESSAGE_COLUMN_ITEMMODEL_AVATAR = "itemmodelavatar";

    public static HashMap<String, String> t_message = new HashMap<>();
    static {
        t_message.put(TABLE_MESSAGE_COLUMN_ID,TABLE_MESSAGE_COLUMN_ID);
        t_message.put(TABLE_MESSAGE_COLUMN_MSGSTATUS,TABLE_MESSAGE_COLUMN_MSGSTATUS);
        t_message.put(TABLE_MESSAGE_COLUMN_SENDID,TABLE_MESSAGE_COLUMN_SENDID);
        t_message.put(TABLE_MESSAGE_COLUMN_RECEIVEID,TABLE_MESSAGE_COLUMN_RECEIVEID);
        t_message.put(TABLE_MESSAGE_COLUMN_SENDTIME,TABLE_MESSAGE_COLUMN_SENDTIME);
        t_message.put(TABLE_MESSAGE_COLUMN_MESSAGE,TABLE_MESSAGE_COLUMN_MESSAGE);
        t_message.put(TABLE_MESSAGE_COLUMN_MESSAGETYPE,TABLE_MESSAGE_COLUMN_MESSAGETYPE);
        t_message.put(TABLE_MESSAGE_COLUMN_MESSAGEID,TABLE_MESSAGE_COLUMN_MESSAGEID);
        t_message.put(TABLE_MESSAGE_COLUMN_LASTMESSAGEID,TABLE_MESSAGE_COLUMN_LASTMESSAGEID);
        t_message.put(TABLE_MESSAGE_COLUMN_SENDERSESSIONID,TABLE_MESSAGE_COLUMN_SENDERSESSIONID);
        t_message.put(TABLE_MESSAGE_COLUMN_LIMITRANGE,TABLE_MESSAGE_COLUMN_LIMITRANGE);
        t_message.put(TABLE_MESSAGE_COLUMN_FORMAT,TABLE_MESSAGE_COLUMN_FORMAT);
        t_message.put(TABLE_MESSAGE_COLUMN_MSGPROPERTIES,TABLE_MESSAGE_COLUMN_MSGPROPERTIES);
        t_message.put(TABLE_MESSAGE_COLUMN_ACTIVETYPE,TABLE_MESSAGE_COLUMN_ACTIVETYPE);
        t_message.put(TABLE_MESSAGE_COLUMN_RELATEDUSERS,TABLE_MESSAGE_COLUMN_RELATEDUSERS);
        t_message.put(TABLE_MESSAGE_COLUMN_RELATEDMSGID,TABLE_MESSAGE_COLUMN_RELATEDMSGID);
        t_message.put(TABLE_MESSAGE_COLUMN_ITEMMODEL_ID,TABLE_MESSAGE_COLUMN_ITEMMODEL_ID);
        t_message.put(TABLE_MESSAGE_COLUMN_ITEMMODEL_NAME,TABLE_MESSAGE_COLUMN_ITEMMODEL_NAME);
        t_message.put(TABLE_MESSAGE_COLUMN_ITEMMODEL_AVATAR,TABLE_MESSAGE_COLUMN_ITEMMODEL_AVATAR);

    }

    public static String getCreateSQL() {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + TABLE_MESSAGE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_MESSAGE_COLUMN_MSGSTATUS + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_SENDID + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_RECEIVEID + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_SENDTIME + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_MESSAGE + " TEXT, "
                + TABLE_MESSAGE_COLUMN_MESSAGETYPE + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_MESSAGEID + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_LASTMESSAGEID + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_SENDERSESSIONID + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_LIMITRANGE + " TEXT, "
                + TABLE_MESSAGE_COLUMN_FORMAT + " TEXT, "
                + TABLE_MESSAGE_COLUMN_MSGPROPERTIES + " TEXT, "
                + TABLE_MESSAGE_COLUMN_ACTIVETYPE + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_RELATEDUSERS + " TEXT, "
                + TABLE_MESSAGE_COLUMN_RELATEDMSGID + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_ITEMMODEL_ID + " INTEGER, "
                + TABLE_MESSAGE_COLUMN_ITEMMODEL_NAME + " TEXT,"
                + TABLE_MESSAGE_COLUMN_ITEMMODEL_AVATAR + " TEXT "
                + ")";
        return sql;
    }

    public static String[] getAllColumns() {
        String[] result = t_message.values().toArray(new String[0]);

        return (String[])result;
    }

    public static ContentValues convertChatMessage(ChatMsg chatMsg) {
        ContentValues result = new ContentValues();

        result.put(TABLE_MESSAGE_COLUMN_MSGSTATUS, chatMsg.getMsgStatus());
        result.put(TABLE_MESSAGE_COLUMN_SENDID, chatMsg.getSendID());
        result.put(TABLE_MESSAGE_COLUMN_RECEIVEID, chatMsg.getReceiveID());
        result.put(TABLE_MESSAGE_COLUMN_SENDTIME, chatMsg.getSendTime());
        result.put(TABLE_MESSAGE_COLUMN_MESSAGE, chatMsg.getMessage());
        result.put(TABLE_MESSAGE_COLUMN_MESSAGETYPE, chatMsg.getMessageType());
        result.put(TABLE_MESSAGE_COLUMN_MESSAGEID, chatMsg.getMessageID());
        result.put(TABLE_MESSAGE_COLUMN_LASTMESSAGEID, chatMsg.getLastMessageID());
        result.put(TABLE_MESSAGE_COLUMN_SENDERSESSIONID, chatMsg.getSenderSessionID());
        String limitRange = chatMsg.getLimitRange().toString();
        if (!limitRange.isEmpty()) result.put(TABLE_MESSAGE_COLUMN_LIMITRANGE, limitRange);
        if (!chatMsg.getFormat().isEmpty()) result.put(TABLE_MESSAGE_COLUMN_FORMAT, chatMsg.getFormat());
        if (!chatMsg.getMsgProperties().isEmpty()) result.put(TABLE_MESSAGE_COLUMN_MSGPROPERTIES, chatMsg.getMsgProperties());
        result.put(TABLE_MESSAGE_COLUMN_ACTIVETYPE, chatMsg.getActiveType());
        String relatedUsers = chatMsg.getRelatedUsers().toString();
        if (!relatedUsers.isEmpty())  result.put(TABLE_MESSAGE_COLUMN_RELATEDUSERS, relatedUsers);
        result.put(TABLE_MESSAGE_COLUMN_RELATEDMSGID, chatMsg.getRelatedMsgID());
        result.put(TABLE_MESSAGE_COLUMN_ITEMMODEL_ID, chatMsg.getId());
        if(!chatMsg.getName().isEmpty()) result.put(TABLE_MESSAGE_COLUMN_ITEMMODEL_NAME, chatMsg.getName());
        if (!chatMsg.getAvatar().isEmpty()) result.put(TABLE_MESSAGE_COLUMN_ITEMMODEL_AVATAR, chatMsg.getAvatar());
        return result;
    }

    public static ArrayList<ChatMsg> toChatMsg(Cursor cursor) {
        ArrayList<ChatMsg> result = new ArrayList<>();

        while(cursor.moveToNext()) {

            ChatMsg chatMsg = new ChatMsg();

            chatMsg.setMsgStatus(cursor.getInt(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_MSGSTATUS)));

            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_SENDID = "sendid";
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_RECEIVEID = "receiveid";
            Log.v(TAG, "sendid: " + cursor.getLong(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_SENDID)));
            Log.v(TAG, "receiveid: " + cursor.getLong(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_RECEIVEID)));

            //chatMsg.
            chatMsg.setSendTime(cursor.getLong(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_SENDTIME)));
            chatMsg.setMessage(cursor.getString(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_MESSAGE)));
            chatMsg.setMessageType(cursor.getInt(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_MESSAGETYPE)));
            chatMsg.setMessageID(cursor.getLong(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_MESSAGEID)));
            chatMsg.setLastMessageID(cursor.getLong(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_LASTMESSAGEID)));
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_SENDERSESSIONID = "sendersessionid";
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_LIMITRANGE = "limitrange";
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_FORMAT = "format";
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_MSGPROPERTIES = "msgproperties";
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_ACTIVETYPE = "activetype";
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_RELATEDUSERS = "relatedusers";
            //cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_RELATEDMSGID = "relatedmsgid";
            chatMsg.setId(cursor.getLong(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_SENDID)));           //由于setSendID不可写，所以暂时占用此字段进行历史数据显现
            chatMsg.setName(cursor.getString(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_ITEMMODEL_NAME)));
            chatMsg.setAvatar(cursor.getString(cursor.getColumnIndex(TABLE_MESSAGE_COLUMN_ITEMMODEL_AVATAR)));

            result.add(chatMsg);

        }
        return result;
    }
}
