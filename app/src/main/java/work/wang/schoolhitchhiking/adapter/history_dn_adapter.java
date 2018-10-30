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
import work.wang.schoolhitchhiking.order_object.kd;

public class history_dn_adapter extends BaseAdapter {

    private List<kd> dnList;
    private View view;
    private Context mContext;
    private ViewHolder viewHolder; //优化Listview

    public history_dn_adapter(Context mContext, List<kd> newsList) {
        this.dnList = newsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dnList.size();
    }

    @Override
    public Object getItem(int position) {
        return dnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.dn_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.dn_item_location = (TextView) view.findViewById(R.id.dn_item_location);
            viewHolder.dn_item_type = (TextView) view.findViewById(R.id.dn_item_type);
            viewHolder.dn_item_date = (TextView) view.findViewById(R.id.dn_item_date);
            viewHolder.dn_item_money = (TextView) view.findViewById(R.id.dn_item_money);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.dn_item_location.setText("类型: " + dnList.get(position).getGoodsType());
        int i=dnList.get(position).getTasked();
        if(i==0){
            viewHolder.dn_item_type.setText("状态:求接中" );
            viewHolder.dn_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else if(i==1){
            viewHolder.dn_item_type.setText("状态:进行中" );
            viewHolder.dn_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else if(i==5){
            viewHolder.dn_item_type.setText("状态:未完成" );
            viewHolder.dn_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else if(i==6){
            viewHolder.dn_item_type.setText("状态:已完成" );
            viewHolder.dn_item_type.setTextColor(Color.parseColor("#EE7600"));
        }else {
            viewHolder.dn_item_type.setText("状态:结算中" );
            viewHolder.dn_item_type.setTextColor(Color.parseColor("#EE7600"));
        }
        viewHolder.dn_item_date.setText("时间:" + dnList.get(position).getSendDate());
        viewHolder.dn_item_money.setText("赏金:" + dnList.get(position).getPay());
        return view;
    }
    class ViewHolder{
        TextView dn_item_location;
        TextView dn_item_type;
        TextView dn_item_date;
        TextView dn_item_money;
    }

}
