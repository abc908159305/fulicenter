package cn.ucai.fulicenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.utils.MFGT;

public class SplashActivity extends AppCompatActivity {
    static long sleepTime = 2000;
    SplashActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserDao dao = new UserDao(mContext);
                dao.getUser("abc908159305");
                MFGT.gotoMainActivity(SplashActivity.this);
                finish();
            }
        },sleepTime);
    }
}
