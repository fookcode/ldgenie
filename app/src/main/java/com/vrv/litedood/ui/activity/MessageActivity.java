package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Toast;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Chat;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.imsdk.model.ChatMsgList;
import com.vrv.imsdk.model.Contact;
import com.vrv.imsdk.model.Group;
import com.vrv.imsdk.model.ItemModel;
import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.MessageAdapter;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.ui.activity.MessageAttachmentFragment.MessageFaceFragment;
import com.vrv.litedood.ui.activity.MessageAttachmentFragment.MessageImageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageActivity extends AppCompatActivity {
    private static final String TAG = MessageActivity.class.getSimpleName();

    public static final String ID_MESSAGE_TYPE = "ID_MESSAGE_TYPE";
    public static final String ID_USER_ID = "USER_ID";
    public static final String ID_USER_NAME = "USER_NAME";
    public static final String ID_USER_AVATAR = "USER_AVATAR";
    public static final String ID_LAST_MESSAGE_ID = "LAST_MESSAGE_ID";
    public static final String ID_UNREAD_MESSAGE_NUMBER = "UNREAD_NUMBER";


    private static final int TYPE_HANDLER_GET_HISTORY_MESSAGE = 1;
    private static final int TYPE_HANDLER_SEND_MESSAGE = 2;
    private static final int TYPE_HANDLER_GET_GROUP = 3;
    private static final int TYPE_HANDLER_GET_GROUP_MEMBER = 4;
    private static final int TYPE_HANDLER_LOAD_HISTORY_MESSAGE = 5;

    public static final int TYPE_MESSAGE_CHAT = 1;
    public static final int TYPE_MESSAGE_GROUP = 2;
    public static final int TYPE_MESSAGE_UNKNOWN = 0;

    private static final int DEFAULT_MESSAGE_COUNT = 15;

    private static final int FRAGMENT_ITEM_FACE = 0;
    private static final int FRAGMENT_ITEM_IMAGE = 1;

    private static List<Contact> mMemberContacts = null;         //两个表态变量，临时用作处理Group中的发言人名字
    private static Group mGroup = null;
    private static long mPrevGroupID = 0;

    private Toolbar mToolbarMessage;
    private List<ChatMsg> mChatMsgQueue = new ArrayList<>();
    private ChatMsgList mChatMsgList;
    private ListViewCompat mListViewMessage;

    private MessageAdapter mMessageAdapter;

    private boolean isGettingHistoryMessage = false;

    private ContentResolver mLiteDoodContentResolver;

    public static <T extends ItemModel> void startMessageActivity(Activity activity, T item) {
        Intent intent = new Intent();
        //intent.putExtra(ID_USER_INFO, BaseInfoBean.chat2BaseInfo(chat));
        intent.putExtra(ID_USER_ID, item.getId());
        intent.putExtra(ID_USER_NAME, item.getName());
        if (item instanceof Chat) {
            intent.putExtra(ID_LAST_MESSAGE_ID, ((Chat)item).getLastMsgID());
            intent.putExtra(ID_UNREAD_MESSAGE_NUMBER, ((Chat)item).getUnReadNum());
            intent.putExtra(ID_MESSAGE_TYPE, (int)((Chat) item).getType());  //type=1个人消息 type=2群消息
            intent.putExtra(ID_USER_AVATAR, ((Chat)item).getAvatar());
        }
        else if (item instanceof Group){
            intent.putExtra(ID_MESSAGE_TYPE, TYPE_MESSAGE_GROUP);
        }
        else intent.putExtra(ID_MESSAGE_TYPE, TYPE_MESSAGE_UNKNOWN);
        intent.setClass(activity, MessageActivity.class);
        activity.startActivity(intent);
        if (!(activity instanceof MainActivity)) {
            activity.finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        mLiteDoodContentResolver = getContentResolver();

        initToolbar();
        initMessageData();
        initAttachmentAction();
    }

    private void initToolbar() {
        mToolbarMessage = (Toolbar)findViewById(R.id.toolbarMessage);
        mToolbarMessage.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbarMessage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra(ID_USER_NAME));

    }

    private void initMessageData() {

        mMessageAdapter = new MessageAdapter(MessageActivity.this, mChatMsgQueue);
        mListViewMessage = (ListViewCompat)findViewById(R.id.listMessage);
        mListViewMessage.setOnScrollListener(new AbsListView.OnScrollListener() {
            final ContentLoadingProgressBar indicator = (ContentLoadingProgressBar) findViewById(R.id.clpbMessageHistoryLoading);
            private int mPrevPos;
            int posAwayTop =1;
            private boolean ListIsAtTop()   {
                if(mListViewMessage.getChildCount() == 0) return true;
                return mListViewMessage.getChildAt(0).getTop() == 0;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {//scrollstate 1:开始滚动 2:正在滚动;0:停止
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (isGettingHistoryMessage) return;
                    indicator.setVisibility(View.VISIBLE);

                    posAwayTop = mListViewMessage.getChildAt(0).getTop();

                    if (ListIsAtTop() && (mPrevPos <= posAwayTop)) {
                        if (!isGettingHistoryMessage) {
                            isGettingHistoryMessage = true;

                            RequestHelper.getChatHistory(MessageActivity.this.getIntent().getLongExtra(ID_USER_ID, 0),
                                    ((ChatMsg) mListViewMessage.getItemAtPosition(mListViewMessage.getCount() - 1)).getMessageID(),
                                    DEFAULT_MESSAGE_COUNT + mListViewMessage.getCount(),
                                    new MessageRequestHandler(TYPE_HANDLER_LOAD_HISTORY_MESSAGE));
                        }

                    } else {
                        indicator.setVisibility(View.GONE);
                    }
                    mPrevPos = posAwayTop;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.v(TAG, "firstVisibleItem:" + String.valueOf(firstVisibleItem) +
//                            " visibleItemCount: " + String.valueOf(visibleItemCount) +
//                            " totalItemCount : " + String.valueOf(totalItemCount));

            }
        });
//        mListViewMessage.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.v(TAG, "scrollX" + String.valueOf(scrollX) +
//                        " scrollY" + String.valueOf(scrollY) +
//                        " oldScrollX" + String.valueOf(oldScrollX) +
//                        " oldScrollY" + String.valueOf(oldScrollY) );
//            }
//        });


        mListViewMessage.setAdapter(mMessageAdapter);

        if (getIntent().getIntExtra(ID_MESSAGE_TYPE, 0) == TYPE_MESSAGE_GROUP) {           //如果是群，由于chatMsg中没有给name字段赋值，所以先取回群中所有人员放入静态变量，用作adapter展现时获取发言人名字，在成员名称获取成功后setMessageHistory(异步handler中)
            long groupID = getIntent().getLongExtra(ID_USER_ID, 0);
            if ((mPrevGroupID != groupID)) {
                RequestHelper.getGroupInfo(getIntent().getLongExtra(ID_USER_ID, 0), new MessageRequestHandler(TYPE_HANDLER_GET_GROUP));
                mPrevGroupID = groupID;
            }
            else setMessageHistory();
        }
        else setMessageHistory();                 //如果是点对点就直接取消息，不存在群友名称问题

        mChatMsgList = SDKManager.instance().getChatMsgList();
        mChatMsgList.setReceiveListener(getIntent().getLongExtra(ID_USER_ID, 0), new ChatMsgList.OnReceiveChatMsgListener() {
            @Override
            public void onReceive(ChatMsg msg) {
                if (msg == null) return;
                if((msg.getTargetID() == getIntent().getLongExtra(ID_USER_ID, 0)) && (mChatMsgQueue != null)) {
                    mChatMsgQueue.add(msg);
                    RequestHelper.setMsgRead(msg.getTargetID(), msg.getMessageID());
                    mMessageAdapter.notifyDataSetChanged();
                    mListViewMessage.setSelection(mListViewMessage.getCount() -1);
                    //saveMessageToDB(chatMsg);
                }
            }

            @Override
            public void onUpdate(ChatMsg msg) {

                if (msg == null) {
                    return;
                }
                int size = mChatMsgQueue.size();
                Log.v(TAG, "in Update, MsgQueueSize: " + String.valueOf(size));
                for (int i = size - 1; i >= 0; i--) {
                    if (msg.getLocalID() == mChatMsgQueue.get(i).getLocalID()) {
                        mChatMsgQueue.set(i, msg);
                        mMessageAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        });

//        //setMessageHistory(userInfo.getID(), getIntent().getLongExtra(ID_LAST_MESSAGE_ID, -1));
//        //获取未读记录
//        int count = getIntent().getIntExtra(ID_UNREAD_MESSAGE_NUMBER, 0);
//        RequestHelper.getChatHistory(getIntent().getLongExtra(ID_USER_ID, 0),
//                0,
//                getShowedMessageCount(count),
//                new MessageRequestHandler(TYPE_HANDLER_GET_HISTORY_MESSAGE));

        final AppCompatEditText edtMessage = (AppCompatEditText)findViewById(R.id.edtMessage);
        edtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.vpMessageAttachment).setVisibility(View.GONE);
            }
        });
        final AppCompatButton btnSendMessage = (AppCompatButton)findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = edtMessage.getText().toString();
                if (!txt.isEmpty()) {
                    RequestHelper.sendTxt(getIntent().getLongExtra(ID_USER_ID, 0), txt, null, new MessageRequestHandler(TYPE_HANDLER_SEND_MESSAGE));
                    edtMessage.getText().clear();
                    mListViewMessage.setSelection(mListViewMessage.getCount() -1);
                }
            }
        });



    }

    private void initAttachmentAction() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        final ViewPager pager = (ViewPager)findViewById(R.id.vpMessageAttachment);
        MessageFaceFragment messageFaceFragment = new MessageFaceFragment();
        MessageImageFragment messageImageFragment = new MessageImageFragment();
        messageImageFragment.setParam(getIntent().getLongExtra(ID_USER_ID, 0), this);

        fragments.add(messageFaceFragment);
        fragments.add(messageImageFragment);
        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.add(messageFaceFragment, "FACE");
//        transaction.add(messageImageFragment, "IMAGE").hide(messageImageFragment);
//        transaction.commit();
        pager.setAdapter(new MessageAttachmentPagerAdapter(fm, fragments));

        AppCompatImageView btnFace = (AppCompatImageView)findViewById(R.id.btnMessageFace);
        btnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getVisibility() != View.VISIBLE) {
                    InputMethodManager inputMethodService = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodService.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    MessageActivity.this.findViewById(R.id.vpMessageAttachment).setVisibility(View.VISIBLE);
                    pager.setCurrentItem(0);
                }
                else {
                    if (pager.getCurrentItem() != FRAGMENT_ITEM_FACE) {pager.setCurrentItem(FRAGMENT_ITEM_FACE);}
                    else pager.setVisibility(View.GONE);
                }
            }
        });
        AppCompatImageView btnImage = (AppCompatImageView)findViewById(R.id.btnMessageImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getVisibility() != View.VISIBLE) {
                    InputMethodManager inputMethodService = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodService.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    MessageActivity.this.findViewById(R.id.vpMessageAttachment).setVisibility(View.VISIBLE);
                    pager.setCurrentItem(FRAGMENT_ITEM_IMAGE);
                }
                else {
                    if (pager.getCurrentItem() != FRAGMENT_ITEM_IMAGE) {pager.setCurrentItem(FRAGMENT_ITEM_IMAGE);}
                    else  pager.setVisibility(View.GONE);
                }

            }
        });

    }

    class MessageAttachmentPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager mFragmentManager;
        private ArrayList<Fragment> mFragmentList;

        public MessageAttachmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mFragmentManager = fm;
            mFragmentList = fragments;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
    }


    public boolean isScrollMessageListView() {
        boolean result = false;
        final LinearLayoutCompat rootLayout = (LinearLayoutCompat)findViewById(R.id.llActivityMessageRootLayout);
        int screenHeight = rootLayout.getRootView().getHeight();

        Rect rectangle= new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int contentScreenHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight() + rectangle.top;

        if (contentScreenHeight < screenHeight) {
            result = true;
        }
        return  result;
    }

    private void setMessageHistory() {
        //setMessageHistory(userInfo.getID(), getIntent().getLongExtra(ID_LAST_MESSAGE_ID, -1));
        //获取未读记录
        int count = getIntent().getIntExtra(ID_UNREAD_MESSAGE_NUMBER, 0);
        RequestHelper.getChatHistory(getIntent().getLongExtra(ID_USER_ID, 0),
                0,
                getShowedMessageCount(count > DEFAULT_MESSAGE_COUNT ? DEFAULT_MESSAGE_COUNT : count),
                new MessageRequestHandler(TYPE_HANDLER_GET_HISTORY_MESSAGE));
    }

    private int getShowedMessageCount(int unReadCount) {
        return unReadCount == 0 ? DEFAULT_MESSAGE_COUNT : unReadCount;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除消息点对点接收监听函数，否则此Activity就算消毁，过程依然继续后台执行
        mChatMsgList.setReceiveListener(-99, new ChatMsgList.OnReceiveChatMsgListener(){
            @Override
            public void onReceive(ChatMsg chatMsg) {

            }

            @Override
            public void onUpdate(ChatMsg chatMsg) {

            }
        });
    }

    /*private void setMessageHistory(long targetID) {
        String uriString = LiteDoodMessageProvider.SCHEME + LiteDoodMessageProvider.AUTHORITY + "/" + MessageDTO.TABLE_NAME;
        Uri uri = Uri.parse(uriString);
        Log.v(TAG, "receive ID: " + String.valueOf(receiveID));
        Cursor temp = mLiteDoodContentResolver.query(uri, MessageDTO.getAllColumns(), null, null, null);
        ArrayList<ChatMsg> tempList = MessageDTO.toChatMsg(temp);


        Cursor chatMsgHistory = mLiteDoodContentResolver.query(uri,
                MessageDTO.getAllColumns(),
                MessageDTO.TABLE_MESSAGE_COLUMN_RECEIVEID
                    + "=? or "
                    + MessageDTO.TABLE_MESSAGE_COLUMN_SENDID
                    + "=? ", new String[] {String.valueOf(targetID), String.valueOf(targetID)},
                null);
        ArrayList<ChatMsg> oldMsg = MessageDTO.toChatMsg(chatMsgHistory);

        for(ChatMsg msg : oldMsg) {
            Log.v(TAG, msg.toString() + " id:" + msg.getId());
        }
        if ((oldMsg != null) && (oldMsg.size() > 0)) {
            mChatMsgQueue.addAll(oldMsg);
            mMessageAdapter.notifyDataSetChanged();
        };
    }*/

//    private void saveMessageToDB(ChatMsg chatMsg) {
//
//        String uriString = LiteDood.URI + "/" + MessageDTO.TABLE_NAME;
//        Uri insertUri = Uri.parse(uriString);
//        mLiteDoodContentResolver.insert(insertUri, MessageDTO.convertChatMessage(chatMsg));
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                MainActivity.startMainActivity(this);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public static String getMemberName(long id) {
        String result = "";
        if (mMemberContacts == null) return result;
        long myid = LiteDoodApplication.getAppContext().getMyself().getId();
        if (myid != id) {
            for (Contact item : mMemberContacts) {
                if (item.getId() == id) {
                    result = item.getName();
                    break;
                }
            }

        }
        return  result;

    }

    public static String getMemberAvatar(long id) {
        String result = "";
        if (mMemberContacts == null) return result;
        long myid = LiteDoodApplication.getAppContext().getMyself().getId();
        if (myid != id) {
            for (Contact item : mMemberContacts) {
                if (item.getId() == id) {
                    result = item.getAvatar();
                    break;
                }
            }

        }
        return  result;

    }

//    public void choosePic()
//    {
//
//        final CharSequence[] items = {"拍照上传", "从相册上传", "取消"};
//
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("添加相片")
//                .setItems(items, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                        int RESULT_OK_CAMERA = 1;
//                        int RESULT_OK_ALBUM = 2;
//
//                        if(item == 0)  //拍照
//                        {
//                            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
//                            startActivityForResult(getImageByCamera, RESULT_OK_CAMERA);
//                        }
//                        else if(item == 1)//相册
//                        {
//                            Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
//                            getImage.addCategory(Intent.CATEGORY_OPENABLE);
//                            getImage.setType("image/jpeg");
//                            startActivityForResult(getImage, RESULT_OK_ALBUM);
//                        }
//                        else
//                        {
//                            dialog.dismiss();
//                        }
//
//
//                    }
//                }).create();
//        dialog.show();
//    }

    public class MessageRequestHandler extends RequestHandler {
        private int nType;

        public MessageRequestHandler(int type) {
            this.nType = type;
        }
        @Override
        public void handleSuccess(Message msg) {
            switch (nType){
                case TYPE_HANDLER_GET_HISTORY_MESSAGE:
                    ArrayList<ChatMsg> chatMsgArray = msg.getData().getParcelableArrayList("data");
                    if (chatMsgArray.size() > 0) {
                        mChatMsgQueue.clear();
                        mChatMsgQueue.addAll(chatMsgArray);
                        mMessageAdapter.notifyDataSetChanged();
                        mListViewMessage.setSelection(chatMsgArray.size() -1);
                        ChatMsg lastMsg = chatMsgArray.get(chatMsgArray.size()-1);
                        RequestHelper.setMsgRead(lastMsg.getTargetID(), lastMsg.getMessageID());
                    }
                    break;
                case TYPE_HANDLER_SEND_MESSAGE:
                    Log.v(TAG, msg.toString());
                    break;
                case TYPE_HANDLER_GET_GROUP:
                    mGroup = msg.getData().getParcelable("data");
                    if (mGroup != null)
                        RequestHelper.getGroupMembers(mGroup, new MessageRequestHandler(TYPE_HANDLER_GET_GROUP_MEMBER));
                    break;
                case TYPE_HANDLER_GET_GROUP_MEMBER:
                    mMemberContacts = msg.getData().getParcelableArrayList("data");
                    setMessageHistory();
                    break;
                case TYPE_HANDLER_LOAD_HISTORY_MESSAGE:
                    ArrayList<ChatMsg> nextChatMsgArray = msg.getData().getParcelableArrayList("data");
                    if (nextChatMsgArray.size() > mChatMsgQueue.size()) {
                        mChatMsgQueue.clear();
                        mChatMsgQueue.addAll(nextChatMsgArray);
                        mMessageAdapter.notifyDataSetChanged();
                        //mListViewMessage.smoothScrollToPosition(DEFAULT_MESSAGE_COUNT );

                    }
                    else {
                        Toast.makeText(MessageActivity.this, "没有更多的记录了", Toast.LENGTH_SHORT).show();
                    }
                    isGettingHistoryMessage = false;
                    ContentLoadingProgressBar clpbHistory = (ContentLoadingProgressBar)MessageActivity.this.findViewById(R.id.clpbMessageHistoryLoading);
                    clpbHistory.setVisibility(View.GONE);
                    break;

            }
        }

        @Override
        public void handleFailure(int code, String message) {
            super.handleFailure(code, message);
            switch (nType) {
                case TYPE_HANDLER_LOAD_HISTORY_MESSAGE:
                    ContentLoadingProgressBar clpbHistory = (ContentLoadingProgressBar)MessageActivity.this.findViewById(R.id.clpbMessageHistoryLoading);
                    clpbHistory.setVisibility(View.GONE);
                    isGettingHistoryMessage = false;
                    break;
            }
        }
    }
}
