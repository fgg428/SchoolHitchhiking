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
import android.widget.ImageView;
import android.widget.ListView;

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
import work.wang.schoolhitchhiking.show.messageActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class userHistoryAcceptOrderActivity extends AppCompatActivity {
    private ListView listView;
    private Handler mHandler;
    private List<message> list;
    private ImageView history_back;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_history_accept_order);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        id=sp.getInt("userID",1);
        init();
        attainAcceptOrder();
        updateUI();
    }
    private void init(){
        history_back=(ImageView)this.findViewById(R.id.history_back);
        listView=(ListView)this.findViewById(R.id.history_lv);
        listView.setEmptyView(this.findViewById(R.id.layout_noData));
        history_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userHistoryAcceptOrderActivity.this,selectHistoryOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        message_adapter m=new message_adapter(userHistoryAcceptOrderActivity.this,list);
                        listView.setAdapter(m);
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void attainAcceptOrder(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",id+"")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/acceptOrderHistory")
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
                    if (list!=null && !list.isEmpty()){
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
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,selectHistoryOrderActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
