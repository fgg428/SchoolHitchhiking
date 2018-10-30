package work.wang.schoolhitchhiking.userInfor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import work.wang.schoolhitchhiking.R;
import work.wang.schoolhitchhiking.show.userActivity;

public class selectHistoryOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private Button order1,order2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_history_order);
        init();
        initEvent();
    }
    private void init(){
        order1=(Button)this.findViewById(R.id.select_order1);
        order2=(Button)this.findViewById(R.id.select_order2);
    }
    private void initEvent(){
        order1.setOnClickListener(this);
        order2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_order1:
                Intent intent=new Intent(this,userHistoryOrderctivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.select_order2:
                Intent i=new Intent(this,userHistoryAcceptOrderActivity.class);
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
