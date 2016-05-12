package com.vrv.litedood.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Group;
import com.vrv.imsdk.model.GroupList;
import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.utils.BaseInfoBean;
import com.vrv.litedood.dto.MessageDTO;
import com.vrv.litedood.ui.activity.MainActivity;
import com.vrv.litedood.ui.activity.MessageActivity;

/**
 * Created by kinee on 2016/4/6.
 */
public class LiteDoodMessageProvider extends ContentProvider {
    private static final String TAG = LiteDoodMessageProvider.class.getSimpleName();

    public static final String PATH_INSERT_SUCCESS = "/insert_success";

    public static final int DB_VERSION = 1;

    public static final int CODE_TABLE_MESSAGE = 1;

    public static UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(LiteDood.AUTHORITY, MessageDTO.TABLE_NAME, CODE_TABLE_MESSAGE);

    }

    private DatabaseHelper dbhelper;

    class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, LiteDood.DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = MessageDTO.getCreateSQL();
            Log.v(TAG, sql);
            db.execSQL(sql);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

    @Override
    public boolean onCreate() {
        dbhelper = new DatabaseHelper(getContext());


        SDKManager.instance().getGroupList().setOperateListener(new GroupList.OnOperateListener() {
            @Override
            public void operate(byte b, Group group) {
                Log.v(TAG, String.valueOf(b));
                switch(b) {
                    case (byte)1:
                        MessageActivity.startMessageActivity(LiteDoodApplication.getMainActivity(), group);

                        break;
                    default:
                        break;
                }
            }
        });
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor result = null;
        switch(uriMatcher.match(uri)) {
            case CODE_TABLE_MESSAGE: {
                result = db.query(MessageDTO.TABLE_NAME, projection, selection, selectionArgs, null, null, MessageDTO.TABLE_MESSAGE_COLUMN_SENDTIME);
                break;
            }
        }

        return result;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return LiteDood.URI + "TEST";
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = null;
        switch (uriMatcher.match(uri)) {
            case CODE_TABLE_MESSAGE:
            {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                long count = db.insert(MessageDTO.TABLE_NAME, MessageDTO.TABLE_MESSAGE_COLUMN_ID, values);
                if (count == 1) {
                    String uriString = LiteDood.URI + PATH_INSERT_SUCCESS;
                    uri = Uri.parse(uriString);

                }
                db.close();
            }
        }
        return uri;
    }

    public static void test() {}


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
