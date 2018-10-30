package work.wang.schoolhitchhiking.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.MainActivity;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class dfActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private RadioGroup df_rg_type,df_rg;
    private EditText df_userName,df_phone,df_location,df_state;
    private Spinner df_money;
    private Button df_ok;
    private int id,now_money,number;
    private String rg_selected="",rg_type_selected="";
    private String userName,phone,state,location,money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_df);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        id=sp.getInt("userID",1);
        now_money=sp.getInt("money",0);
        init();
        initEvent();
    }
    private void init(){
        df_rg_type=(RadioGroup)this.findViewById(R.id.df_rg_type);
        df_rg=(RadioGroup)this.findViewById(R.id.df_rg);
        df_userName=(EditText)this.findViewById(R.id.df_userName);
        df_phone=(EditText)this.findViewById(R.id.df_phone);
        df_location=(EditText)this.findViewById(R.id.df_location);
        df_state=(EditText)this.findViewById(R.id.df_state);
        df_money=(Spinner)this.findViewById(R.id.df_money);
        df_ok=(Button)this.findViewById(R.id.df_ok);
    }
    private void initEvent(){
        //获取带饭地点
        df_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRgButton();
            }
        });
        //获取带饭类型
        df_rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRgTypeButton();
            }
        });
        //获取赏金
        df_money.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                money = (String) df_money.getSelectedItem();
                number=Integer.parseInt(money);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        df_ok.setOnClickListener(this);
    }
    private void selectRgButton(){
        rg_selected="";
        RadioButton rb = (RadioButton)this.findViewById(df_rg.getCheckedRadioButtonId());
        rg_selected=rb.getText().toString();
    }
    private void selectRgTypeButton(){
        rg_type_selected="";
        RadioButton rb = (RadioButton)this.findViewById(df_rg_type.getCheckedRadioButtonId());
        rg_type_selected=rb.getText().toString();
    }

    @Override
    public void onClick(View view) {
        insertFood();
    }
    private void insertFood(){
        userName=df_userName.getText().toString();
        phone=df_phone.getText().toString();
        location=df_location.getText().toString();
        state=df_state.getText().toString();
        if(userName.equals("")){
            Toast.makeText(this,"请填写联系人姓名",Toast.LENGTH_SHORT).show();
        }else if(phone.equals("")){
            Toast.makeText(this,"联系人手机号不为空",Toast.LENGTH_SHORT).show();
        }else if(location.equals("")){
            Toast.makeText(this,"请填写宿舍地址",Toast.LENGTH_SHORT).show();
        }else if(rg_type_selected.equals("")){
            Toast.makeText(this,"请选择带饭类型",Toast.LENGTH_SHORT).show();
        }else if(rg_selected.equals("")){
            Toast.makeText(this,"请选择饭堂类型",Toast.LENGTH_SHORT).show();
        }else if(now_money<number){
            Toast.makeText(this,"您的锋乐币不足",Toast.LENGTH_SHORT).show();
        }else {
            if (checkNetworkState()){
                startInsert();
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case 1:
                                Toast.makeText(dfActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(dfActivity.this,userActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                Toast.makeText(dfActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(dfActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                };
            }else {
                Toast.makeText(this,"网络没有打开，请打开网络后重试",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void startInsert(){
        new Thread(new Runnable() {
            @Override
            // private String rg_selected,money,userName,phone,location,state,date;
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",id+"")
                        .add("userName",userName)
                        .add("phone",phone)
                        .add("location",location)
                        .add("state",state)
                        .add("money",money)
                        .add("type",rg_selected)
                        .add("food_type",rg_type_selected)
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/orderFood")
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
    private boolean checkNetworkState(){
        ConnectivityManager cm=(ConnectivityManager)this.getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        if((ni==null)||(!ni.isConnected())){
            return false;
        }
        return true;
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
