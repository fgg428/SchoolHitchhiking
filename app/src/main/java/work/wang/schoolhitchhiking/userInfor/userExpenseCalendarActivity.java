package work.wang.schoolhitchhiking.userInfor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.adapter.bill_adapter;
import work.wang.schoolhitchhiking.adapter.dg_adapter;
import work.wang.schoolhitchhiking.bill.bill;
import work.wang.schoolhitchhiking.exchange.gameActivity;
import work.wang.schoolhitchhiking.order_object.dg;
import work.wang.schoolhitchhiking.show.userActivity;

public class userExpenseCalendarActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView bill_back,bill_game;
    private TextView bill_type,bill_income,bill_pay,bill_addUp;
    private ListView bill_lv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler mHandler;
    private List<bill> billList;
    private String sort[]={"综合","微信充值","支付宝充值","积分兑换","饭堂带饭","快递代拿","商品代购","订单回退","订单赏金","币值提现"};
    private String type="类型:综合";
    private int income=0,pay=0,addUp=0;
    private int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_expense_calendar);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        userID=sp.getInt("userID",1);
        init();
        initEvent();
        attainBill();
        updateUI();
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        income=0;
                        pay=0;
                        for(bill b:billList){
                            if(b.getType()==0){
                                income+=b.getNumber();
                            }
                            if(b.getType()==1){
                                pay+=b.getNumber();
                            }
                        }
                        bill_income.setText("+"+income);
                        bill_pay.setText("-"+pay);
                        if(income>pay){
                            addUp=income-pay;
                            bill_addUp.setText("+"+addUp);
                            bill_addUp.setTextColor(Color.parseColor("#00FF00"));
                        }else {
                            addUp=pay-income;
                            bill_addUp.setText("-"+addUp);
                            bill_addUp.setTextColor(Color.parseColor("#EE0000"));
                        }
                        bill_lv.setAdapter(null);
                        bill_adapter dg=new bill_adapter(userExpenseCalendarActivity.this,billList);
                        bill_lv.setAdapter(dg);
                        break;
                    case 2:
                        Toast.makeText(userExpenseCalendarActivity.this,"没有数据刷新试试",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void attainBill(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",userID+"")
                        .add("type",type)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/billInfor")
                        .post(formBody)
                        .build();
                Response response;
                try {
                    Gson gson=new Gson();
                    response=client.newCall(request).execute();
                    String jsons=response.body().string();
                    //Json的解析类对象
                    JsonParser parser = new JsonParser();
                    //将JSON的String 转成一个JsonArray对象
                    JsonArray jsonArray = parser.parse(jsons).getAsJsonArray();
                    billList = new ArrayList<>();
                    for (JsonElement n : jsonArray) {
                        //使用GSON，直接转成Bean对象
                        bill b=gson.fromJson(n,bill.class);
                        billList.add(b);
                    }
                    if (billList!=null){
                        Message message = mHandler.obtainMessage();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }else {
                        Message message = mHandler.obtainMessage();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void init(){
        bill_back=(ImageView)this.findViewById(R.id.bill_back);
        bill_game=(ImageView)this.findViewById(R.id.bill_game);
        bill_type=(TextView) this.findViewById(R.id.bill_type);
        bill_income=(TextView)this.findViewById(R.id.bill_income);
        bill_pay=(TextView)this.findViewById(R.id.bill_pay);
        bill_addUp=(TextView)this.findViewById(R.id.bill_addUp);
        bill_type.setText(type);
        bill_lv=(ListView)this.findViewById(R.id.bill_lv);
        bill_lv.setEmptyView(this.findViewById(R.id.layout_noData));
        swipeRefreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.swipe_container);
    }
    private void initEvent(){
        bill_back.setOnClickListener(this);
        bill_game.setOnClickListener(this);
        bill_type.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable(){
                    public void run(){
                        if(isNetworkAvailable(userExpenseCalendarActivity.this)){
                            attainBill();
                            Toast.makeText(userExpenseCalendarActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(userExpenseCalendarActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bill_back:
                Intent intent=new Intent(this,userActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bill_game:
                Intent i=new Intent(this,gameActivity.class);
                startActivity(i);
                finish();
            case R.id.bill_type:
                selectType();
                break;
        }
    }
    private void selectType(){
        AlertDialog.Builder incomeAlertDialog=new AlertDialog.Builder(this);
        incomeAlertDialog.setSingleChoiceItems(sort, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                // which表示点击的条目
                Object checkedItem = lw.getAdapter().getItem(which);
                // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                dialog.dismiss();
                // 更新你的view
                if(((String)checkedItem).equals("微信充值")){
                    type="微信充值";
                    bill_type.setText("类型:"+type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("支付宝充值")){
                    type="支付宝充值";
                    bill_type.setText("类型:"+type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("积分兑换")){
                    type="积分兑换";
                    bill_type.setText("类型:"+type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("饭堂带饭")){
                    type="饭堂带饭";
                    bill_type.setText("类型:"+type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("快递代拿")){
                    type="快递代拿";
                    bill_type.setText("类型:"+type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("商品代购")){
                    type="商品代购";
                    bill_type.setText("类型:"+type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("订单回退")){
                    type="订单回退";
                    bill_type.setText("类型:"+type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("综合")){
                    type="类型:综合";
                    bill_type.setText(type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("订单赏金")){
                    type="订单赏金";
                    bill_type.setText(type);
                    attainBill();
                    updateUI();
                }
                if(((String)checkedItem).equals("币值提现")){
                    type="币值提现";
                    bill_type.setText(type);
                    attainBill();
                    updateUI();
                }
            }
        });
        AlertDialog dialog = incomeAlertDialog.create();
        dialog.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,userActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
