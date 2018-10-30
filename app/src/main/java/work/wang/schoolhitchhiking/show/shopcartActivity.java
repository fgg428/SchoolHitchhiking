package work.wang.schoolhitchhiking.show;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import work.wang.schoolhitchhiking.accept_order.dfAcceptActivity;
import work.wang.schoolhitchhiking.accept_order.dgAcceptActivity;
import work.wang.schoolhitchhiking.accept_order.kdAcceptActivity;
import work.wang.schoolhitchhiking.adapter.history_df_adapter;
import work.wang.schoolhitchhiking.adapter.history_dg_adapter;
import work.wang.schoolhitchhiking.adapter.history_dn_adapter;
import work.wang.schoolhitchhiking.order_object.df;
import work.wang.schoolhitchhiking.order_object.dg;
import work.wang.schoolhitchhiking.order_object.kd;

public class shopcartActivity extends AppCompatActivity implements View.OnClickListener {
    private long exitTime=System.currentTimeMillis();
    private ImageButton show,shopcart,task,message,user;
    private TextView tv_shopcart,tv1,tv2;
    private ListView listView;
    private String selectType="";
    private String selectAll="";
    private String sort1[]={"代购","代拿","代餐"};
    private String sortDg[]={"书籍","药品","日用品","其它"};
    private String sortDf[]={"早餐","午餐","晚餐","夜宵"};
    private String sortDn[]={"3号门","绿岛","丰巢","其它"};
    private String type[]={};
    private Handler mHandler;
    private List<df> dfList;
    private List<kd> dnList;
    private List<dg> dgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopcart);
        init();
        initEvent();
        updateUI();
    }
    private void init(){
        listView=(ListView)this.findViewById(R.id.listview);
        listView.setEmptyView(this.findViewById(R.id.layout_noData));
        tv_shopcart=(TextView)this.findViewById(R.id.bottom_tv_shopcart);
        tv_shopcart.setTextColor(Color.parseColor("#00EE00"));
        tv1=(TextView)this.findViewById(R.id.s_tv1);
        tv2=(TextView)this.findViewById(R.id.s_tv2);
        show=(ImageButton)this.findViewById(R.id.bottom_show);
        shopcart=(ImageButton)this.findViewById(R.id.bottom_shopcart);
        task=(ImageButton)this.findViewById(R.id.bottom_task);
        message=(ImageButton)this.findViewById(R.id.bottom_message);
        user=(ImageButton)this.findViewById(R.id.bottom_user);
    }
    private void initEvent(){
        show.setOnClickListener(this);
        shopcart.setOnClickListener(this);
        task.setOnClickListener(this);
        message.setOnClickListener(this);
        user.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectType.equals("代购")){
                    dg d=dgList.get(i);
                    Intent intent=new Intent(shopcartActivity.this,dgAcceptActivity.class);
                    intent.putExtra("dg_data",d);
                    intent.putExtra("laiyuan",1);
                    startActivity(intent);
                    finish();
                }
                if(selectType.equals("代拿")){
                    kd d=dnList.get(i);
                    Intent intent=new Intent(shopcartActivity.this,kdAcceptActivity.class);
                    intent.putExtra("dn_data",d);
                    intent.putExtra("laiyuan",1);
                    startActivity(intent);
                }
                if(selectType.equals("代餐")){
                    df d=dfList.get(i);
                    Intent intent=new Intent(shopcartActivity.this,dfAcceptActivity.class);
                    intent.putExtra("df_data",d);
                    intent.putExtra("laiyuan",1);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.bottom_show:
                intent=new Intent(shopcartActivity.this,showActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_shopcart:
//                intent=new Intent(shopcartActivity.this,shopcartActivity.class);
//                startActivity(intent);
//                finish();
                break;
            case R.id.bottom_task:
                intent=new Intent(shopcartActivity.this,taskActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_message:
                intent=new Intent(shopcartActivity.this,messageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_user:
                intent=new Intent(shopcartActivity.this,userActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.s_tv1:
                select1();
                break;
            case R.id.s_tv2:
                if(selectType.equals("")){
                    Toast.makeText(this,"请先选择类别哦",Toast.LENGTH_SHORT).show();
                }else {
                    select2();
                }
                break;
        }
    }
    private void select1(){
        AlertDialog.Builder incomeAlertDialog=new AlertDialog.Builder(shopcartActivity.this);
        incomeAlertDialog.setSingleChoiceItems(sort1, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                // which表示点击的条目
                Object checkedItem = lw.getAdapter().getItem(which);
                // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                dialog.dismiss();
//                sortDg[]={"书籍","药品","日用品","其它"};
//                private String sortDf[]={"早餐","午餐","晚餐","夜宵"};
//                private String sortDn[]={"三号门","绿岛","丰巢","其它"};
                // 更新你的view

                if(((String)checkedItem).equals("代购")){
                    tv1.setText("类型:代购");
                    selectType="代购";
                }
                if(((String)checkedItem).equals("代拿")){
                    tv1.setText("类别:代拿");
                    selectType="代拿";
                }
                if(((String)checkedItem).equals("代餐")){
                    tv1.setText("类别:代餐");
                    selectType="代餐";
                }
            }
        });
        AlertDialog dialog = incomeAlertDialog.create();
        dialog.show();
    }
    private void select2(){
        if(selectType.equals("代购")){
            type=sortDg;
        }else if(selectType.equals("代拿")){
           type=sortDn;
        }else{
            type=sortDf;
        }
        AlertDialog.Builder incomeAlertDialog=new AlertDialog.Builder(shopcartActivity.this);
        incomeAlertDialog.setSingleChoiceItems(type, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                // which表示点击的条目
                Object checkedItem = lw.getAdapter().getItem(which);
                // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                dialog.dismiss();
                // 更新你的view
                if(((String)checkedItem).equals("书籍")){
                    tv2.setText("类型:书籍");
                    selectAll="书籍";
                    attainDg();
                }
                if(((String)checkedItem).equals("药品")){
                    tv2.setText("类型:药品");
                    selectAll="药品";
                    attainDg();
                }
                if(((String)checkedItem).equals("日用品")){
                    tv2.setText("类型:日用品");
                    selectAll="日用品";
                    attainDg();
                }
                if(((String)checkedItem).equals("其它")){
                    tv2.setText("类型:其它");
                    selectAll="其它";
                   if(selectType.equals("代购")){
                       attainDg();
                   }else {
                       attainDn();
                   }
                }
                if(((String)checkedItem).equals("早餐")){
                    tv2.setText("类型:早餐");
                    selectAll="早餐";
                    attinDf();
                }
                if(((String)checkedItem).equals("午餐")){
                    tv2.setText("类型:午餐");
                    selectAll="午餐";
                    attinDf();
                }
                if(((String)checkedItem).equals("晚餐")){
                    tv2.setText("类型:晚餐");
                    selectAll="晚餐";
                    attinDf();
                }

                if(((String)checkedItem).equals("夜宵")){
                    tv2.setText("类型:夜宵");
                    selectAll="夜宵";
                    attinDf();
                }
                if(((String)checkedItem).equals("3号门")){
                    tv2.setText("类型:3号门");
                    selectAll="3号门";
                    attainDn();
                }
                if(((String)checkedItem).equals("绿岛")){
                    tv2.setText("类型:绿岛");
                    selectAll="绿岛";
                    attainDn();
                }
                if(((String)checkedItem).equals("丰巢")){
                    tv2.setText("类型:丰巢");
                    selectAll="丰巢";
                    attainDn();
                }
            }
        });
        AlertDialog dialog = incomeAlertDialog.create();
        dialog.show();
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        listView.setAdapter(null);
                        history_dn_adapter dn=new history_dn_adapter(shopcartActivity.this,dnList);
                        listView.setAdapter(dn);
                        break;
                    case 2:
                        Toast.makeText(shopcartActivity.this,"系统出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        listView.setAdapter(null);
                        history_df_adapter df=new history_df_adapter(shopcartActivity.this,dfList);
                        listView.setAdapter(df);
                        break;
                    case 4:
                        listView.setAdapter(null);
                        history_dg_adapter dg=new history_dg_adapter(shopcartActivity.this,dgList);
                        listView.setAdapter(dg);
                    default:
                        break;
                }
            }
        };
    }
    private void attainDg(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("type","1")
                        .add("typeAll",selectAll)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/seekOrder")
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
                        message.what = 4;
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
    private void attainDn(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("type","2")
                        .add("typeAll",selectAll)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/seekOrder")
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
    private void attinDf(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("type","3")
                        .add("typeAll",selectAll)
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/seekOrder")
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
                        message.what = 3;
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
}
