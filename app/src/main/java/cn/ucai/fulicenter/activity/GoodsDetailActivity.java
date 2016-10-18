package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailActivity extends AppCompatActivity {

    @Bind(R.id.ivBack)
    ImageView mivBack;
    @Bind(R.id.tvEnName)
    TextView mtvEnName;
    @Bind(R.id.tvNowPrice)
    TextView mtvNowPrice;
    @Bind(R.id.tvChName)
    TextView mtvChName;
    @Bind(R.id.tvPrice)
    TextView mtvPrice;
    @Bind(R.id.salv)
    SlideAutoLoopView msalv;
    @Bind(R.id.indicator)
    FlowIndicator mindicator;
    @Bind(R.id.wv_good_brief)
    WebView mwvGoodBrief;
    int goodsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            finish();
        }
        initView();
        initData();
        setListener();
        L.e("details", "goodsid=" + goodsId);
    }

    private void setListener() {

    }

    private void initData() {
        NetDao.downloadGoodsDetail(this, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details="+result);
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details,error="+error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        mtvChName.setText(details.getGoodsName());
        mtvEnName.setText(details.getGoodsEnglishName());
        mtvPrice.setText(details.getShopPrice());
        mtvNowPrice.setText(details.getCurrencyPrice());
        msalv.startPlayLoop(mindicator,getAlbumImgUrl(details),getAlbumImgCount(details));
        mwvGoodBrief.loadDataWithBaseURL(null,details.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return  0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i = 0;i < albums.length;i++) {
                urls[i] = albums[i].getImgUrl();
            }
        }
        return urls;
    }

    private void initView() {
        
    }
    @OnClick(R.id.ivBack)
    public void onBackOnClick() {
        MFGT.finish(this);
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }
}
