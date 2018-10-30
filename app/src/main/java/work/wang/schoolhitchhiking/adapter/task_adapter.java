package work.wang.schoolhitchhiking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.taskFragment.tasking;

public class task_adapter extends BaseAdapter {
    private List<tasking> taskList;
    private View view;
    private Context mContext;
    private task_adapter.ViewHolder viewHolder; //优化Listview

    public task_adapter(Context mContext,  List<tasking> taskList) {
        this.taskList = taskList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tasking_item,
                    null);
            viewHolder = new task_adapter.ViewHolder();
            viewHolder.df_item_type = (TextView) view.findViewById(R.id.tasking_item_type);
            viewHolder.df_item_date = (TextView) view.findViewById(R.id.tasking_item_date);
            viewHolder.df_item_money = (TextView) view.findViewById(R.id.tasking_item_money);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (task_adapter.ViewHolder) view.getTag();
        }
        viewHolder.df_item_type.setText("类型: " + taskList.get(position).getType());
        viewHolder.df_item_date.setText("时间:" + taskList.get(position).getDate());
        viewHolder.df_item_money.setText("赏金:" + taskList.get(position).getMoney());
        return view;
    }
    class ViewHolder{
        TextView df_item_type;
        TextView df_item_date;
        TextView df_item_money;
    }
}
