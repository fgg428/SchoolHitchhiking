package work.wang.schoolhitchhiking.userInfor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.exchange.exchangeActivity;
import work.wang.schoolhitchhiking.exchange.gameActivity;
import work.wang.schoolhitchhiking.show.userActivity;

public class userInvestExperienceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button experience_exchange,experience_playGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_invest_experience);
        init();
        initEvent();
    }
    private void init(){
        experience_exchange=(Button)this.findViewById(R.id.experience_exchange);
        experience_playGame=(Button)this.findViewById(R.id.experience_playGame);
    }
    private void initEvent(){
        experience_exchange.setOnClickListener(this);
        experience_playGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.experience_exchange:
                Intent intent=new Intent(this,exchangeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.experience_playGame:
                Intent i=new Intent(this,gameActivity.class);
                startActivity(i);
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
