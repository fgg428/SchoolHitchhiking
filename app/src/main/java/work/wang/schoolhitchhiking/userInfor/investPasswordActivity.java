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
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.forgetActivity;
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.showActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class investPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    String old_password;
    EditText old,new_p,new_ap;
    Button back,revise;
    private Handler mHandler;
    private String userName="123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_invest_password);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        old_password=sp.getString("Password","1");
        userName=sp.getString("userName","我的123");
        init();
    }
    private void init(){
        old=(EditText)this.findViewById(R.id.invest_old);
        new_p=(EditText)this.findViewById(R.id.invest_password);
        new_ap=(EditText)this.findViewById(R.id.invest_aPassword);
        back=(Button)this.findViewById(R.id.invest_back);
        revise=(Button)this.findViewById(R.id.invest_revise);
        back.setOnClickListener(this);
        revise.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.invest_back:
                Intent intent=new Intent(investPasswordActivity.this,userActivity.class);
                startActivity(intent);
                break;
            case R.id.invest_revise:
                checkAndRevise();
                break;
        }
    }
    private void checkAndRevise(){
        if(!old.getText().toString().equals(old_password)){
            Toast.makeText(this,"旧密码有误，请重新输入",Toast.LENGTH_SHORT).show();
            old.setText("");
        }else if (new_p.getText().toString().equals("")){
            Toast.makeText(this,"密码不为空",Toast.LENGTH_SHORT).show();
        }else if (!new_p.getText().toString().equals(new_ap.getText().toString())){
            Toast.makeText(this,"两次密码不同，请重新输入",Toast.LENGTH_SHORT).show();
            new_p.setText("");
            new_ap.setText("");
        }else {
            investPassword();
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 1:
                            Toast.makeText(investPasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(investPasswordActivity.this,userActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 2:
                            Toast.makeText(investPasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(investPasswordActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            };
        }
    }
    private void investPassword(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("new_password",new_p.getText().toString())
                        .add("userName",userName)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/updatePassword")
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
