package cn.ucai.fulicenter.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.tvSystem_title)
    TextView mtvSystemTitle;
    @Bind(R.id.ivUserAvatar)
    ImageView mivUserAvatar;
    @Bind(R.id.ivUserName)
    TextView mivUserName;
    @Bind(R.id.ivUserNick)
    TextView mivUserNick;


    SettingActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mtvSystemTitle.setText("设置");
        User user = FuLiCenterApplication.getUser();
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,mivUserAvatar);//设置头像
        mivUserNick.setText(user.getMuserNick());//昵称
        mivUserName.setText(user.getMuserName());//用户名
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.ivBack, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btnCancel:
                cancel();
                break;
        }
    }

    private void cancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("TIP");
        builder.setMessage("确认注销吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePrefrenceUtils.getInstence(mContext).removeUser();
                FuLiCenterApplication.setUser(null);
                MFGT.gotoLogin(mContext);
            }
        });
        builder.show();
    }
}
