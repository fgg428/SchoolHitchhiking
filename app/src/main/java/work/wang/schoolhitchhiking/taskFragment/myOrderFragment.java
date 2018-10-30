package work.wang.schoolhitchhiking.taskFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import work.wang.schoolhitchhiking.adapter.df_adapter;
import work.wang.schoolhitchhiking.adapter.history_df_adapter;
import work.wang.schoolhitchhiking.adapter.history_dg_adapter;
import work.wang.schoolhitchhiking.adapter.history_dn_adapter;
import work.wang.schoolhitchhiking.adapter.task_adapter;
import work.wang.schoolhitchhiking.order_object.df;
import work.wang.schoolhitchhiking.order_object.dg;
import work.wang.schoolhitchhiking.order_object.kd;
import work.wang.schoolhitchhiking.userInfor.userHistoryOrderctivity;

import static android.content.Context.MODE_PRIVATE;

public class myOrderFragment extends Fragment implements AdapterView.OnItemClickListener {
    private TextView tv;
    private ListView listView;
    private Handler mHandler;
    private List<df> dfList;
    private List<kd> dnList;
    private List<dg> dgList;
    private String sort[]={"代购","代拿","代餐"};
    int userID;
    public myOrderFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_order, container, false);
        SharedPreferences sp=getActivity().getSharedPreferences("data",MODE_PRIVATE);
        userID=sp.getInt("userID",1);
        listView=(ListView) view.findViewById(R.id.lv);
        listView.setEmptyView(view.findViewById(R.id.layout_noData));
        tv=(TextView)view.findViewById(R.id.tv);
        initEvent();
        attinDf();
        updateUI();
        return view;
    }
    private void initEvent(){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectType();
            }
        });
        listView.setOnItemClickListener(this);
    }
    private void selectType(){
        AlertDialog.Builder incomeAlertDialog=new AlertDialog.Builder(getActivity());
        incomeAlertDialog.setSingleChoiceItems(sort, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                // which表示点击的条目
                Object checkedItem = lw.getAdapter().getItem(which);
                // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                dialog.dismiss();
                // 更新你的view
                if(((String)checkedItem).equals("代购")){
                    tv.setText("正在发的订单:代购");
                    attainDg();
                    updateUI();
                }
                if(((String)checkedItem).equals("代拿")){
                    tv.setText("正在发的订单:代拿");
                    attainDn();
                    updateUI();

                }
                if(((String)checkedItem).equals("代餐")){
                    tv.setText("正在发的订单:代餐");
                    attinDf();
                    updateUI();

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
                        history_dn_adapter dn=new history_dn_adapter(getActivity(),dnList);
                        listView.setAdapter(dn);
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"系统出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        listView.setAdapter(null);
                        history_df_adapter df=new history_df_adapter(getActivity(),dfList);
                        listView.setAdapter(df);
                        break;
                    case 4:
                        listView.setAdapter(null);
                        history_dg_adapter dg=new history_dg_adapter(getActivity(),dgList);
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
                        .add("userID",userID+"")
                        .add("type","1")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/myorder")
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
                        .add("userID",userID+"")
                        .add("type","2")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/myorder")
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
                        .add("userID",userID+"")
                        .add("type","3")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/myorder")
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(tv.getText().toString().equals("正在发的订单:代餐")){
            Intent intent=new Intent(getActivity(),taskingDf.class);
            intent.putExtra("df",dfList.get(i));
            startActivity(intent);
        }
        if(tv.getText().toString().equals("正在发的订单:代拿")){
            Intent intent=new Intent(getActivity(),taskingDn.class);
            intent.putExtra("dn",dnList.get(i));
            startActivity(intent);
        }
        if(tv.getText().toString().equals("正在发的订单:代购")){
            Intent intent=new Intent(getActivity(),taskingDg.class);
            intent.putExtra("dg",dgList.get(i));
            startActivity(intent);
        }
    }
}

