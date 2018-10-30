package work.wang.schoolhitchhiking.userInfor;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.userActivity;

public class payMoneyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et;
    private RadioGroup rg;
    private Button ok;
    String selected="微信",userName,money;
    private Handler mHandler;
    int mon;//拿到钱
    int id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_money);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        mon=sp.getInt("money",0);
        id=sp.getInt("userID",1);
        init();
        initEvent();
    }
    private void init(){
        et=(EditText)this.findViewById(R.id.pay_money_et);
        rg=(RadioGroup) this.findViewById(R.id.pay_money_rg);
        ok=(Button)this.findViewById(R.id.pay_money_ok);
    }
    private void initEvent(){
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRadioButton();
            }
        });
        ok.setOnClickListener(this);
    }
    private void selectRadioButton(){
        selected="";
        RadioButton rb = (RadioButton)this.findViewById(rg.getCheckedRadioButtonId());
        selected=rb.getText().toString();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,installActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        money=et.getText().toString();
        if(money.equals("")){
            Toast.makeText(payMoneyActivity.this,"请输入提现金额",Toast.LENGTH_SHORT).show();
        }else{
            if (Integer.parseInt(et.getText().toString())==0){
                Toast.makeText(payMoneyActivity.this,"提现金额不为0",Toast.LENGTH_SHORT).show();
            }else {
                if(Integer.parseInt(money)>mon){
                    Toast.makeText(payMoneyActivity.this,"锋乐币不足",Toast.LENGTH_SHORT).show();
                }else {
                    payMoney();
                    mHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what){
                                case 1:
                                    Toast.makeText(payMoneyActivity.this,"提现成功",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(payMoneyActivity.this,userActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 2:
                                    Toast.makeText(payMoneyActivity.this,"提现失败",Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(payMoneyActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    };
                }
            }
        }
    }
    private void payMoney(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("money",money)
                        .add("userID",id+"")
                        .add("type",selected)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/payMoney")
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
                        }else {
                            Message message = mHandler.obtainMessage();
                            message.what = 2;
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
