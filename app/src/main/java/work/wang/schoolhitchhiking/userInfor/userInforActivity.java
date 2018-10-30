package work.wang.schoolhitchhiking.userInfor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.userActivity;

public class userInforActivity extends AppCompatActivity {
    TextView infor_userName,infor_realName,infor_telNumber,infor_location,infor_money;
    Button infor_invest,infor_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_infor);
        init();
        initEvent();
        initstart();
    }
    private void init(){
        infor_userName=(TextView)this.findViewById(R.id.infor_userName);
        infor_realName=(TextView)this.findViewById(R.id.infor_realName);
        infor_telNumber=(TextView)this.findViewById(R.id.infor_telNumber);
        infor_location=(TextView)this.findViewById(R.id.infor_location);
        infor_money=(TextView)this.findViewById(R.id.infor_money);
        infor_back=(Button)this.findViewById(R.id.infor_back);
        infor_invest=(Button)this.findViewById(R.id.infor_revise);
    }
    private void initEvent(){
        infor_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userInforActivity.this,userActivity.class);
                startActivity(intent);
                finish();
            }
        });
        infor_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userInforActivity.this,investPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initstart(){
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        infor_userName.setText("我的昵称："+sp.getString("userName","1234"));
        infor_realName.setText("我的姓名："+sp.getString("realName","1234"));
        infor_telNumber.setText("手机号码："+sp.getString("telNumber","1234"));
        infor_location.setText("宿舍住址："+sp.getString("location","1234"));
        infor_money.setText("当前账户余额："+sp.getInt("money",100));
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
