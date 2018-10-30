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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import work.wang.schoolhitchhiking.MainActivity;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class kdActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private RadioGroup kd_rg,kd_rg_type;
    private Spinner kd_company,kd_money;
    private EditText kd_userName,kd_phone,kd_weight,kd_location,kd_state;
    private Button kd_ok;

    private String sp_company="顺丰快递",sp_money="1";
    private String rg_selected="",rg_type_selected="";
    private String userName,phone,weight,location,state;

    private int id,money,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kd);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        id=sp.getInt("userID",1);
        money=sp.getInt("money",0);
        init();
        initEvent();
    }
    private void init(){
        kd_rg=(RadioGroup)this.findViewById(R.id.kd_rg);
        kd_rg_type=(RadioGroup)this.findViewById(R.id.kd_rg_type);
        kd_company=(Spinner)this.findViewById(R.id.kd_company);
        kd_money=(Spinner)this.findViewById(R.id.kd_money);
        kd_userName=(EditText)this.findViewById(R.id.kd_userName);
        kd_phone=(EditText)this.findViewById(R.id.kd_phone);
        kd_weight=(EditText)this.findViewById(R.id.kd_weight);
        kd_location=(EditText)this.findViewById(R.id.kd_location);
        kd_state=(EditText)this.findViewById(R.id.kd_state);
        kd_ok=(Button)this.findViewById(R.id.kd_ok);
    }
    private void initEvent(){
        //获取快递站点
        kd_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRgButton();
            }
        });

        //获取快递公司
        kd_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                sp_company = (String) kd_company.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //获取物品类型
        kd_rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRgTypeButton();
            }
        });

        //获取赏金
        kd_money.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                sp_money = (String) kd_money.getSelectedItem();
                number=Integer.parseInt(sp_money);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //提交订单
        kd_ok.setOnClickListener(this);
    }
    private void selectRgButton(){
        rg_selected="";
        RadioButton rb = (RadioButton)this.findViewById(kd_rg.getCheckedRadioButtonId());
        rg_selected=rb.getText().toString();
    }
    private void selectRgTypeButton(){
        rg_type_selected="";
        RadioButton rb = (RadioButton)this.findViewById(kd_rg_type.getCheckedRadioButtonId());
        rg_type_selected=rb.getText().toString();
    }

    @Override
    public void onClick(View view) {
        //获取联系人姓名
        userName=kd_userName.getText().toString();
        //获取手机号码
        phone=kd_phone.getText().toString();
        //获取物品重量
        weight=kd_weight.getText().toString();
        //获取用户地址
        location=kd_location.getText().toString();
        //获取说明
        state=kd_state.getText().toString();

        if (rg_selected.equals("")){
            Toast.makeText(this,"请选择快递站点",Toast.LENGTH_SHORT).show();
        }else if(rg_type_selected.equals("")){
            Toast.makeText(this,"请选择物品类型",Toast.LENGTH_SHORT).show();
        }else if(userName.equals("")){
            Toast.makeText(this,"请填写联系人姓名",Toast.LENGTH_SHORT).show();
        }else if(phone.equals("")){
            Toast.makeText(this,"请填写手机号码",Toast.LENGTH_SHORT).show();
        }else if(weight.equals("")){
            Toast.makeText(this,"请填写物品重量",Toast.LENGTH_SHORT).show();
        }else if(location.equals("")){
            Toast.makeText(this,"请填写宿舍地址",Toast.LENGTH_SHORT).show();
        }else if(money<number){
            Toast.makeText(this,"您的锋乐币不足",Toast.LENGTH_SHORT).show();
        } else{
            if(checkNetworkState()){
                insertTableSend();
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case 1:
                                Toast.makeText(kdActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(kdActivity.this,userActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                Toast.makeText(kdActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(kdActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
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
    private void insertTableSend(){
        new Thread(new Runnable() {
            @Override
            //        private String sp_company="顺丰快递",sp_money="1";
//        private String rg_selected="",rg_type_selected="";
//        private String userName,phone,weight,location,state;
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",id+"")
                        .add("userName",userName)
                        .add("phone",phone)
                        .add("weight",weight)
                        .add("location",location)
                        .add("state",state)
                        .add("company",sp_company)
                        .add("money",sp_money)
                        .add("locationType",rg_selected)
                        .add("goodsType",rg_type_selected)
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/orderSend")
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
