package work.wang.schoolhitchhiking.taskFragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import work.wang.schoolhitchhiking.message_object.message;
import work.wang.schoolhitchhiking.order_object.df;
import work.wang.schoolhitchhiking.show.taskActivity;

public class taskOrderDf extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private List<df> dfList;
    private TextView df_accept_money,df_accept_foodType,df_accept_foodLocation,df_accept_name,df_accept_phone,df_accept_location,df_accept_state;
    private Button df_accept_back;
    private int tblID,workID;
    private message m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_order_df);
        getInfor();
        init();
        initEvent();
        attainDf();
        updateUI();
    }
    private void getInfor(){
        m=(message) getIntent().getSerializableExtra("message_data");
        tblID=m.getTblID();
        workID=m.getWorkID();
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        startUpdate();
                        break;
                    case 2:
                        Toast.makeText(taskOrderDf.this,"出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void startUpdate(){
        df_accept_money.setText(dfList.get(0).getPay()+"");
        df_accept_foodType.setText(dfList.get(0).getFoodType());
        df_accept_foodLocation.setText(dfList.get(0).getFoodLocationType());
        df_accept_name.setText(dfList.get(0).getBuyerName());
        df_accept_phone.setText(dfList.get(0).getBuyerPhone());
        df_accept_location.setText(dfList.get(0).getBuyerLocation());
        df_accept_state.setText(dfList.get(0).getStatement());
    }
    private void attainDf(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("tblID",tblID+"")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/foodInforMessage")
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
                    dfList = new ArrayList<>();
                    for (JsonElement n : jsonArray) {
                        //使用GSON，直接转成Bean对象
                        df k=gson.fromJson(n,df.class);
                        dfList.add(k);
                    }
                    if (dfList!=null){
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
        df_accept_money=(TextView)this.findViewById(R.id.df_accept_money);
        df_accept_foodType=(TextView)this.findViewById(R.id.df_accept_foodType);
        df_accept_foodLocation=(TextView)this.findViewById(R.id.df_accept_foodLocation);
        df_accept_name=(TextView)this.findViewById(R.id.df_accept_name);
        df_accept_phone=(TextView)this.findViewById(R.id.df_accept_phone);
        df_accept_location=(TextView)this.findViewById(R.id.df_accept_location);
        df_accept_state=(TextView)this.findViewById(R.id.df_accept_state);
        df_accept_back=(Button)this.findViewById(R.id.df_accept_back);
    }
    private void initEvent(){
        df_accept_back.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.df_accept_back:
                Intent intent=new Intent(this,taskActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,taskActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
