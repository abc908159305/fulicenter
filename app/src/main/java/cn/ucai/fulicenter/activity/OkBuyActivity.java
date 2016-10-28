package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

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

    String cartIds = "";
    User user = null;
    OkBuyActivity mContext;
    ArrayList<CartBean> mList = null;
    String[] ids = new String[]{};
    int rankPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ok_buy);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,"确认购买");
    }

    @Override
    protected void initData() {
        cartIds = getIntent().getStringExtra(I.Cart.ID);
        user = FuLiCenterApplication.getUser();
        if (cartIds == null || cartIds.equals("") || user == null) {
            finish();
        }
        ids = cartIds.split(",");
        getOrderList();
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_OkBuy)
    public void checkOrder() {
        String name = metName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            metName.setError("收货人姓名不能为空");
            metName.requestFocus();
            return;
        }
        String number = metNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            metNumber.setError("手机号码不能为空");
            metNumber.requestFocus();
            return;
        }
        if (!number.matches("[\\d]{11}]")) {
            metNumber.setError("手机号码格式错误");
            metNumber.requestFocus();
            return;
        }
        String city = mspCity.getSelectedItem().toString();
        if (TextUtils.isEmpty(city)) {
            CommonUtils.showLongToast("收货人地区不能为空哦");
            return;
        }
        String address = metAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            metAddress.setError("街道地址不能为空");
            metAddress.requestFocus();
            return;
        }
        gotoStatements();
    }
    private void gotoStatements() {

    }


    public void getOrderList() {
        NetDao.downloadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    mList.addAll(list);
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void sumPrice() {
        rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                for (String id : ids) {
                    if (id.equals(String.valueOf(c.getId()))) {
                        cartIds += c.getId() + ",";
                        rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    }
                }
            }
        }
        mtvTotal.setText("合计：￥"+Double.valueOf(rankPrice));
    }
    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }
}
