package work.wang.schoolhitchhiking.userInfor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.order.dfActivity;
import work.wang.schoolhitchhiking.order.dgActivity;
import work.wang.schoolhitchhiking.order.kdActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class userOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private Button order_kd,order_sp,order_cf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_order);
        init();
        initEvent();
    }
    private void init(){
        order_kd=(Button)this.findViewById(R.id.order_kd);
        order_sp=(Button)this.findViewById(R.id.order_sp);
        order_cf=(Button)this.findViewById(R.id.order_cf);
    }
    private void initEvent(){
        order_kd.setOnClickListener(this);
        order_sp.setOnClickListener(this);
        order_cf.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_kd:
                Intent intent=new Intent(userOrderActivity.this,kdActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.order_sp:
                Intent i=new Intent(userOrderActivity.this,dgActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.order_cf:
                Intent i1=new Intent(userOrderActivity.this,dfActivity.class);
                startActivity(i1);
                finish();
                break;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,userActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
