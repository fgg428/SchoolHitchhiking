package work.wang.schoolhitchhiking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.order_object.df;

public class df_adapter extends BaseAdapter {
    private List<df> dfList;
    private View view;
    private Context mContext;
    private df_adapter.ViewHolder viewHolder; //优化Listview

    public df_adapter(Context mContext, List<df> newsList) {
        this.dfList = newsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dfList.size();
    }

    @Override
    public Object getItem(int position) {
        return dfList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.df_item,
                    null);
            viewHolder = new df_adapter.ViewHolder();
            viewHolder.df_item_name = (TextView) view.findViewById(R.id.df_item_name);
            viewHolder.df_item_type = (TextView) view.findViewById(R.id.df_item_type);
            viewHolder.df_item_date = (TextView) view.findViewById(R.id.df_item_date);
            viewHolder.df_item_money = (TextView) view.findViewById(R.id.df_item_money);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (df_adapter.ViewHolder) view.getTag();
        }
        viewHolder.df_item_name.setText("发单人:" + dfList.get(position).getBuyerName());
        viewHolder.df_item_type.setText("类型: " + dfList.get(position).getFoodType());
        viewHolder.df_item_date.setText("时间:" + dfList.get(position).getFoodDate());
        viewHolder.df_item_money.setText("赏金:" + dfList.get(position).getPay());
        return view;
    }
    class ViewHolder{
        TextView df_item_name;
        TextView df_item_type;
        TextView df_item_date;
        TextView df_item_money;
    }

}
