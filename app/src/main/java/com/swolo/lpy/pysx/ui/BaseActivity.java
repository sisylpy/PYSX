package com.swolo.lpy.pysx.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.swolo.lpy.pysx.R;
import com.swolo.lpy.pysx.eventbus.EventCenter;
import com.swolo.lpy.pysx.ui.view.CommonLoadingDialog;
import com.swolo.lpy.pysx.util.ActivityUtil;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by hezhisu on 2016/12/20.
 */

public abstract class BaseActivity extends FragmentActivity {

    private CommonLoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.pushActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewRes());

        EventBus.getDefault().register(this);

        mLoadingDialog = new CommonLoadingDialog(this);
        initView();
        initData();
        bindAction();
        setView();
    }

    public void showToast(String messsage){
        Toast.makeText(this,messsage,Toast.LENGTH_SHORT).show();
    }

    public void showLoading(){
        if(mLoadingDialog != null && !mLoadingDialog.isShowing()){
            mLoadingDialog.show();
        }
    }

    public void stopLoading(){
        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }

    public void setTitle(String title){
        TextView mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText(title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.goBack(BaseActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.pushActivity(this);
    }

    /**
     * 监听返回键按下实现Activity退出过渡动画
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            android.support.v4.app.FragmentManager fManager = getSupportFragmentManager();
            if (null != fManager && fManager.getBackStackEntryCount() >= 1)
                fManager.popBackStack();
            else //back activity
                ActivityUtil.goBack(this);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void bindAction();
    protected abstract void setView();
    protected abstract int getContentViewRes();

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            onEventComming(eventCenter);
        }
    }

    /**
     * when event comming
     *
     * @param eventCenter
     */
    protected void onEventComming(EventCenter eventCenter) {
        int event = eventCenter.getEventCode();
        switch (event) {
            case EventCenter.EVENTCODE_CLOSE_ALL_ACTIVITY:
                finish();
                break;
        }
    }

}
