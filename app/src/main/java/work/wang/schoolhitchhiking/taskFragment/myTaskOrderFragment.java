package work.wang.schoolhitchhiking.taskFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import work.wang.schoolhitchhiking.show.messageActivity;

import static android.content.Context.MODE_PRIVATE;


public class myTaskOrderFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Handler mHandler;
    private List<message> list;
    private ListView listView;
    int id;
    public myTaskOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_task_order, container, false);
        SharedPreferences sp=getActivity().getSharedPreferences("data",MODE_PRIVATE);
        id=sp.getInt("userID",1);

        listView=(ListView)view.findViewById(R.id.lv);
        listView.setEmptyView(view.findViewById(R.id.layout_noData));
        listView.setOnItemClickListener(this);
        attainMessage();
        updateUI();
        return view;
    }
    private void updateUI(){
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        message_adapter m=new message_adapter(getActivity(),list);
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
    private void attainMessage(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client=new OkHttpClient();
                RequestBody formBody=new FormBody.Builder()
                        .add("userID",id+"")
                        .build();
                Request request=new Request.Builder().url("http://172.17.149.206:8080/hitchhiking/myTaskOrder")
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        message m = list.get(i);
        if (list.get(i).getAcceptType().equals("快餐代送")) {
            Intent intent = new Intent(getActivity(), taskOrderDf.class);
            intent.putExtra("message_data", m);
            startActivity(intent);
            getActivity().onBackPressed();
        }
        if (list.get(i).getAcceptType().equals("商品代购")) {
            Intent intent=new Intent(getActivity(),taskOrderDg.class);
            intent.putExtra("message_data",m);
            startActivity(intent);
            getActivity().onBackPressed();
        }
        if (list.get(i).getAcceptType().equals("快递代拿")) {
            Intent intent=new Intent(getActivity(),taskOrderDn.class);
            intent.putExtra("message_data",m);
            startActivity(intent);
            getActivity().onBackPressed();
        }
    }
}
