package work.wang.schoolhitchhiking.order;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.MainActivity;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class dgActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private RadioGroup dg_rg_type;
    private TextView dg_date,dg_select_date;
    private EditText dg_userName,dg_phone,dg_location,dg_state;
    private Spinner dg_money;
    private Button dg_ok;
    private String rg_selected="",money,userName,phone,location,state,date;
    private Calendar cal;
    private int year,month,day;
    private int id,now_money,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dg);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        id=sp.getInt("userID",1);
        now_money=sp.getInt("money",0);
        //获取当前日期
        getDate();
        init();
        initEvent();
    }
    private void init(){
        dg_rg_type=(RadioGroup)this.findViewById(R.id.dg_rg_type);
        dg_select_date=(TextView)this.findViewById(R.id.dg_select_date);
        dg_date=(TextView)this.findViewById(R.id.dg_date);
        dg_userName=(EditText)this.findViewById(R.id.dg_userName);
        dg_phone=(EditText)this.findViewById(R.id.dg_phone);
        dg_location=(EditText)this.findViewById(R.id.dg_location);
        dg_state=(EditText)this.findViewById(R.id.dg_state);
        dg_money=(Spinner)this.findViewById(R.id.dg_money);
        dg_ok=(Button)this.findViewById(R.id.dg_ok);
    }
    private void initEvent(){
        //获取代购类型
        dg_rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRgButton();
            }
        });
        //获取日期
        dg_select_date.setOnClickListener(this);
        //获取赏金
        dg_money.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                money = (String) dg_money.getSelectedItem();
                number=Integer.parseInt(money);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        dg_ok.setOnClickListener(this);
    }
    private void selectRgButton(){
        rg_selected="";
        RadioButton rb = (RadioButton)this.findViewById(dg_rg_type.getCheckedRadioButtonId());
        rg_selected=rb.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dg_select_date:
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        dg_date.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                DatePickerDialog dialog=new DatePickerDialog(dgActivity.this, 0,listener,year,month,day);
                dialog.show();
                break;
            case R.id.dg_ok:
                insertBuy();
                break;
        }
    }
    private void insertBuy(){
        //rg_selected,money
        userName=dg_userName.getText().toString();
        phone=dg_phone.getText().toString();
        date=dg_date.getText().toString();
        location=dg_location.getText().toString();
        state=dg_state.getText().toString();
        if(userName.equals("")){
            Toast.makeText(this,"请填写联系人姓名",Toast.LENGTH_SHORT).show();
        }else if(phone.equals("")){
            Toast.makeText(this,"联系人手机号不为空",Toast.LENGTH_SHORT).show();
        }else if(date.equals("")){
            Toast.makeText(this,"请选择日期",Toast.LENGTH_SHORT).show();
        }else if(location.equals("")){
            Toast.makeText(this,"请填写宿舍地址",Toast.LENGTH_SHORT).show();
        }else if(rg_selected.equals("")){
            Toast.makeText(this,"请选择代购物品类型",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(dgActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(dgActivity.this,userActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                Toast.makeText(dgActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(dgActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
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
                        .add("date",date)
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/orderBuy")
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
    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
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
