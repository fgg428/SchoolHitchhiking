package work.wang.schoolhitchhiking.show;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.taskFragment.myOrderFragment;
import work.wang.schoolhitchhiking.taskFragment.myTaskOrderFragment;
import work.wang.schoolhitchhiking.userInfor.user;

public class taskActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Fragment> fragmentList;
    ArrayList<work.wang.schoolhitchhiking.userInfor.user> userBeanList;
    private Handler mHandler;
    private long exitTime=System.currentTimeMillis();
    private ImageButton show,shopcart,task,message,user;
    private TextView tv_task;
    private TextView tasking,task_order;
    private ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task);
        init();
        initEvent();
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new myOrderFragment());
        fragments.add(new myTaskOrderFragment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (vp.getCurrentItem()){
                    case 0:
                        changeBgColor(0);
                        break;
                    case 1:
                        changeBgColor(1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void changeBgColor(int i){
        if(i==0){
            tasking.setBackgroundColor(Color.parseColor("#FFA500"));
            task_order.setBackgroundColor(Color.parseColor("#B7B7B7"));
        }
        if(i==1){
            tasking.setBackgroundColor(Color.parseColor("#B7B7B7"));
            task_order.setBackgroundColor(Color.parseColor("#FFA500"));
        }
    }
    private void init(){
        tv_task=(TextView)this.findViewById(R.id.bottom_tv_task);
        tv_task.setTextColor(Color.parseColor("#00EE00"));
        show=(ImageButton)this.findViewById(R.id.bottom_show);
        shopcart=(ImageButton)this.findViewById(R.id.bottom_shopcart);
        task=(ImageButton)this.findViewById(R.id.bottom_task);
        message=(ImageButton)this.findViewById(R.id.bottom_message);
        user=(ImageButton)this.findViewById(R.id.bottom_user);
        tasking=(TextView)this.findViewById(R.id.task_order);
        task_order=(TextView)this.findViewById(R.id.task_task_order);
        vp=(ViewPager)this.findViewById(R.id.task_vp);
    }
    private void initEvent(){
        show.setOnClickListener(this);
        shopcart.setOnClickListener(this);
        task.setOnClickListener(this);
        message.setOnClickListener(this);
        user.setOnClickListener(this);
        tasking.setOnClickListener(this);
        task_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.task_order:
                vp.setCurrentItem(0);
                changeBgColor(0);
                break;
            case R.id.task_task_order:
                vp.setCurrentItem(1);
                changeBgColor(1);
                break;
            case R.id.bottom_show:
                intent=new Intent(taskActivity.this,showActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_shopcart:
                intent=new Intent(taskActivity.this,shopcartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_task:
                intent=new Intent(taskActivity.this,taskActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_message:
                intent=new Intent(taskActivity.this,messageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bottom_user:
                intent=new Intent(taskActivity.this,userActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {

            if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
            {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public class FragAdapter extends FragmentPagerAdapter {
        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            fragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
