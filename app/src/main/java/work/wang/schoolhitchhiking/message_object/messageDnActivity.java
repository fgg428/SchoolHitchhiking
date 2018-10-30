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
import work.wang.schoolhitchhiking.order_object.kd;
import work.wang.schoolhitchhiking.show.messageActivity;

public class messageDnActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private List<kd> dnList;
    private TextView dn_accept_money,dn_accept_locationType,dn_accept_company,dn_accept_name,dn_accept_phone;
    private TextView dn_accept_goodsType,dn_accept_goodsWeight,dn_accept_date,dn_accept_location,dn_accept_state;
    private Button dn_accept_back,dn_accept_accept;
    private int tblID,workID;
    private message m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message_dn);
        getInfor();
        init();
        initEvent2();
        attainDn();
        updateUI();
    }
    private void attainDn(){
    new Thread(new Runnable() {
        public void run() {
            OkHttpClient client=new OkHttpClient();
            RequestBody formBody=new FormBody.Builder()
                    .add("tblID",tblID+"")
                    .build();
            Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/sendInforMessage")
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
                dnList = new ArrayList<>();
                for (JsonElement n : jsonArray) {
                    //使用GSON，直接转成Bean对象
                    kd k=gson.fromJson(n,kd.class);
                    dnList.add(k);
                }
                if (dnList!=null){
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
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        startUpdate();
                        break;
                    case 2:
                        Toast.makeText(messageDnActivity.this,"出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(messageDnActivity.this,"订单已完成,谢谢",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(messageDnActivity.this,messageActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        Toast.makeText(messageDnActivity.this,"请求失败,服务器出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void startUpdate(){
        dn_accept_money.setText(dnList.get(0).getPay()+"");
        dn_accept_locationType.setText(dnList.get(0).getLocationType());
        dn_accept_company.setText(dnList.get(0).getSendCompany());
        dn_accept_name.setText(dnList.get(0).getContactPerson());
        dn_accept_phone.setText(dnList.get(0).getPhone());
        dn_accept_goodsType.setText(dnList.get(0).getGoodsType());
        dn_accept_goodsWeight.setText(dnList.get(0).getGoodsWeight()+"");
        dn_accept_date.setText(dnList.get(0).getSendDate());
        dn_accept_location.setText(dnList.get(0).getSendLocation());
        dn_accept_state.setText(dnList.get(0).getState());
    }
    private void getInfor(){
        m=(message) getIntent().getSerializableExtra("message_data");
        tblID=m.getTblID();
        workID=m.getWorkID();
    }
    private void init(){
        dn_accept_money=(TextView)this.findViewById(R.id.dn_accept_money);
        dn_accept_locationType=(TextView)this.findViewById(R.id.dn_accept_locationType);
        dn_accept_company=(TextView)this.findViewById(R.id.dn_accept_company);
        dn_accept_name=(TextView)this.findViewById(R.id.dn_accept_name);
        dn_accept_phone=(TextView)this.findViewById(R.id.dn_accept_phone);
        dn_accept_goodsType=(TextView)this.findViewById(R.id.dn_accept_goodsType);
        dn_accept_goodsWeight=(TextView)this.findViewById(R.id.dn_accept_goodsWeight);
        dn_accept_date=(TextView)this.findViewById(R.id.dn_accept_date);
        dn_accept_location=(TextView)this.findViewById(R.id.dn_accept_location);
        dn_accept_state=(TextView)this.findViewById(R.id.dn_accept_state);
        dn_accept_back=(Button) this.findViewById(R.id.dn_accept_back);
        dn_accept_accept=(Button)this.findViewById(R.id.dn_accept_accept);
    }
    private void initEvent2(){
        dn_accept_back.setOnClickListener(this);
        dn_accept_accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dn_accept_back:
                Intent i=new Intent(this,workerInforActivity.class);
                i.putExtra("workID",workID);
                i.putExtra("type","dn");
                i.putExtra("message_data", m);
                startActivity(i);
                finish();
                break;
            case R.id.dn_accept_accept:
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
                        .url("http://172.17.149.206:8080/hitchhiking/dnComplete")
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
