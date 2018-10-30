package work.wang.schoolhitchhiking.taskFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import work.wang.schoolhitchhiking.order_object.dg;
import work.wang.schoolhitchhiking.show.taskActivity;

public class taskingDg extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    TextView dg_accept_money,dg_accept_type,dg_accept_date,dg_accept_name,dg_accept_phone,dg_accept_location;
    TextView dg_accept_state;
    Button dg_accept_back,dg_accept_accept;
    private int buyID,userID,money,workID;
    private String type,date,name,phone,location,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tasking_dg);
        init();
        attinData();
        initEvent();
        updateUI();
        deleterResult();
    }
    private void attinData(){
        dg d=(dg)getIntent().getSerializableExtra("dg");
        buyID=d.getBuyID();
        money=d.getPay();
        userID=d.getUserID();
        type=d.getBuyType();
        date=d.getBuyDate();
        name=d.getBuyerName();
        phone=d.getBuyerPhone();
        location=d.getBuyerLocation();
        state=d.getStatement();
    }
    private void updateUI(){
        dg_accept_money.setText(money+"");
        dg_accept_type.setText(type);
        dg_accept_date.setText(date);
        dg_accept_name.setText(name);
        dg_accept_phone.setText(phone);
        dg_accept_location.setText(location);
        dg_accept_state.setText(state);
    }
    private void init(){
        dg_accept_money=(TextView)this.findViewById(R.id.dg_accept_money);
        dg_accept_type=(TextView)this.findViewById(R.id.dg_accept_type);
        dg_accept_date=(TextView)this.findViewById(R.id.dg_accept_date);
        dg_accept_name=(TextView)this.findViewById(R.id.dg_accept_name);
        dg_accept_phone=(TextView)this.findViewById(R.id.dg_accept_phone);
        dg_accept_location=(TextView)this.findViewById(R.id.dg_accept_location);
        dg_accept_state=(TextView)this.findViewById(R.id.dg_accept_state);
        dg_accept_back=(Button)this.findViewById(R.id.dg_accept_back);
        dg_accept_accept=(Button)this.findViewById(R.id.dg_accept_accept);
    }
    private void initEvent(){
        dg_accept_back.setOnClickListener(this);
        dg_accept_accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dg_accept_back:
                Intent i=new Intent(this,taskActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.dg_accept_accept:
                AlertDialog dialog;
                dialog=new AlertDialog.Builder(this)
                        .setTitle(null)
                        .setMessage("是否确定退订该订单?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleterOrder();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                dialog.show();
                break;
        }
    }
    private void deleterResult(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        Toast.makeText(taskingDg.this,"订单退订成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(taskingDg.this,taskActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        Toast.makeText(taskingDg.this,"退订失败,订单已经被接啦",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(taskingDg.this,"服务器数据库失联...",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void deleterOrder(){
        new Thread(new Runnable() {
            @Override
            // private String rg_selected,money,userName,phone,location,state,date;
            public void run() {
                okhttp3.OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("buyID",buyID+"")
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/deleteDgOrder")
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
                        }else if(s.equals("accepted")){
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
}
