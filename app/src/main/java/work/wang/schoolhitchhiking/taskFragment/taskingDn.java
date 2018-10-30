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
import work.wang.schoolhitchhiking.order_object.kd;
import work.wang.schoolhitchhiking.show.taskActivity;

public class taskingDn extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private TextView dn_accept_money,dn_accept_locationType,dn_accept_company,dn_accept_name,dn_accept_phone;
    private TextView dn_accept_goodsType,dn_accept_goodsWeight,dn_accept_date,dn_accept_location,dn_accept_state;
    private Button dn_accept_back,dn_accept_accept;
    private int sendID,userID,money,workID,goodsWeight;
    private String locationType,company,name,phone,goodsType,date,location,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tasking_dn);
        attinData();
        init();
        initEvent2();
        updateUI();
        deleterResult();
    }
    private void attinData(){
        kd d=(kd)getIntent().getSerializableExtra("dn");
        sendID=d.getSendID();
        userID=d.getUserID();
        money=d.getPay();
        goodsWeight=d.getGoodsWeight();
        locationType=d.getLocationType();
        company=d.getSendCompany();
        name=d.getContactPerson();
        phone=d.getPhone();
        goodsType=d.getGoodsType();
        date=d.getSendDate();
        location=d.getSendLocation();
        state=d.getState();
    }
    private void updateUI(){
        dn_accept_money.setText(money+"");
        dn_accept_locationType.setText(locationType);
        dn_accept_company.setText(company);
        dn_accept_name.setText(name);
        dn_accept_phone.setText(phone);
        dn_accept_goodsType.setText(goodsType);
        dn_accept_goodsWeight.setText(goodsWeight+"千克");
        dn_accept_date.setText(date+"(今天)");
        dn_accept_location.setText(location);
        dn_accept_state.setText(state);
    }
    private void init(){
        dn_accept_money=(TextView)this.findViewById(R.id.dn_accept_money);
        dn_accept_locationType=(TextView)this.findViewById(R.id.dn_accept_locationType);
        dn_accept_company=(TextView)this.findViewById(R.id.dn_accept_company);
        dn_accept_name=(TextView)this.findViewById(R.id.dn_accept_name);
        dn_accept_phone=(TextView)this.findViewById(R.id.dn_accept_phone);
        dn_accept_goodsType=(TextView)this.findViewById(R.id.dn_accept_goodsType);
        dn_accept_goodsWeight=(TextView)this.findViewById(R.id.dn_accept_goodsWeight);
        dn_accept_date=(TextView)this.findViewById(R.id.dn_accept_date);
        dn_accept_location=(TextView)this.findViewById(R.id.dn_accept_location);
        dn_accept_state=(TextView)this.findViewById(R.id.dn_accept_state);
        dn_accept_back=(Button) this.findViewById(R.id.dn_accept_back);
        dn_accept_accept=(Button)this.findViewById(R.id.dn_accept_accept);
    }
    private void initEvent2(){
        dn_accept_back.setOnClickListener(this);
        dn_accept_accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dn_accept_back:
                Intent i=new Intent(this,taskActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.dn_accept_accept:
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
                        Toast.makeText(taskingDn.this,"订单退订成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(taskingDn.this,taskActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        Toast.makeText(taskingDn.this,"退订失败,订单已经被接啦",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(taskingDn.this,"服务器数据库失联...",Toast.LENGTH_SHORT).show();
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
                        .add("sendID",sendID+"")
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/deleteDnOrder")
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
