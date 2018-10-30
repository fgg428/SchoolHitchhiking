package work.wang.schoolhitchhiking.accept_order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.order_object.df;
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.shopcartActivity;
import work.wang.schoolhitchhiking.show.showActivity;

public class dfAcceptActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private TextView df_accept_money,df_accept_foodType,df_accept_foodLocation,df_accept_name,df_accept_phone,df_accept_location,df_accept_state;
    private TextView df_look_review;
    private Button df_accept_back,df_accept_accept;
    private int foodID,userID,money,workID;
    private String foodType,name,phone,locationType,state,location;
    private int laiyuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_df_accept);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        workID=sp.getInt("userID",1);
        attinData();
        init();
        initEvent();
        updateUI();
    }
    private void attinData(){
        df d=(df)getIntent().getSerializableExtra("df_data");
        laiyuan=getIntent().getIntExtra("laiyuan",0);
        foodID=d.getFoodID();
        money=d.getPay();
        userID=d.getUserID();
        foodType=d.getFoodType();
        name=d.getBuyerName();
        phone=d.getBuyerPhone();
        locationType=d.getFoodLocationType();
        state=d.getStatement();
        location=d.getBuyerLocation();
    }
    private void updateUI(){
        df_accept_money.setText(money+"");
        df_accept_foodType.setText(foodType);
        df_accept_foodLocation.setText(locationType);
        df_accept_location.setText(location);
        df_accept_name.setText(name);
        df_accept_phone.setText(phone);
        df_accept_state.setText(state);
    }
    private void init(){
        df_accept_money=(TextView)this.findViewById(R.id.df_accept_money);
        df_accept_foodType=(TextView)this.findViewById(R.id.df_accept_foodType);
        df_accept_foodLocation=(TextView)this.findViewById(R.id.df_accept_foodLocation);
        df_accept_name=(TextView)this.findViewById(R.id.df_accept_name);
        df_accept_phone=(TextView)this.findViewById(R.id.df_accept_phone);
        df_accept_location=(TextView)this.findViewById(R.id.df_accept_location);
        df_accept_state=(TextView)this.findViewById(R.id.df_accept_state);
        df_look_review=(TextView)this.findViewById(R.id.df_look_review);
        df_accept_back=(Button)this.findViewById(R.id.df_accept_back);
        df_accept_accept=(Button)this.findViewById(R.id.df_accept_accept);
    }
    private void initEvent(){
        df_look_review.setOnClickListener(this);
        df_accept_back.setOnClickListener(this);
        df_accept_accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.df_look_review:
                break;
            case R.id.df_accept_back:
                Intent i=new Intent(this,showActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.df_accept_accept:
                if(userID!=workID){
                    insertTblAccept();
                }else {
                    Toast.makeText(dfAcceptActivity.this,"不可以接自己发的订单哦",Toast.LENGTH_SHORT).show();
                }
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case 1:
                                Toast.makeText(dfAcceptActivity.this,"接单成功√",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(dfAcceptActivity.this,"订单已经被接啦",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(dfAcceptActivity.this,"服务器数据库失联...",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                };
                break;
        }
    }
    private void insertTblAccept(){
        new Thread(new Runnable() {
            @Override
            // private String rg_selected,money,userName,phone,location,state,date;
            public void run() {
                okhttp3.OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",userID+"")
                        .add("workID",workID+"")
                        .add("tblID",foodID+"")
                        .add("type","快餐代送")
                        .add("pay",money+"")
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/acceptDfOrder")
                        .post(formBody)
                        .build();
                try {
                    Response response=client.newCall(request).execute();
                    if (!response.isSuccessful()){
                        Message message = mHandler.obtainMessage();
                        message.what = 3;
                        mHandler.sendMessage(message);
                    }else {
                        String s=response.body().string();
                        if(s.equals("success")){
                            Message message = mHandler.obtainMessage();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }else if(s.equals("existed")){
                            Message message = mHandler.obtainMessage();
                            message.what = 2;
                            mHandler.sendMessage(message);
                        }else {
                            Message message = mHandler.obtainMessage();
                            message.what = 3;
                            mHandler.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (laiyuan==0){
                Intent intent=new Intent(this,showActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent=new Intent(this,shopcartActivity.class);
                startActivity(intent);
                finish();
            }

        }
        return true;
    }
}
