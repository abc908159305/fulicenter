package cn.ucai.fulicenter.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;

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
    User user;
    OnSetAvatarListener mOnSetAvatarListener;
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

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
            return;
        }
        showInfo();
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.ivBack, R.id.btnCancel,R.id.Avatar_Layout,R.id.UserName_Layout,R.id.UserNick_Layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.Avatar_Layout:
                //上传头像
                mOnSetAvatarListener = new OnSetAvatarListener(mContext, R.id.layout_upload_avatar,
                        user.getMuserName(), I.AVATAR_TYPE_USER_PATH);

                break;
            case R.id.UserName_Layout:
                CommonUtils.showShortToast("用户名不能修改哟");
                break;
            case R.id.UserNick_Layout:
                MFGT.startActivityForResult(mContext,new Intent(mContext,UpDateNickActivity.class), I.REQUEST_CODE_UPDATE_NICK);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInfo();
    }
    private void showInfo() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivUserAvatar);//设置头像
            mivUserNick.setText(user.getMuserNick());//昵称
            mivUserName.setText(user.getMuserName());//用户名
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_UPDATE_NICK) {
            CommonUtils.showLongToast("修改成功哦");
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode,data,mivUserAvatar);
        if (requestCode == I.REQUEST_CODE_UPDATE_NICK) {
            CommonUtils.showLongToast("更改头像成功哟");
        }
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = OnSetAvatarListener.getAvatarFile(mContext, user.getMuserName());
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                ResultUtils.getResultFromJson(s, User.class);
            }

            @Override
            public void onError(String error) {

            }
        });
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
                if (user != null) {
                    SharePrefrenceUtils.getInstence(mContext).removeUser();
                    FuLiCenterApplication.setUser(null);
                    MFGT.gotoLogin(mContext);
                }
                finish();
            }
        });
        builder.show();
    }
}
