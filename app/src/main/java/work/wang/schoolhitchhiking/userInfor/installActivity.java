package work.wang.schoolhitchhiking.userInfor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import work.wang.schoolhitchhiking.MainActivity;
import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.userActivity;

public class installActivity extends AppCompatActivity {
    private TextView back;
    private LinearLayout look,pay,login;
    private Switch aSwitch;
    int noLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_install);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        noLogin=sp.getInt("noLogin",0);
        init();
        initEvent();
    }
    private void init(){
        back=(TextView) this.findViewById(R.id.install_back);
        look=this.findViewById(R.id.install_look);
        pay=this.findViewById(R.id.install_pay);
        login=this.findViewById(R.id.install_login);
        aSwitch=(Switch)this.findViewById(R.id.install_switch);
        if(noLogin==1){
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }
    }
    private void initEvent(){
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putInt("noLogin",1);
                    editor.commit();
                }else {
                    SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putInt("noLogin",0);
                    editor.commit();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(installActivity.this, userActivity.class);
                startActivity(i);
                finish();
            }
        });
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(installActivity.this,payMoneyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putInt("noLogin",0);
                editor.commit();
                Toast.makeText(installActivity.this,"注销成功",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(installActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
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
