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
import java.util.ArrayList;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.userInfor.user;

public class workerInforActivity extends AppCompatActivity {
    TextView infor_userName,infor_realName,infor_telNumber,infor_location;
    Button infor_back;
    ArrayList<user> userBeanList;
    private Handler mHandler;
    int i;
    String type="";
    message m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_worker_infor);
        i=getIntent().getIntExtra("workID",0);
        type=getIntent().getStringExtra("type");
        m=(message) getIntent().getSerializableExtra("message_data");
        init();
        initEvent();
        attainWorkerInfor();
        initstart();
    }
    private void initstart(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        infor_userName.setText("接单人昵称:"+userBeanList.get(0).userName);
                        infor_realName.setText("接单人姓名:"+userBeanList.get(0).realName);
                        infor_telNumber.setText("联系方式:"+userBeanList.get(0).telNumber);
                        infor_location.setText("宿舍住址:"+userBeanList.get(0).location);
                        break;
                    case 2:
                        Toast.makeText(workerInforActivity.this,"服务器获取出错",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void attainWorkerInfor(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",i+"")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/workerInfor")
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
    private void init(){
        infor_userName=(TextView)this.findViewById(R.id.infor_userName);
        infor_realName=(TextView)this.findViewById(R.id.infor_realName);
        infor_telNumber=(TextView)this.findViewById(R.id.infor_telNumber);
        infor_location=(TextView)this.findViewById(R.id.infor_location);
        infor_back=(Button)this.findViewById(R.id.infor_back);
    }
    private void initEvent(){
        infor_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("df")){
                    Intent intent=new Intent(workerInforActivity.this,messageDfActivity.class);
                    intent.putExtra("message_data", m);
                    startActivity(intent);
                    finish();
                }
                if(type.equals("dn")) {
                    Intent intent=new Intent(workerInforActivity.this,messageDnActivity.class);
                    intent.putExtra("message_data", m);
                    startActivity(intent);
                    finish();
                }
                if(type.equals("dg")){
                    Intent intent=new Intent(workerInforActivity.this,messageDgActivity.class);
                    intent.putExtra("message_data", m);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(type.equals("df")){
                Intent intent=new Intent(workerInforActivity.this,messageDfActivity.class);
                intent.putExtra("message_data", m);
                startActivity(intent);
                finish();
            }
            if(type.equals("dn")) {
                Intent intent=new Intent(workerInforActivity.this,messageDnActivity.class);
                intent.putExtra("message_data", m);
                startActivity(intent);
                finish();
            }
            if(type.equals("dg")){
                Intent intent=new Intent(workerInforActivity.this,messageDgActivity.class);
                intent.putExtra("message_data", m);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }
}
