package work.wang.schoolhitchhiking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import work.wang.schoolhitchhiking.show.userActivity;

public class registerActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText telNum,validateNum;
    private Button validateNum_btn,landing_btn;
    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器
    private Handler mHandler;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent=getIntent();
        status=intent.getStringExtra("status");
        init();
        init1();
    }
    private void init(){
        telNum=(EditText)this.findViewById(R.id.telNum);
        validateNum=(EditText)this.findViewById(R.id.validateNum);
        validateNum_btn=(Button)this.findViewById(R.id.validateNum_btn);
        landing_btn=(Button)this.findViewById(R.id.landing_btn);
        validateNum_btn.setOnClickListener(this);
        landing_btn.setOnClickListener(this);
        mTimeCount = new TimeCount(60000, 1000);
    }
    private void init1(){
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                        if (status.equals("register")){
                            Intent intent=new Intent(registerActivity.this,registerAndLoginActivity.class);
                            intent.putExtra("telNum",telNum.getText().toString());
                            startActivity(intent); //页面跳转
                            finish();
                        }else {
                            Intent intent=new Intent(registerActivity.this,forgetActivity.class);
                            //intent.putExtra("telNum",telNum.getText().toString());
                            startActivity(intent); //页面跳转
                            finish();
                        }
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){ //返回支持发送验证码的国家列表

                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    private final OkHttpClient client=new OkHttpClient();
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.validateNum_btn:
                attainValidateNum();
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case 1:
                                if (status.equals("register")){
                                    SMSSDK.getVerificationCode("+86",telNum.getText().toString());//获取验证码
                                    mTimeCount.start();
                                }else {
                                    Toast.makeText(registerActivity.this,"该手机号码没有被注册",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                if (status.equals("register")){
                                    Toast.makeText(registerActivity.this,"该手机号码已经注册",Toast.LENGTH_SHORT).show();
                                }else {
                                    SMSSDK.getVerificationCode("+86",telNum.getText().toString());//获取验证码
                                    mTimeCount.start();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                };
                break;
            case R.id.landing_btn:
                attainLanding();
                break;
        }
    }
    public void checkTelNumber(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody=new FormBody.Builder()
                        .add("telNum",telNum.getText().toString().trim())
                        .build();
                Request request=new Request.Builder()
                        .url("http://172.17.149.206:8080/hitchhiking/checkTelNum")
                        .post(formBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()){
                            return;
                        }
                        byte[] data=null;
                        try {
                            data=response.body().bytes();
                            String mg=new String(data,"UTF-8");//byte[] 转 string
                            if(mg.equals("success")){
                                Message message = mHandler.obtainMessage();
                                message.what = 1;
                                mHandler.sendMessage(message);
                            }else{
                                SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sp.edit();
                                editor.putString("userName",mg);
                                editor.commit();
                                Message message = mHandler.obtainMessage();
                                message.what = 2;
                                mHandler.sendMessage(message);
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
    private void attainValidateNum(){
        if(!telNum.getText().toString().trim().equals("")){
            if (checkTel(telNum.getText().toString().trim())) {
                checkTelNumber();
//                SMSSDK.getVerificationCode("+86",telNum.getText().toString());//获取验证码
//                mTimeCount.start();
            }else{
                Toast.makeText(registerActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(registerActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }
    private void attainLanding(){
        if (!telNum.getText().toString().trim().equals("")) {
            if (checkTel(telNum.getText().toString().trim())) {
                if (!validateNum.getText().toString().trim().equals("")) {
                    SMSSDK.submitVerificationCode("+86",telNum.getText().toString().trim(),validateNum.getText().toString().trim());//提交验证
                }else{
                    Toast.makeText(registerActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(registerActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(registerActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkTel(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);//回调完成后，销毁，防止内存泄漏
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            validateNum_btn.setClickable(false);
            validateNum_btn.setText(l/1000 + "秒后重载");
        }

        @Override
        public void onFinish() {
            validateNum_btn.setClickable(true);
            validateNum_btn.setText("获取验证码");
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
