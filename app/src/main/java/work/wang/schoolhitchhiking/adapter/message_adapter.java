package work.wang.schoolhitchhiking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.message_object.message;

public class message_adapter extends BaseAdapter {

    private List<message> list;
    private View view;
    private Context mContext;
    private message_adapter.ViewHolder viewHolder; //优化Listview
    public message_adapter(Context mContext, List<message> newsList) {
        this.list = newsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.message_item,
                    null);
            viewHolder = new message_adapter.ViewHolder();
            viewHolder.message_img = (ImageView) view.findViewById(R.id.message_item_img);
            viewHolder.message_type = (TextView) view.findViewById(R.id.message_item_type);
            viewHolder.message_money = (TextView) view.findViewById(R.id.message_item_money);
            viewHolder.message_date = (TextView) view.findViewById(R.id.message_item_date);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (message_adapter.ViewHolder) view.getTag();
        }
        viewHolder.message_type.setText("类型:"+list.get(position).getAcceptType());
        String img=list.get(position).getAcceptType();
        if(img.equals("商品代购")){
           viewHolder.message_img.setBackgroundResource(R.drawable.buy);
        }else if (img.equals("快递代拿")){
            viewHolder.message_img.setBackgroundResource(R.drawable.send);
        }else{
            viewHolder.message_img.setBackgroundResource(R.drawable.food);
        }
        viewHolder.message_money.setText("本次赏金:"+list.get(position).getMoney());
        viewHolder.message_date.setText(list.get(position).getAcceptDate());
        return view;
    }
    class ViewHolder{
        ImageView message_img;
        TextView message_type;
        TextView message_money;
        TextView message_date;
    }

}
