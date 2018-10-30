package work.wang.schoolhitchhiking.message_object;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.order_object.dg;
import work.wang.schoolhitchhiking.show.messageActivity;

public class messageDgActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private List<dg> dgList;
    TextView dg_accept_money,dg_accept_type,dg_accept_date,dg_accept_name,dg_accept_phone,dg_accept_location;
    TextView dg_accept_state;
    Button dg_accept_back,dg_accept_accept;
    private int tblID,workID;
    private message m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message_dg);
        getInfor();
        init();
        initEvent();
        attainDg();
        updateUI();
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
                        Toast.makeText(messageDgActivity.this,"出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(messageDgActivity.this,"订单已完成,谢谢",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(messageDgActivity.this,messageActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        Toast.makeText(messageDgActivity.this,"请求失败,服务器出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void startUpdate(){
        dg_accept_money.setText(dgList.get(0).getPay()+"");
        dg_accept_type.setText(dgList.get(0).getBuyType());
        dg_accept_date.setText(dgList.get(0).getBuyDate());
        dg_accept_name.setText(dgList.get(0).getBuyerName());
        dg_accept_phone.setText(dgList.get(0).getBuyerPhone());
        dg_accept_location.setText(dgList.get(0).getBuyerLocation());
        dg_accept_state.setText(dgList.get(0).getStatement());
    }
    private void attainDg(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("tblID",tblID+"")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/buyInforMessage")
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
                    dgList = new ArrayList<>();
                    for (JsonElement n : jsonArray) {
                        //使用GSON，直接转成Bean对象
                        dg k=gson.fromJson(n,dg.class);
                        dgList.add(k);
                    }
                    if (dgList!=null){
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
    private void getInfor(){
        m=(message) getIntent().getSerializableExtra("message_data");
        tblID=m.getTblID();
        workID=m.getWorkID();
    }
    private void init(){
        dg_accept_money=(TextView)this.findViewById(R.id.dg_accept_money);
        dg_accept_type=(TextView)this.findViewById(R.id.dg_accept_type);
        dg_accept_date=(TextView)this.findViewById(R.id.dg_accept_date);
        dg_accept_name=(TextView)this.findViewById(R.id.dg_accept_name);
        dg_accept_phone=(TextView)this.findViewById(R.id.dg_accept_phone);
        dg_accept_location=(TextView)this.findViewById(R.id.dg_accept_location);
        dg_accept_state=(TextView)this.findViewById(R.id.dg_accept_state);
        dg_accept_back=(Button)this.findViewById(R.id.dg_accept_back);
        dg_accept_accept=(Button)this.findViewById(R.id.dg_accept_accept);
    }
    private void initEvent(){
        dg_accept_back.setOnClickListener(this);
        dg_accept_accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dg_accept_back:
                Intent i=new Intent(this,workerInforActivity.class);
                i.putExtra("workID",workID);
                i.putExtra("type","dg");
                i.putExtra("message_data", m);
                startActivity(i);
                finish();
                break;
            case R.id.dg_accept_accept:
                orderComplete();
                break;
        }
    }
    private void orderComplete(){
        new Thread(new Runnable() {
            @Override
            // private String rg_selected,money,userName,phone,location,state,date;
            public void run() {
                okhttp3.OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("tblID",tblID+"")
                        .build();
                final Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/dgComplete")
                        .post(formBody)
                        .build();
                try {
                    Response response=client.newCall(request).execute();
                    if (!response.isSuccessful()){
                        Message message = mHandler.obtainMessage();
                        message.what = 4;
                        mHandler.sendMessage(message);
                    }else {
                        String s=response.body().string();
                        if(s.equals("success")){
                            Message message = mHandler.obtainMessage();
                            message.what = 3;
                            mHandler.sendMessage(message);
                        }else {
                            Message message = mHandler.obtainMessage();
                            message.what = 4;
                            mHandler.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,messageActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
