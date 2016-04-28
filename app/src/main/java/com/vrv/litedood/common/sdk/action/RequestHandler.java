package com.vrv.litedood.common.sdk.action;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.vrv.litedood.R;


/**
 * {@link CallBackHelper} SDK请求使用
 * Created by Yang on 2015/8/30 030.
 */
public abstract class RequestHandler extends Handler {

    private static final String TAG = RequestHandler.class.getSimpleName();

    public static final String KEY_DATA = "data";
    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;
    public static final int REQUEST_FALSE = -2;
    public static final int SHOW_PRO = 2;
    public static final int DIS_PRO = 3;

    private static AlertDialog dialog;
    private Context context;

    public RequestHandler(){};

    public RequestHandler(Context context) {
        this.context = context;
    };

    public Context getContext() {
        return context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SUCCESS:
                handleSuccess(msg);
                break;
            case REQUEST_FALSE:
                break;
            case FAILURE:
                handleFailure(msg.arg1, String.valueOf(msg.obj));
                break;
            case SHOW_PRO:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                AppCompatActivity activity = (AppCompatActivity)context;
                View view = activity.getLayoutInflater().inflate(R.layout.item_busy_indicator, null);
                dialog = builder.setView(view).show();
                dialog.getWindow().setLayout(160, 160);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                break;
            case DIS_PRO:
                try {
                    if (dialog != null) {
                       dialog.dismiss();
                       dialog = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public abstract void handleSuccess(Message msg);

    public void handleFailure(int code, String message) {
        if ((dialog != null)&&(dialog.isShowing())) {
            dialog.dismiss();
        }
        Toast.makeText(context, code + ":" + message, Toast.LENGTH_SHORT).show();
    }
//
//    public static class BusyIndicatorDialog extends DialogFragment {
//        private static AlertDialog dialog;
//        public BusyIndicatorDialog() {
//            super();
//        }
//
//        @Override
//        public Dialog getDialog() {
//            return dialog;
//        }
//
//        @NonNull
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            View view = getActivity().getLayoutInflater().inflate(R.layout.item_busy_indicator, null);
//           // Log.v(TAG, String.valueOf(params.height) + "   " + String.valueOf(params.width));
//            builder.setView(view);
//
//            dialog = builder.create();
//            return dialog;
//        }
//
//    }
}
