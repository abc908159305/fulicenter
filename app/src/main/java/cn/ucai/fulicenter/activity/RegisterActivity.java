package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.DisplayUtils;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.ivBack)
    ImageView mivBack;
    @Bind(R.id.tvSystem_title)
    TextView mtvSystemTitle;
    @Bind(R.id.Login_edName)
    EditText mLoginEdName;
    @Bind(R.id.Login_edNick)
    EditText mLoginEdNick;
    @Bind(R.id.Login_edPwd)
    EditText mLoginEdPwd;
    @Bind(R.id.Login_edPwd2)
    EditText mLoginEdPwd2;
    @Bind(R.id.btnRegister)
    Button mbtnRegister;

    String muserName;
    String muserNick;
    String muserPwd;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "账户注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btnRegister)
    public void onClick() {
        muserName = mLoginEdName.getText().toString().trim();
        muserNick = mLoginEdNick.getText().toString().trim();
        muserPwd = mLoginEdPwd.getText().toString().trim();
        String userPwd2 = mLoginEdPwd2.getText().toString().trim();
        L.e("账号："+muserName+",昵称："+muserNick+",密码："+muserPwd+",确认密码："+userPwd2);
        if (TextUtils.isEmpty(muserName)) {
            CommonUtils.showShortToast("账号不能为空");
            mLoginEdName.requestFocus();
            return;
        } else if (!muserName.matches("[A-Za-z]\\w{6,16}")) {
            CommonUtils.showShortToast("账号不合法");
            mLoginEdName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(muserNick)) {
            CommonUtils.showShortToast("昵称不能为空");
            mLoginEdNick.requestFocus();
            return;
        } else if (TextUtils.isEmpty(muserPwd)) {
            CommonUtils.showShortToast("密码不能为空");
            mLoginEdPwd.requestFocus();
            return;
        } else if (TextUtils.isEmpty(userPwd2)) {
            CommonUtils.showShortToast("确认密码不能为空");
            mLoginEdPwd2.requestFocus();
            return;
        } else if (!muserPwd.equals(userPwd2)) {
            CommonUtils.showShortToast("两次密码不一致");
            mLoginEdPwd.requestFocus();
            return;
        }
        register();
    }

    private void register() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("注册中");
        pd.show();
        NetDao.register(mContext, muserName, muserNick, muserPwd, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if (result == null) {
                    CommonUtils.showShortToast("注册失败");
                } else {
                    if (result.isRetMsg()) {
                        CommonUtils.showLongToast("注册成功");
                        //返回给上一个页面
                        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,muserName));
                        MFGT.finish((Activity) mContext);
                    } else {
                        CommonUtils.showLongToast("用户名已存在");
                        mLoginEdName.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(error);
                L.e("注册"+error);
            }
        });
    }
}
