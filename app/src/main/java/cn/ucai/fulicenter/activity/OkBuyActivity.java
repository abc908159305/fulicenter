package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;

public class OkBuyActivity extends BaseActivity {

    @Bind(R.id.et_Name)
    EditText metName;
    @Bind(R.id.et_Number)
    EditText metNumber;
    @Bind(R.id.sp_City)
    Spinner mspCity;
    @Bind(R.id.et_Address)
    EditText metAddress;
    @Bind(R.id.tv_Total)
    TextView mtvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ok_buy);
        ButterKnife.bind(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String cartIds = getIntent().getStringExtra(I.Cart.ID);
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_OkBuy)
    public void onClick() {
    }
}
