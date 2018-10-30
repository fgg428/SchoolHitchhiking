package work.wang.schoolhitchhiking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.bill.bill;

public class bill_adapter extends BaseAdapter {
    private List<bill> billList;
    private View view;
    private Context mContext;
    private bill_adapter.ViewHolder viewHolder; //优化Listview

    public bill_adapter(Context mContext, List<bill> newsList) {
        this.billList = newsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return billList.size();
    }

    @Override
    public Object getItem(int position) {
        return billList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.bill_item,
                    null);
            viewHolder = new bill_adapter.ViewHolder();
            viewHolder.bill_img = (ImageView) view.findViewById(R.id.bill_img);
            viewHolder.bill_type = (TextView) view.findViewById(R.id.bill_type);
            viewHolder.bill_date = (TextView) view.findViewById(R.id.bill_date);
            viewHolder.bill_number = (TextView) view.findViewById(R.id.bill_number);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (bill_adapter.ViewHolder) view.getTag();
        }
        viewHolder.bill_type.setText(billList.get(position).getBillType());
        viewHolder.bill_date.setText(billList.get(position).getBillDate());
        String numberType="";
        if( billList.get(position).getType()==0){
           numberType="+";
            viewHolder.bill_number.setTextColor(Color.parseColor("#00FF00"));
        }else {
           numberType="-";
            viewHolder.bill_number.setTextColor(Color.parseColor("#EE0000"));
        }
        viewHolder.bill_number.setText( numberType+billList.get(position).getNumber());
        String type=billList.get(position).getBillType();
        // String sort[]={"微信充值","支付宝充值","积分兑换","饭堂带饭","快递代拿","商品代购"};
        if(type.equals("微信充值")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.weixin);
        }else if (type.equals("支付宝充值")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.zhifubao);
        }else if (type.equals("积分兑换")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.jifen);
        }else if (type.equals("饭堂带饭")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.food);
        }else if (type.equals("快递代拿")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.send);
        }else if (type.equals("商品代购")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.buy);
        }else if(type.equals("代餐订单退回")|| type.equals("代餐赏金")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.food);
        }else if(type.equals("代购订单退回")|| type.equals("代购赏金")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.buy);
        }else if(type.equals("代拿订单退回")|| type.equals("代餐赏金")){
            viewHolder.bill_img.setBackgroundResource(R.drawable.send);
        }else {
            viewHolder.bill_img.setBackgroundResource(R.drawable.userimg);
        }
        return view;
    }
    class ViewHolder{
        ImageView bill_img;
        TextView bill_type;
        TextView bill_date;
        TextView bill_number;
    }

}

