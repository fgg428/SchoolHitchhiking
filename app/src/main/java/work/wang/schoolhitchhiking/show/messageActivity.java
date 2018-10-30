package work.wang.schoolhitchhiking.show;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
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
import work.wang.schoolhitchhiking.adapter.message_adapter;
import work.wang.schoolhitchhiking.message_object.message;
import work.wang.schoolhitchhiking.message_object.messageDfActivity;
import work.wang.schoolhitchhiking.message_object.messageDgActivity;
import work.wang.schoolhitchhiking.message_object.messageDnActivity;
import work.wang.schoolhitchhiking.userInfor.userExpenseCalendarActivity;

public class messageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Handler mHandler;
    private List<message> list;
    private long exitTime=System.currentTimeMillis();
    private ImageButton show,shopcart,task,message,user;
    private TextView tv_message;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        id=sp.getInt("userID",1);
        init();
        initEvent();
        attainMessage();
        updateUI();
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        listView.setAdapter(null);
                        message_adapter m=new message_adapter(messageActivity.this,list);
                        listView.setAdapter(m);
                        break;
                    case 2:
                        Toast.makeText(messageActivity.this,"没有数据刷新试试",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void attainMessage(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",id+"")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/acceptOrderMessage")
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
                    list = new ArrayList<>();
                    for (JsonElement n : jsonArray) {
                        //使用GSON，直接转成Bean对象
                        message k=gson.fromJson(n,message.class);
                        list.add(k);
                    }
                    if (list!=null){
                        for(int i=0;i<list.size();i++){
                            for (int j = 0; j < i; j++){
                                if (list.get(i).getAcceptID()==list.get(j).getAcceptID() && list.get(i).getAcceptType().equals(list.get(j).getAcceptType())){
                                    list.remove(i);
                                    i=i-1;
                                }
                            }
                        }
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
    private void init(){
        tv_message=(TextView)this.findViewById(R.id.bottom_tv_message);
        tv_message.setTextColor(Color.parseColor("#00EE00"));
        show=(ImageButton)this.findViewById(R.id.bottom_show);
        shopcart=(ImageButton)this.findViewById(R.id.bottom_shopcart);
        task=(ImageButton)this.findViewById(R.id.bottom_task);
        message=(ImageButton)this.findViewById(R.id.bottom_message);
        user=(ImageButton)this.findViewById(R.id.bottom_user);
        listView=(ListView)this.findViewById(R.id.message_lv);
        listView.setEmptyView(this.findViewById(R.id.layout_noData));
        //swipeRefreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.swipe_container);
    }
    private void initEvent(){
        show.setOnClickListener(this);
        shopcart.setOnClickListener(this);
        task.setOnClickListener(this);
        message.setOnClickListener(this);
        user.setOnClickListener(this);
        listView.setOnItemClickListener(this);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
//
//            @Override
//            public void onRefresh() {
//                mHandler.postDelayed(new Runnable(){
//                    public void run(){
//                        if(isNetworkAvailable(messageActivity.this)){
//                            attainMessage();
//                            Toast.makeText(messageActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(messageActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
//                        }
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                },3000);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.bottom_show:
                intent=new Intent(messageActivity.this,showActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_shopcart:
                intent=new Intent(messageActivity.this,shopcartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_task:
                intent=new Intent(messageActivity.this,taskActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_message:
//                intent=new Intent(messageActivity.this,messageActivity.class);
////               startActivity(intent);
////               finish();
                break;
            case R.id.bottom_user:
                intent=new Intent(messageActivity.this,userActivity.class);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        message m = list.get(i);
        if (list.get(i).getAcceptType().equals("快餐代送")) {
            Intent intent = new Intent(messageActivity.this, messageDfActivity.class);
            intent.putExtra("message_data", m);
            startActivity(intent);
        }
        if (list.get(i).getAcceptType().equals("商品代购")) {
            Intent intent=new Intent(messageActivity.this,messageDgActivity.class);
            intent.putExtra("message_data",m);
            startActivity(intent);
            finish();
        }
        if (list.get(i).getAcceptType().equals("快递代拿")) {
                    Intent intent=new Intent(messageActivity.this,messageDnActivity.class);
                    intent.putExtra("message_data",m);
                    startActivity(intent);
                    finish();
        }

    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
