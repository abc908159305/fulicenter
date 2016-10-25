package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.ivBack)
    ImageView mivBack;
    @Bind(R.id.tvSystem_title)
    TextView mtvSystemTitle;
    @Bind(R.id.Login_edName)
    EditText mLoginEdName;
    @Bind(R.id.Login_edPwd)
    EditText mLoginEdPwd;
    @Bind(R.id.btnLogin)
    Button mbtnLogin;
    @Bind(R.id.btnRegister)
    Button mbtnRegister;

    LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.ivBack, R.id.btnLogin, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btnLogin:
                String userName = mLoginEdName.getText().toString().trim();
                String userPwd = mLoginEdPwd.getText().toString().trim();
                final ProgressDialog pd = new ProgressDialog(mContext);
                pd.setMessage("登陆中");
                pd.show();
                NetDao.login(mContext, userName, userPwd, new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        pd.dismiss();
                        Result result = ResultUtils.getResultFromJson(s, User.class);
                        if (result == null) {

                        }else {
                            if (result.isRetMsg()) {
                                User user = (User) result.getRetData();
                                CommonUtils.showShortToast(R.string.login_success);
                                UserDao dao = new UserDao(mContext);
                                boolean isSuccess = dao.saveUser(user);
                                if (isSuccess) {
                                    SharePrefrenceUtils.getInstence(mContext).saveUser(user.getMuserName());
                                    FuLiCenterApplication.setUser(user);
                                    MFGT.finish(mContext);
                                } else {
                                    CommonUtils.showLongToast(R.string.user_datebase_error);
                                }
                            } else {
                                if (result.getRetCode()==401) {//账户不存在
                                    CommonUtils.showLongToast("账户不存在");
                                } else if (result.getRetCode()==402) {//账户密码错误
                                    CommonUtils.showLongToast("账户密码错误");
                                } else if (result.getRetCode()==403) {//登陆成功
                                    CommonUtils.showLongToast("登陆成功");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        L.e("登陆="+error);
                        CommonUtils.showLongToast("登陆失败");
                    }
                });
                break;
            case R.id.btnRegister:
                MFGT.gotoRegister(mContext);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER) {
            String name = data.getStringExtra(I.User.USER_NAME);
            mLoginEdName.setText(name);
        }
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }
}
