package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class OkBuyActivity extends BaseActivity implements PaymentHandler{

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
    private static String URL = "http://218.244.151.190/demo/charge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ok_buy);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});
        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
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
        if (!number.matches("[\\d]{11}")) {
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
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", rankPrice*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
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

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果

            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    PingppLog.d("result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            int resultCode = data.getExtras().getInt("code");
            switch (resultCode) {
                case 1:
                    paySuccess();
                    CommonUtils.showLongToast("付款成功");
                    break;
                case -1:
                    CommonUtils.showLongToast("付款失败");
                    finish();
                    break;
            }
        }
    }

    private void paySuccess() {
        for (String id : ids) {
            NetDao.deleteCart(mContext, Integer.valueOf(id), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {

                }

                @Override
                public void onError(String error) {

                }
            });
        }
        finish();
    }
}
