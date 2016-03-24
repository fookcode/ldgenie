package com.vrv.ldgenie.action;

import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

import com.vrv.imsdk.api.ResponseCode;
import com.vrv.imsdk.model.BaseModel;
import com.vrv.imsdk.model.ResultCallBack;

import java.util.ArrayList;


/**
 * SDK回掉方法处理类
 * Created by Yang on 2015/8/30 030.
 */
public class CallBackHelper {

    private static final String TAG = CallBackHelper.class.getSimpleName();

    public static <T> ResultCallBack buildCallBack(final RequestHandler handler) {

        return new ResultCallBack<T>() {
            @Override
            public void onSuccess(T result) {
                handleResult(RequestHandler.SUCCESS, ResponseCode.RSP_SUCCESS, "", result, handler);
            }

            @Override
            public void onError(int code, String message) {
                handleResult(RequestHandler.FAILURE, code, message, null, handler);
            }
        };
    }

    private static <T> void handleResult(int what, int arg1, String obj, T t, RequestHandler handler) {
        final Message msg = new Message();
        msg.what = what;
        if (arg1 == ResponseCode.RSP_SUCCESS) {
            if (t != null) {
                Bundle bundle = new Bundle();
                if (t instanceof BaseModel) {
                    bundle.putParcelable(RequestHandler.KEY_DATA, (BaseModel) t);
                } else if (t instanceof String) {
                    bundle.putString(RequestHandler.KEY_DATA, (String) t);
                } else if (t instanceof Byte) {
                    bundle.putByte(RequestHandler.KEY_DATA, (Byte) t);
                } else if (t instanceof ArrayList) {
                    bundle.putParcelableArrayList(RequestHandler.KEY_DATA, (ArrayList<? extends Parcelable>) t);
                } else if (t instanceof Long) {
                    bundle.putLong(RequestHandler.KEY_DATA, (Long) t);
                }
                msg.setData(bundle);
            }
        } else {
            msg.arg1 = arg1;
            msg.obj = obj;
        }
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }
}
