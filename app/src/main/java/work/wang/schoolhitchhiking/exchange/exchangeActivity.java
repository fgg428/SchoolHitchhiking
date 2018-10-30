package work.wang.schoolhitchhiking.exchange;

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.userActivity;
import work.wang.schoolhitchhiking.userInfor.userInvestMoneyActivity;

public class exchangeActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private TextView exchange_state;
    private EditText exchange_number;
    private Button exchange_ok;
    int userID,number,result,experience;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        number=sp.getInt("experience",0);
        userID=sp.getInt("userID",1);
        init();
        initEvent();
    }
    private void init(){
        exchange_state=(TextView) this.findViewById(R.id.exchange_state);
        exchange_number=(EditText)this.findViewById(R.id.exchange_number);
        exchange_ok=(Button)this.findViewById(R.id.exchange_ok);
    }
    private void initEvent(){
        if(number<10){
            result=0;
        }else {
            result=number/10;
        }
        exchange_state.setText("注：您当前有"+number+"个蜂蜜积分,最多可兑换"+result
                +"个锋乐币");
        exchange_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(exchange_number.getText().toString()!=""){
            if(exchange_number.getText().toString().equals("0")){
                Toast.makeText(this,"兑换不能为0",Toast.LENGTH_SHORT).show();
                return;
            }
            if(result>0){
                int i=Integer.parseInt(exchange_number.getText().toString());
                if (i>result){
                    Toast.makeText(this,"蜂蜜积分不足",Toast.LENGTH_SHORT).show();
                }else {
                    result=i;
                    experience=number-result*10;
                    exchangeStart();
                    mHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what){
                                case 1:
                                    Toast.makeText(exchangeActivity.this,"兑换成功",Toast.LENGTH_SHORT).show();
//                                    SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
//                                    SharedPreferences.Editor editor=sp.edit();
//                                    editor.putInt("money",mon+Integer.parseInt(money));
//                                    editor.commit();
                                    Intent intent=new Intent(exchangeActivity.this,userActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 2:
                                    Toast.makeText(exchangeActivity.this,"兑换失败",Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(exchangeActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    };
                }
            }else {
                Toast.makeText(this,"蜂蜜积分不足",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this,"请输入兑换的数量",Toast.LENGTH_SHORT).show();
        }
    }
    private void exchangeStart(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",userID+"")
                        .add("money",result+"")
                        .add("experience",experience+"")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/exchangeMoney")
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
