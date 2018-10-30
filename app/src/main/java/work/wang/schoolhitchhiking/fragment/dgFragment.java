package work.wang.schoolhitchhiking.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.accept_order.dgAcceptActivity;
import work.wang.schoolhitchhiking.adapter.dg_adapter;
import work.wang.schoolhitchhiking.order_object.dg;

/**
 * A simple {@link Fragment} subclass.
 */
public class dgFragment extends Fragment {
    private Handler mHandler;
    private ListView listView;
    private List<dg> dgList;
    private SwipeRefreshLayout swipeRefreshLayout;
    public dgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dg, container, false);
        init(view);
        initEvent();
        attainSendInfor();
        updateUI();
        return view;
    }
    private void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dg d=dgList.get(position);
                Intent intent=new Intent(getActivity(),dgAcceptActivity.class);
                intent.putExtra("dg_data",d);
                intent.putExtra("laiyuan",0);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable(){
                    public void run(){
                        if(isNetworkAvailable(getActivity())){
                            attainSendInfor();
                            Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(),"刷新失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
    }
    private void init(View view){
        listView=(ListView)view.findViewById(R.id.dg_list);
        listView.setEmptyView(view.findViewById(R.id.layout_noData));
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        listView.setAdapter(null);
                        dg_adapter dg=new dg_adapter(getActivity(),dgList);
                        listView.setAdapter(dg);
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"没有数据刷新试试",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void attainSendInfor(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/buyInfor")
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
