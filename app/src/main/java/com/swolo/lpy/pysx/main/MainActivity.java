package com.swolo.lpy.pysx.main;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.swolo.lpy.pysx.R;
import com.swolo.lpy.pysx.main.adapter.FatherGoodsViewAdapter;
import com.swolo.lpy.pysx.main.gp.Constant;
import com.swolo.lpy.pysx.main.gp.ThreadPool;
import com.swolo.lpy.pysx.main.gp.UsbListAcitivity;
import com.swolo.lpy.pysx.main.modal.ComuFatherGoods;
import com.swolo.lpy.pysx.main.presenter.ComuFatherGoodsPresenterImpl;
import com.swolo.lpy.pysx.main.presenter.MainContract;
import com.swolo.lpy.pysx.ui.BaseActivity;
import com.swolo.lpy.pysx.util.ActivityUtil;
import com.swolo.lpy.pysx.util.Utils;
import com.swolo.lpy.pysx.main.DeviceConnFactoryManager;
import com.swolo.lpy.pysx.main.GoodsActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.swolo.lpy.pysx.main.DeviceConnFactoryManager.ACTION_CONN_STATE;
import static com.swolo.lpy.pysx.main.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static com.swolo.lpy.pysx.main.GoodsActivity.CHOOSE_FATHER_GOODS_RESULT_CODE;
import static com.swolo.lpy.pysx.main.gp.Constant.ACTION_USB_PERMISSION;
import static com.swolo.lpy.pysx.main.gp.Constant.MESSAGE_UPDATE_PARAMETER;


//AppCompatActivity
//
public class MainActivity extends  BaseActivity implements MainContract.FatherGoodsView{

    private RecyclerView listView;
    private Button rcBtn;
    private Button ckBtn;
    private TextView tvConnState;
    private List<ComuFatherGoods> comuFatherGoodsList;
    private ComuFatherGoodsPresenterImpl fatherGoodsPresenter;
    private FatherGoodsViewAdapter fatherGoodsViewAdapter;
    private ComuFatherGoods mCurComuFatherGoods;
    private String type = "1";
    private Boolean isUpdate = false;

    long waitTime = 2000;
    long touchTime = 0;


//    ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    private static String TAG = "MainActivityMainActivity";

    private String usbName;
    private PendingIntent		mPermissionIntent;



    private int id = 0;
    private ThreadPool threadPool;


    /**
     * 连接状态断开
     */
    private static final int CONN_STATE_DISCONN = 0x007;

    /**
     * 使用打印机指令错误
     */
    private static final int PRINTER_COMMAND_ERROR = 0x008;
    private static final int CONN_PRINTER = 0x12;
    private static final String DEBUG_TAG = "DeviceListActivity";
    private int		printcount	= 0;
    private boolean		continuityprint = false;

    private int			counts;


    private UsbManager usbManager;

    private UsbDevice myUsbDevice;
    private Button btnConn;



    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter( ACTION_USB_PERMISSION );
        filter.addAction( ACTION_USB_DEVICE_DETACHED );
        filter.addAction( ACTION_QUERY_PRINTER_STATE );
        filter.addAction( ACTION_CONN_STATE );
        filter.addAction( ACTION_USB_DEVICE_ATTACHED );
        registerReceiver( receiver, filter );

        usbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
        //2)获取到所有设备 选择出满足的设备
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        StringBuilder sb = new StringBuilder();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            if (device.getVendorId() == 26728 && device.getProductId() == 1280) {
                usbConn( device );
            }
        }
    }

    public void btnUsbConn(View view){
        startActivityForResult(new Intent(MainActivity.this, UsbListAcitivity.class), Constant.USB_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constant.USB_REQUEST_CODE){
                closeport();
                usbName = data.getStringExtra(UsbListAcitivity.USB_NAME);
                /* 通过USB设备名找到USB设备 */
                UsbDevice usbDevice = Utils.getUsbDeviceFromName( MainActivity.this, usbName );
                /* 判断USB设备是否有权限 */
                if ( usbManager.hasPermission( usbDevice ) )
                {
                    usbConn( usbDevice );
                    Log.i(TAG, "usb connnet,,,,");
                } else {        /* 请求权限 */
                    mPermissionIntent = PendingIntent.getBroadcast( this, 0, new Intent( ACTION_USB_PERMISSION ), 0 );
                    usbManager.requestPermission( usbDevice, mPermissionIntent );
                }
            }

        }
        if(requestCode == CHOOSE_FATHER_GOODS_RESULT_CODE){
            if(resultCode==1){//resultCode需要与SecondActivity中的resultCode一致
                //获取返回数据，获取用户名
                showLoading();
                fatherGoodsPresenter.getFatherGoodsList("1", type);
            }
        }


    }




    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action)
            {
                case ACTION_USB_PERMISSION:
                    Toast.makeText(MainActivity.this, "1--ACTION_USB_PERMISSION", Toast.LENGTH_LONG).show();

                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra( UsbManager.EXTRA_DEVICE );
                        if ( intent.getBooleanExtra( UsbManager.EXTRA_PERMISSION_GRANTED, false ) )
                        {
                            if ( device != null )
                            {
                                System.out.println( "permission ok for device " + device );
                                usbConn( device );
                            }
                        } else {
                            System.out.println( "permission denied for device " + device );
                        }
                    }
                    break;

                case ACTION_USB_DEVICE_DETACHED: //usb 拔出
                    Toast.makeText(MainActivity.this, "2--ACTION_USB_DEVICE_DETACHED", Toast.LENGTH_LONG).show();
                    mHandler.obtainMessage( CONN_STATE_DISCONN ).sendToTarget();

                    break;

                case ACTION_QUERY_PRINTER_STATE:
                    Toast.makeText(MainActivity.this, "3--ACTION_QUERY_PRINTER_STATE", Toast.LENGTH_LONG).show();
                    break;

                case ACTION_USB_DEVICE_ATTACHED: //
                    tvConnState.setText( getString( R.string.str_conn_state_disconnect ) );
                    Toast.makeText(MainActivity.this, "4321--ACTION_USB_DEVICE_ATTACHED", Toast.LENGTH_LONG).show();
                    //1)创建usbManager
                    usbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
                    //2)获取到所有设备 选择出满足的设备
                    HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
                    Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                    StringBuilder sb = new StringBuilder();
                    while(deviceIterator.hasNext()){
                        UsbDevice device = deviceIterator.next();
                        if (device.getVendorId() == 26728 && device.getProductId() == 1280) {
                            usbConn( device );
                        }
                    }
                    break;

                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    Toast.makeText(MainActivity.this, "5--ACTION_QUERY_PRINTER_STATE", Toast.LENGTH_LONG).show();

                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state){
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if(id == deviceId){
                                tvConnState.setText( getString( R.string.str_conn_state_disconnect ) );
                                btnConn.setVisibility(View.VISIBLE);

                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            tvConnState.setText( getString( R.string.str_conn_state_connecting ) );
                            btnConn.setVisibility(View.VISIBLE);
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            tvConnState.setText( getString( R.string.str_conn_state_connected ));
                            btnConn.setVisibility(View.GONE);

                            break;
                        case DeviceConnFactoryManager.CONN_STATE_FAILED:
                            Utils.toast( MainActivity.this, getString( R.string.str_conn_fail ) );
                            /* wificonn=false; */
                            tvConnState.setText( getString( R.string.str_conn_state_disconnect ) );
                            btnConn.setVisibility(View.VISIBLE);

                            break;
                        default:
                            break;

                    }

                default:
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                        Utils.toast(MainActivity.this, getString(R.string.str_disconnect_success));
                    }
                    break;
                case PRINTER_COMMAND_ERROR:
                    Utils.toast(MainActivity.this, getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:
                    Utils.toast(MainActivity.this, getString(R.string.str_cann_printer));
                    break;
                case MESSAGE_UPDATE_PARAMETER:
                    String strIp = msg.getData().getString("Ip");
                    String strPort = msg.getData().getString("Port");
                    /* 初始化端口信息 */
                    new DeviceConnFactoryManager.Build()
                            /* 设置端口连接方式 */
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                            /* 设置端口IP地址 */
                            .setIp(strIp)
                            /* 设置端口ID（主要用于连接多设备） */
                            .setId(id)
                            /* 设置连接的热点端口号 */
                            .setPort(Integer.parseInt(strPort))
                            .build();
                    threadPool = ThreadPool.getInstantiation();
                    threadPool.addTask(new Runnable() {
                        @Override
                        public void run() {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    });
                    break;
//                default:
//                    new DeviceConnFactoryManager.Build()
//                            /* 设置端口连接方式 */
//                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
//                            /* 设置端口IP地址 */
//                            .setIp("192.168.2.227")
//                            /* 设置端口ID（主要用于连接多设备） */
//                            .setId(id)
//                            /* 设置连接的热点端口号 */
//                            .setPort(9100)
//                            .build();
//                    threadPool.addTask(new Runnable() {
//                        @Override
//                        public void run() {
//                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
//                        }
//                    });
//                    break;
            }
        }
    };



    /**
     * usb连接
     *
     * @param usbDevice
     */
    private void usbConn( UsbDevice usbDevice )
    {
        new DeviceConnFactoryManager.Build()
                .setId( id )
                .setConnMethod( DeviceConnFactoryManager.CONN_METHOD.USB )
                .setUsbDevice( usbDevice )
                .setContext( this )
                .build();
        Log.d(TAG, "id ---shi duoshao====" + id);
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver( receiver );
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

//        Log.e( TAG, "onDestroy()" );
        DeviceConnFactoryManager.closeAllPort();
        if ( threadPool != null )
        {
            threadPool.stopThreadPool();
            threadPool = null;
        }
    }
    /**
     * 重新连接回收上次连接的对象，避免内存泄漏
     */
    private void closeport()
    {
        if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null &&DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort != null )
        {
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].reader.cancel();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort.closePort();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort = null;
        }
    }



//    |||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    @Override
    protected void initView() {
        rcBtn = (Button) findViewById(R.id.rc_btn);
        ckBtn = (Button) findViewById(R.id.ck_btn);
        listView = this.findViewById(R.id.recycler_view);
        tvConnState	= (TextView) findViewById( R.id.tv_connState );
        btnConn = (Button) findViewById(R.id.btn_conn);


    }




    @Override
    protected void initData() {
        usbManager = (UsbManager) getSystemService( Context.USB_SERVICE );
        fatherGoodsViewAdapter = new FatherGoodsViewAdapter();
        fatherGoodsPresenter = new ComuFatherGoodsPresenterImpl(this);
    }


    @Override
    protected void bindAction() {
       fatherGoodsViewAdapter.setOnClickFatherGoodsListener(new FatherGoodsViewAdapter.OnFatherGoodsClickListener() {
           @Override
           public void onItemClick(ComuFatherGoods comuFatherGoods) {
               Bundle bundle = new Bundle();
               bundle.putSerializable("comuFatherGoods", comuFatherGoods);
               bundle.putString("type", type);
               ActivityUtil.next(MainActivity.this, GoodsActivity.class,bundle, CHOOSE_FATHER_GOODS_RESULT_CODE);

           }
       });
       rcBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               type = "1";
               Log.i(TAG, "type -----"+type);
               showLoading();
               fatherGoodsPresenter.getFatherGoodsList("1",type);
               rcBtn.setBackgroundResource(R.drawable.index_btn_sel);
               rcBtn.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
               ckBtn.setBackgroundResource(R.drawable.index_btn);
               ckBtn.setTextColor(MainActivity.this.getResources().getColor(R.color.colorPrimary));

           }
       });

        ckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "2";
                Log.i(TAG, "type -----"+type);
                showLoading();
                fatherGoodsPresenter.getFatherGoodsList("1", type);
                rcBtn.setBackgroundResource(R.drawable.index_btn);
                rcBtn.setTextColor(MainActivity.this.getResources().getColor(R.color.colorPrimary));
                ckBtn.setBackgroundResource(R.drawable.index_btn_sel);
                ckBtn.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
            }
        });

    }


    @Override
    protected void setView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(fatherGoodsViewAdapter);
        showLoading();
        fatherGoodsPresenter.getFatherGoodsList("1", type);
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_main;
    }


    @Override
    public void getFatherGoodsListSuccess(List<ComuFatherGoods> comuFatherGoods) {
        stopLoading();
        fatherGoodsViewAdapter.setData(comuFatherGoods);
        if(comuFatherGoods.size() > 0){
            isUpdate = true;
        }

    }

    @Override
    public void getFatherGoodsListFail(String error) {
        stopLoading();
        showToast(error);
        Log.i("meiyoudhsangp", isUpdate.toString());
        if(isUpdate){
            System.out.println("ininiinininin?");
            fatherGoodsViewAdapter.setEmpty();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if((currentTime-touchTime)>=waitTime) {
                //让Toast的显示时间和等待时间相同
                Toast.makeText(this, "再按一次退出", (int)waitTime).show();
                touchTime = currentTime;
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
