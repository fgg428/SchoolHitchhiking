package work.wang.schoolhitchhiking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.show.showActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class registerAndLoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText userName,password,again_password,name;
    Spinner sp_build,sp_floor,sp_room;
    Button login;
    private Handler mHandler;
    private final OkHttpClient client=new OkHttpClient();
    String telNum,uName,uPassword,aPassword,rName,build="1栋",floor="01楼",room="01室",rLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);
        Intent i=getIntent();
        telNum=i.getStringExtra("telNum");
        init();
    }
    private void init(){
        userName=(EditText)this.findViewById(R.id.userName);
        password=(EditText)this.findViewById(R.id.password);
        again_password=(EditText)this.findViewById(R.id.again_password);
        name=(EditText)this.findViewById(R.id.name);
        sp_build=(Spinner)this.findViewById(R.id.sp_build);
        sp_floor=(Spinner)this.findViewById(R.id.sp_floor);
        sp_room=(Spinner)this.findViewById(R.id.sp_room);
        sp_build.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                build = (String) sp_build.getSelectedItem()+"栋";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        sp_floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                floor = (String) sp_floor.getSelectedItem()+"楼";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        sp_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                room = (String) sp_room.getSelectedItem()+"室";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        login=(Button)this.findViewById(R.id.login);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (!(checkNetworkState())){
            Toast.makeText(this,"网络没有打开，请打开网络后重试",Toast.LENGTH_SHORT).show();
        }
        switch (view.getId()){
            case R.id.login:
                //将数据保存到数据库
                userInfor();
                break;
        }
    }
    private boolean checkNetworkState(){
        ConnectivityManager cm=(ConnectivityManager)this.getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        if((ni==null)||(!ni.isConnected())){
            return false;
        }
        return true;
    }
    private void userInfor(){
        uName=userName.getText().toString();
        uPassword=password.getText().toString();
        aPassword=again_password.getText().toString();
        rName=name.getText().toString();
        rLocation=build+floor+room;
        if (uName.equals("")){
            Toast.makeText(registerAndLoginActivity.this,"用户名不为空",Toast.LENGTH_SHORT).show();
        }else if(uPassword.equals("")){
            Toast.makeText(registerAndLoginActivity.this,"密码不为空",Toast.LENGTH_SHORT).show();
        }else if(!uPassword.equals(aPassword)){
            Toast.makeText(registerAndLoginActivity.this,"两次密码不同",Toast.LENGTH_SHORT).show();
            again_password.setText("");
        }else if(rName.equals("")){
            Toast.makeText(registerAndLoginActivity.this,"请填写真实姓名",Toast.LENGTH_SHORT).show();
        }else if(rLocation.equals("")){
            Toast.makeText(registerAndLoginActivity.this,"请填写宿舍地址",Toast.LENGTH_SHORT).show();
        }else {
            checkUName();
        }
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("userName",uName);
                        editor.commit();
                        Intent intent=new Intent(registerAndLoginActivity.this,showActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        Toast.makeText(registerAndLoginActivity.this,"插入数据库表时出错",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        setInfor(telNum,uName,uPassword,aPassword,rName,rLocation);
                        break;
                    case 4:
                        Toast.makeText(registerAndLoginActivity.this,"该昵称已经存在",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void checkUName(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody=new FormBody.Builder()
                        .add("uName",userName.getText().toString().trim())
                        .build();
                Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/checkUName")
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
                            String result=new String(data,"UTF-8");//byte[] 转 string
                            if(result.equals("success")){
                                Message message = mHandler.obtainMessage();
                                message.what = 3;
                                mHandler.sendMessage(message);
                            }else{
                                Message message = mHandler.obtainMessage();
                                message.what = 4;
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
    private void setInfor(final String telNum, final String uName, final String uPassword, final String aPassword, final String rName, final String rLocation){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                MediaType userInfor=MediaType.parse("text/plain");
                RequestBody requestBody=new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("telNum",telNum)
                        .addFormDataPart("uName",uName)
                        .addFormDataPart("uPassword",uPassword)
                        .addFormDataPart("rName",rName)
                        .addFormDataPart("rLocation",rLocation)
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/register")
                        .post(requestBody)
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
                            }else{
                                Message message = mHandler.obtainMessage();
                                message.what = 2;
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
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
