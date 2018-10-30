package work.wang.schoolhitchhiking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.order_object.dg;

public class history_dg_adapter extends BaseAdapter {

    private List<dg> dgList;
    private View view;
    private Context mContext;
    private history_dg_adapter.ViewHolder viewHolder; //优化Listview

    public history_dg_adapter(Context mContext, List<dg> newsList) {
        this.dgList = newsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dgList.size();
    }

    @Override
    public Object getItem(int position) {
        return dgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.dg_item,
                    null);
            viewHolder = new history_dg_adapter.ViewHolder();
            viewHolder.dg_item_name = (TextView) view.findViewById(R.id.dg_item_name);
            viewHolder.dg_item_type = (TextView) view.findViewById(R.id.dg_item_type);
            viewHolder.dg_item_date = (TextView) view.findViewById(R.id.dg_item_date);
            viewHolder.dg_item_money = (TextView) view.findViewById(R.id.dg_item_money);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (history_dg_adapter.ViewHolder) view.getTag();
        }
        viewHolder.dg_item_name.setText("类型: " + dgList.get(position).getBuyType());
        int i=dgList.get(position).getTasked();
        if(i==0){
            viewHolder.dg_item_type.setText("状态:求接中" );
            viewHolder.dg_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else if(i==1){
            viewHolder.dg_item_type.setText("状态:进行中" );
            viewHolder.dg_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else if(i==5){
            viewHolder.dg_item_type.setText("状态:未完成" );
            viewHolder.dg_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else if(i==6){
            viewHolder.dg_item_type.setText("状态:已完成" );
            viewHolder.dg_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else {
            viewHolder.dg_item_type.setText("状态:结算中" );
            viewHolder.dg_item_type.setTextColor(Color.parseColor("#EE7600"));
        }
        viewHolder.dg_item_date.setText("时间:" + dgList.get(position).getBuyDate());
        viewHolder.dg_item_money.setText("赏金:" + dgList.get(position).getPay());
        return view;
    }
    class ViewHolder{
        TextView dg_item_name;
        TextView dg_item_type;
        TextView dg_item_date;
        TextView dg_item_money;
    }
}
