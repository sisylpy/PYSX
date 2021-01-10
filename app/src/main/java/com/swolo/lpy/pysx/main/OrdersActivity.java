package com.swolo.lpy.pysx.main;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.printer.command.EscCommand;
import com.printer.command.LabelCommand;
import com.swolo.lpy.pysx.R;
import com.swolo.lpy.pysx.main.adapter.OrdersViewAdapter;
import com.swolo.lpy.pysx.main.gp.PrinterCommand;
import com.swolo.lpy.pysx.main.gp.ThreadPool;
import com.swolo.lpy.pysx.main.modal.ComuGoods;
import com.swolo.lpy.pysx.main.modal.Orders;
import com.swolo.lpy.pysx.main.presenter.MainContract;
import com.swolo.lpy.pysx.main.presenter.OrdersPresenterImpl;
import com.swolo.lpy.pysx.ui.BaseActivity;
import com.swolo.lpy.pysx.util.ActivityUtil;
import com.swolo.lpy.pysx.util.Utils;
import com.swolo.lpy.pysx.main.DeviceConnFactoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.swolo.lpy.pysx.main.gp.Constant.MESSAGE_UPDATE_PARAMETER;

public class OrdersActivity extends BaseActivity implements MainContract.OrdersView {

    public static Integer CHOOSE_GOODS_RESULT_CODE = 0X33;
    private ComuGoods comuGoods;
    private RecyclerView orderView;
    private TextView goodsName;
    private OrdersViewAdapter ordersViewAdapter;
    private OrdersPresenterImpl ordersPresenter;

    private Button numOne;
    private Button numTwo;
    private Button numThree;
    private Button numFour;
    private Button numFive;
    private Button numSix;
    private Button numSeven;
    private Button numEight;
    private Button numNine;
    private Button numDel;
    private Button numZero;
    private Button numDot;
    private Button numPrint;
    private TextView weighText;

    private Orders mOrders;

    List<Orders> ordersList = new ArrayList<>();

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


    @Override
    protected void initView() {
        orderView = (RecyclerView) findViewById(R.id.order_list);
        goodsName = (TextView) findViewById(R.id.goods_name);
        numOne = (Button) findViewById(R.id.num_one);
        numTwo = (Button) findViewById(R.id.num_two);
        numThree = (Button) findViewById(R.id.num_three);
        numFour = (Button) findViewById(R.id.num_four);
        numFive = (Button) findViewById(R.id.num_five);
        numSix = (Button) findViewById(R.id.num_six);
        numSeven = (Button) findViewById(R.id.num_seven);
        numEight = (Button) findViewById(R.id.num_eight);
        numNine = (Button) findViewById(R.id.num_nine);
        numDel = (Button) findViewById(R.id.num_del);
        numZero = (Button) findViewById(R.id.num_zero);
        numDot = (Button) findViewById(R.id.num_dot);
        numPrint = (Button) findViewById(R.id.num_print);
        weighText = (TextView) findViewById(R.id.weigh_text);
    }

    @Override
    protected void initData() {
        comuGoods = (ComuGoods) getIntent().getSerializableExtra("goods");
        String name = comuGoods.nxCgGoodsName;
        String brand = comuGoods.nxCgGoodsBrand;
        if(brand != null){
            goodsName.setText( "[" + brand + "] " + name );
        }else {
            goodsName.setText(name );
        }

        ordersViewAdapter = new OrdersViewAdapter();
        ordersPresenter = new OrdersPresenterImpl(this);
    }

    @Override
    protected void bindAction() {

        ordersViewAdapter.setOnClickOrderListner(new OrdersViewAdapter.onClickOrderListener() {
            @Override
            public void onItemClick(Orders orders, Integer position) {
                mOrders.nxRoWeight = "-1";
                mOrders = orders;
                ordersViewAdapter.changeItemStatus(position);
                Log.i("clickOrdersss", orders.nxRoQuantity + "====pos:" + position.toString());
            }
        });


        numOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("1");
            }
        });
        numTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("2");
            }
        });
        numThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("3");
            }
        });

        numFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("4");
            }
        });
        numFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("5");
            }
        });
        numSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("6");
            }
        });
        numSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("7");
            }
        });
        numEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("8");
            }
        });
        numNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("9");
            }
        });
        numZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight("0");
            }
        });
        numDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWeight(".");
            }
        });

        numDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mOrders.nxRoWeight.equals("-1")) {
                    mOrders.nxRoWeight = "-1";
                }
                ordersViewAdapter.updateOrders(mOrders);
            }
        });


        numPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mOrders.nxRoWeight.equals("-1")){
                    showLoading();
                    ordersPresenter.printOrder(mOrders.nxRestrauntOrdersId, mOrders.nxRoWeight);
                }else {
                    showToast("请输入重量");
                }
            }
        });


    }

    private void inputWeight(String value) {
        if (!mOrders.nxRoWeight.equals("-1")) {
            if(mOrders.nxRoWeight.length() < 10){

            }
            mOrders.nxRoWeight = mOrders.nxRoWeight + value;
        } else {
            mOrders.nxRoWeight = value;
        }
        ordersViewAdapter.updateOrders(mOrders);
    }


    @Override
    protected void setView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        orderView.setLayoutManager(gridLayoutManager);
        orderView.setAdapter(ordersViewAdapter);

        showLoading();
        ordersPresenter.getOrdersList(comuGoods.nxCommunityGoodsId.toString());
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_order;
    }

    @Override
    public void getOrdersListSuccess(List<Orders> orders) {
        stopLoading();
        if (orders.size() > 0) {
            ordersViewAdapter.setData(orders);

            ordersList = orders;
            mOrders = orders.get(0);
            ordersViewAdapter.changeItemStatus(0);
        }
    }

    @Override
    public void getOrdersListFail(String error) {
        stopLoading();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();

    }

    @Override
    public void printOrderSuccess() {
        stopLoading();
        btnLabelPrint(mOrders);

        ordersViewAdapter.deleteItem(mOrders);
        updateList();

    }


    @Override
    public void printOrderFail(String error) {
        stopLoading();
        showToast(error);
    }

    private void updateList() {

        if (this.ordersList.size() > 0) {

            mOrders = this.ordersList.get(0);
            System.out.println(mOrders.nxRoQuantity + "deltehoude orderweihghhg!!!!!!");
            ordersViewAdapter.changeItemStatus(0);
        } else {
            ActivityUtil.goBackWithResult(OrdersActivity.this, 1, null);

        }
    }


    public void btnLabelPrint(Orders orders) {
        final Orders orders1 = orders;
        threadPool = ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                        !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    mHandler.obtainMessage(CONN_PRINTER).sendToTarget();
                    return;
                }
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    sendLabel(orders1);
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }

    /**
     * 发送标签
     */
    void sendLabel(Orders orders) {
        LabelCommand tsc = new LabelCommand();
        /* 撕纸模式开启 */
        tsc.addTear(EscCommand.ENABLE.ON);
        /* 设置标签尺寸，按照实际尺寸设置 */
        tsc.addSize(40, 60);
        /* 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 */
        tsc.addGap(10);
        /* 设置打印方向 */
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        /* 开启带Response的打印，用于连续打印 */
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        /* 设置原点坐标 */
        tsc.addReference(0, 0);
        /* 清除打印缓冲区 */
        tsc.addCls();
        /* 绘制简体中文 */
        String name = orders.nxCommunityGoodsEntity.nxCgGoodsName;

        tsc.addText(70, 400, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_270, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                name);
        String weight = orders.nxRoWeight + orders.nxCommunityGoodsEntity.nxCgGoodsStandardname;
        tsc.addText(70, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_270, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                weight);

        String brand = orders.nxCommunityGoodsEntity.nxCgGoodsBrand;
        if (brand != null) {
            tsc.addText(130, 400, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_270, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                   "品牌:"+ brand);
        }


        String restrauntNumber = orders.nxRestrauntEntity.nxRestrauntNumber;
        tsc.addText(160, 340, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_270, LabelCommand.FONTMUL.MUL_4, LabelCommand.FONTMUL.MUL_4,
                restrauntNumber);

        String remark = orders.nxRoRemark;
        if (remark != null) {
            tsc.addText(270, 400, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_270, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
                   "备注:" + remark);
        }

        /* 打印标签 */
        tsc.addPrint(1, 1);
        /* 打印标签后 蜂鸣器响 */

        tsc.addSound(2, 100);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        /* 发送数据 */
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
//            Log.d(TAG, "sendLabel: 打印机为空");
            return;
        }
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                        Utils.toast(OrdersActivity.this, getString(R.string.str_disconnect_success));
                    }
                    break;
                case PRINTER_COMMAND_ERROR:
                    Utils.toast(OrdersActivity.this, getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:
                    Utils.toast(OrdersActivity.this, getString(R.string.str_cann_printer));
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
                default:
                    new DeviceConnFactoryManager.Build()
                            /* 设置端口连接方式 */
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                            /* 设置端口IP地址 */
                            .setIp("192.168.2.227")
                            /* 设置端口ID（主要用于连接多设备） */
                            .setId(id)
                            /* 设置连接的热点端口号 */
                            .setPort(9100)
                            .build();
                    threadPool.addTask(new Runnable() {
                        @Override
                        public void run() {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

    }




}
