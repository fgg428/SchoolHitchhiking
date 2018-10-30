package work.wang.schoolhitchhiking.show;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.userInfor.user;
import work.wang.schoolhitchhiking.userInfor.*;

public class userActivity extends AppCompatActivity implements View.OnClickListener{
    private long exitTime=System.currentTimeMillis();
    private ImageButton show,shopcart,task,message,user;
    private TextView user_install,tv_user,user_userName,user_money,user_experience;
    private Handler mHandler;
    private String userName="";
    ArrayList<user> userBeanList;
    private View layout_user,layout_order,layout_history_order,layout_expense_calendar,layout_invest_money,layout_invest_experience;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        userName=sp.getString("userName","1234");
        init();
        initEvent();
        attainUserInfor();
        updateUI();
    }
    private void init(){
        user_install=(TextView)this.findViewById(R.id.user_install);
        tv_user=(TextView)this.findViewById(R.id.bottom_tv_user);
        tv_user.setTextColor(Color.parseColor("#00EE00"));
        user_userName=(TextView)this.findViewById(R.id.user_userName);
        user_money=(TextView)this.findViewById(R.id.user_money);
        user_experience=(TextView)this.findViewById(R.id.user_experience);
        show=(ImageButton)this.findViewById(R.id.bottom_show);
        shopcart=(ImageButton)this.findViewById(R.id.bottom_shopcart);
        task=(ImageButton)this.findViewById(R.id.bottom_task);
        message=(ImageButton)this.findViewById(R.id.bottom_message);
        user=(ImageButton)this.findViewById(R.id.bottom_user);

        layout_user=this.findViewById(R.id.layout_user);
        layout_user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userActivity.this,userInforActivity.class);
                startActivity(intent);
                finish();
            }
        });
        layout_order=this.findViewById(R.id.layout_order);
        layout_order.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userActivity.this,userOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layout_history_order=this.findViewById(R.id.layout_history_order);
        layout_history_order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userActivity.this,selectHistoryOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layout_expense_calendar=this.findViewById(R.id.layout_expense_calendar);
        layout_expense_calendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userActivity.this,userExpenseCalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layout_invest_money=this.findViewById(R.id.layout_invest_money);
        layout_invest_money.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userActivity.this,userInvestMoneyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layout_invest_experience=this.findViewById(R.id.layout_invest_experience);
        layout_invest_experience.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userActivity.this,userInvestExperienceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initEvent(){
        show.setOnClickListener(this);
        shopcart.setOnClickListener(this);
        task.setOnClickListener(this);
        message.setOnClickListener(this);
        user.setOnClickListener(this);
        user_install.setOnClickListener(this);
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putInt("userID",userBeanList.get(0).userID);
                        editor.putString("Password",userBeanList.get(0).Password);
                        editor.putString("realName",userBeanList.get(0).realName);
                        editor.putString("location",userBeanList.get(0).location);
                        editor.putString("telNumber",userBeanList.get(0).telNumber);
                        editor.putInt("money",userBeanList.get(0).money);
                        editor.putInt("experience",userBeanList.get(0).experience);
                        editor.commit();
                        user_userName.setText("我的昵称："+userName);
                        user_money.setText(userBeanList.get(0).money+"");
                        user_experience.setText("我的蜂蜜值："+userBeanList.get(0).experience);
                        break;
                    case 2:
                        Toast.makeText(userActivity.this,"服务器获取出错",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void attainUserInfor(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userName",userName)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/userInformation")
                        .post(formBody)
                        .build();
                Response response;
                try {
                    Gson gson=new Gson();
                    response=client.newCall(request).execute();
                    String jsons=response.body().string();

                    //Json的解析类对象
                    JsonParser parser = new JsonParser();
                    //将JSON的String 转成一个JsonArray对象
                    JsonArray jsonArray = parser.parse(jsons).getAsJsonArray();
                    userBeanList = new ArrayList<>();
                    for (JsonElement n : jsonArray) {
                        //使用GSON，直接转成Bean对象
                        user use=gson.fromJson(n,user.class);
                        userBeanList.add(use);
                    }
                    //System.out.println(response.body().string());
                    //System.out.println("userName:::"+userBeanList.get(0).userName);
                    if (userBeanList!=null){
                        Message message = mHandler.obtainMessage();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }else {
                        Message message = mHandler.obtainMessage();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.bottom_show:
                intent=new Intent(userActivity.this,showActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_shopcart:
                intent=new Intent(userActivity.this,shopcartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_task:
                intent=new Intent(userActivity.this,taskActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_message:
                intent=new Intent(userActivity.this,messageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_user:
//                intent=new Intent(userActivity.this,userActivity.class);
//                startActivity(intent);
//                finish();
                break;
            case R.id.user_install:
                intent=new Intent(userActivity.this,installActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {

            if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
            {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
