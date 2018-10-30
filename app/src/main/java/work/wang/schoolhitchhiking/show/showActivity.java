package work.wang.schoolhitchhiking.show;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.fragment.dgFragment;
import work.wang.schoolhitchhiking.fragment.dnFragment;
import work.wang.schoolhitchhiking.fragment.dscFragment;
import work.wang.schoolhitchhiking.userInfor.user;

public class showActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Fragment> fragmentList;
    ArrayList<user> userBeanList;
    private Handler mHandler;
    private TextView dg,kd,dsc,tv_show;
    private ViewPager vp;
    private ImageButton show,shopcart,task,message,user;
    private long exitTime=System.currentTimeMillis();
    String makeInfor,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        makeInfor=sp.getString("makeInfor","no");
        userName=sp.getString("userName","1231");
        if(makeInfor.equals("no")){
            attainUserInfor();
            updateUserInfor();
        }
        init();
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new dgFragment());
        fragments.add(new dnFragment());
        fragments.add(new dscFragment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (vp.getCurrentItem()){
                    case 0:
                        changeTextColor(0);
                        break;
                    case 1:
                        changeTextColor(1);
                        break;
                    case 2:
                        changeTextColor(2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void init(){
        tv_show=(TextView)this.findViewById(R.id.bottom_tv_show);
        tv_show.setTextColor(Color.parseColor("#00EE00"));
        dg=(TextView)this.findViewById(R.id.top_dg);
        kd=(TextView)this.findViewById(R.id.top_kd);
        dsc=(TextView)this.findViewById(R.id.top_dsc);
        vp = (ViewPager) this.findViewById(R.id.show_vp);
        show=(ImageButton)this.findViewById(R.id.bottom_show);
        shopcart=(ImageButton)this.findViewById(R.id.bottom_shopcart);
        task=(ImageButton)this.findViewById(R.id.bottom_task);
        message=(ImageButton)this.findViewById(R.id.bottom_message);
        user=(ImageButton)this.findViewById(R.id.bottom_user);
        dg.setOnClickListener(this);
        kd.setOnClickListener(this);
        dsc.setOnClickListener(this);
        show.setOnClickListener(this);
        shopcart.setOnClickListener(this);
        task.setOnClickListener(this);
        message.setOnClickListener(this);
        user.setOnClickListener(this);
    }
    private void changeTextColor(int i){
        switch (i){
            case 0:
                dg.setTextColor(Color.parseColor("#FFFFFF"));
                kd.setTextColor(Color.parseColor("#BEBEBE"));
                dsc.setTextColor(Color.parseColor("#BEBEBE"));
                break;
            case 1:
                dg.setTextColor(Color.parseColor("#BEBEBE"));
                kd.setTextColor(Color.parseColor("#FFFFFF"));
                dsc.setTextColor(Color.parseColor("#BEBEBE"));
                break;
            case 2:
                dg.setTextColor(Color.parseColor("#BEBEBE"));
                kd.setTextColor(Color.parseColor("#BEBEBE"));
                dsc.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.top_dg:
                vp.setCurrentItem(0);
                changeTextColor(0);
                break;
            case R.id.top_kd:
                vp.setCurrentItem(1);
                changeTextColor(1);
                break;
            case R.id.top_dsc:
                vp.setCurrentItem(2);
                changeTextColor(2);
                break;
            case R.id.bottom_show:
//                intent=new Intent(showActivity.this,showActivity.class);
//                startActivity(intent);
//                finish();
                break;
            case R.id.bottom_shopcart:
                intent=new Intent(showActivity.this,shopcartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_task:
                intent=new Intent(showActivity.this,taskActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_message:
                intent=new Intent(showActivity.this,messageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_user:
                intent=new Intent(showActivity.this,userActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    private void updateUserInfor(){
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
                        break;
                    case 2:
                        Toast.makeText(showActivity.this,"服务器获取出错",Toast.LENGTH_SHORT).show();
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
    public class FragAdapter extends FragmentPagerAdapter {
        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            fragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}