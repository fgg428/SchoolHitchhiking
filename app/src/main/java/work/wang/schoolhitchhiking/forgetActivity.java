package work.wang.schoolhitchhiking;

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
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.showActivity;

public class forgetActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView myName;
    private EditText new_password,new_again_password;
    private Button new_ok;
    private String userName="123";
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        userName=sp.getString("userName","我的123");
        init();
    }
    private void init(){
        myName=(TextView)this.findViewById(R.id.myName);
        myName.setText(userName);
        new_password=(EditText)this.findViewById(R.id.new_password);
        new_again_password=(EditText)this.findViewById(R.id.new_again_password);
        new_ok=(Button)this.findViewById(R.id.new_ok);
        new_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(new_password.getText().toString().equals(new_again_password.getText().toString())){
            updatePassword();
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 1:
                            SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putString("makeInfor","no");
                            editor.commit();
                            Toast.makeText(forgetActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(forgetActivity.this,showActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 2:
                            Toast.makeText(forgetActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(forgetActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            };
        }else {
            Toast.makeText(this,"两次密码不同",Toast.LENGTH_SHORT).show();
        }
    }
    private void updatePassword(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("new_password",new_password.getText().toString())
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
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
