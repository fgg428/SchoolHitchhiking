package work.wang.schoolhitchhiking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.show.showActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText lg_number,lg_psw;
    Button lg;
    TextView lg_f_psw,lg_register;
    String UserNumber,UserPassword;
    private Handler mHandler;
    private final OkHttpClient client=new OkHttpClient();
    private int noLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        noLogin=sp.getInt("noLogin",0);
        if (noLogin==1){
            Intent intent=new Intent(this,showActivity.class);
            startActivity(intent);
            finish();
        }
        init();
        lg.setOnClickListener(this);
        lg_f_psw.setOnClickListener(this);
        lg_register.setOnClickListener(this);
    }
    private void init(){
         lg_number= (EditText)findViewById(R.id.lg_number);
         lg_psw=(EditText)findViewById(R.id.lg_psw);
        lg=(Button)findViewById(R.id.lg_lg);
        lg_f_psw=(TextView)findViewById(R.id.lg_f_psw);
        lg_register=(TextView)findViewById(R.id.lg_register);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lg_lg:
                login();
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case 1:
                                SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sp.edit();
                                editor.putString("userName",UserNumber);
                                editor.putString("makeInfor","no");
                                editor.commit();
                                Intent intent=new Intent(MainActivity.this,showActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(MainActivity.this,"服务器数据库失联中",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                };
                break;
            case R.id.lg_f_psw:
                forget();
                break;
            case R.id.lg_register:
                register();
                break;
        }

    }
    private void login(){
        UserNumber=lg_number.getText().toString();
        UserPassword=lg_psw.getText().toString();
        if(UserNumber.equals("")){
            Toast.makeText(MainActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
        }else if(UserPassword.equals("")){
            Toast.makeText(MainActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestBody formBody=new FormBody.Builder()
                            .add("userNT",UserNumber)
                            .add("uPassword",UserPassword)
                            .build();
                    Request request=new Request.Builder()
                            .url("http://172.17.149.206:8080/hitchhiking/login")
                            .post(formBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful()){
                                return;
                            }
                            byte[] data=null;
                            try {
                                data=response.body().bytes();
                                String mg=new String(data,"UTF-8");//byte[] 转 string
                                if(mg.equals("success")){
                                    Message message = mHandler.obtainMessage();
                                    message.what = 1;
                                    mHandler.sendMessage(message);
                                }else if(mg.equals("fail")){
                                    Message message = mHandler.obtainMessage();
                                    message.what = 2;
                                    mHandler.sendMessage(message);
                                } else{
                                    Message message = mHandler.obtainMessage();
                                    message.what = 3;
                                    mHandler.sendMessage(message);
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        }
    }
    private void forget(){
        Intent intent=new Intent(this,registerActivity.class);
        intent.putExtra("status","forget");
        startActivity(intent);
        finish();
    }
    private void register(){
        Intent intent=new Intent(this,registerActivity.class);
        intent.putExtra("status","register");
        startActivity(intent);
        finish();
    }
}
