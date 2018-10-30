package work.wang.schoolhitchhiking.userInfor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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

public class userInvestMoneyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText invest_money_et;
    private Button invest_money_ok;
    private RadioGroup invest_money_rg;
    String selected="微信",userName,money;
    private Handler mHandler;
    int mon;//拿到钱
    int id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_invest_money);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        userName=sp.getString("userName","我的123");
        mon=sp.getInt("money",0);
        id=sp.getInt("userID",1);
        init();
        initEvent();
    }
    private void init(){
        invest_money_et=(EditText)this.findViewById(R.id.invest_money_et);
        invest_money_ok=(Button)this.findViewById(R.id.invest_money_ok);
        invest_money_rg=(RadioGroup)this.findViewById(R.id.invest_money_rg);
    }
    private void initEvent(){
        invest_money_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRadioButton();
            }
        });
        invest_money_ok.setOnClickListener(this);
    }
    private void selectRadioButton(){
        selected="";
        RadioButton rb = (RadioButton)this.findViewById(invest_money_rg.getCheckedRadioButtonId());
        selected=rb.getText().toString();
    }

    @Override
    public void onClick(View view) {
        money=invest_money_et.getText().toString();
        if(money!="" ){
            if(money.equals("0")){
                Toast.makeText(this,"充值金额不为0（元）",Toast.LENGTH_SHORT).show();
            }else {
                investMoney();
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case 1:
                                Toast.makeText(userInvestMoneyActivity.this,"充值成功",Toast.LENGTH_SHORT).show();
                                SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sp.edit();
                                editor.putInt("money",mon+Integer.parseInt(money));
                                editor.commit();
                                Intent intent=new Intent(userInvestMoneyActivity.this,userActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                Toast.makeText(userInvestMoneyActivity.this,"充值失败",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(userInvestMoneyActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        }else {
            Toast.makeText(this,"充值金额不为空",Toast.LENGTH_SHORT).show();
        }
    }
    private void investMoney(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userName",userName)
                        .add("money",money)
                        .add("userID",id+"")
                        .add("type",selected)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/investMoney")
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
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,userActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
