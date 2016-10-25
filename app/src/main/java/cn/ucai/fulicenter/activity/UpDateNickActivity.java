package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

public class UpDateNickActivity extends BaseActivity {

    @Bind(R.id.tvSystem_title)
    TextView mtvSystemTitle;
    @Bind(R.id.etUserNick)
    EditText metUserNick;

    User user = null;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_up_date_nick);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mtvSystemTitle.setText("更新昵称");

    }

    @Override
    protected void initData() {

        user = FuLiCenterApplication.getUser();
        if (user != null) {
            metUserNick.setText(user.getMuserNick());
            metUserNick.setSelectAllOnFocus(true);
        } else {
            finish();
        }

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btnSave)
    public void onClickSave() {
        if (user != null) {
            String nick = metUserNick.getText().toString().trim();
            if (nick.equals(user.getMuserNick())) {
                CommonUtils.showLongToast("昵称没有改变哟");
            } else {
                updateNick(nick);
            }

        }
    }

    private void updateNick(String nick) {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("修改中");
        pd.show();
        NetDao.updatenick(mContext, user.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                //setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,muserName));
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result == null) {
                    CommonUtils.showLongToast("更新失败");
                } else {
                    if (result.isRetMsg()) {
                        User u = (User) result.getRetData();
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.saveUser(u);
                        if (isSuccess) {
                            FuLiCenterApplication.setUser(u);
                            setResult(RESULT_OK);
                            MFGT.finish((Activity) mContext);
                        } else {
                            CommonUtils.showLongToast("数据库异常");
                        }
                    } else {
                        if (result.getRetCode() == I.MSG_USER_SAME_NICK) {
                            CommonUtils.showLongToast("昵称未修改");
                        } else if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                            CommonUtils.showLongToast("昵称修改失败");
                        } else {
                            CommonUtils.showLongToast("修改失败,未知错误");
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        MFGT.finish(this);
    }
}
