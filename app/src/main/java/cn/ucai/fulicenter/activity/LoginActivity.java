package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 1001);
                break;
        }
    }
}
