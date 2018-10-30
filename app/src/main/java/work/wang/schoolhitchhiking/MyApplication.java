package work.wang.schoolhitchhiking;
import android.app.Application;
import cn.smssdk.SMSSDK;
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "26360eb7bf028", "2fb44aa73275891db5faa8ee8af1dbee");
    }
}
