package work.wang.schoolhitchhiking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.order_object.dg;

public class dg_adapter extends BaseAdapter {

    private List<dg> dgList;
    private View view;
    private Context mContext;
    private ViewHolder viewHolder; //优化Listview

    public dg_adapter(Context mContext, List<dg> newsList) {
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
            viewHolder = new ViewHolder();
            viewHolder.dg_item_name = (TextView) view.findViewById(R.id.dg_item_name);
            viewHolder.dg_item_type = (TextView) view.findViewById(R.id.dg_item_type);
            viewHolder.dg_item_date = (TextView) view.findViewById(R.id.dg_item_date);
            viewHolder.dg_item_money = (TextView) view.findViewById(R.id.dg_item_money);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.dg_item_name.setText("发单人:" + dgList.get(position).getBuyerName());
        viewHolder.dg_item_type.setText("类型: " + dgList.get(position).getBuyType());
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
