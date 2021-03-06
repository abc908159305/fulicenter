package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailActivity extends BaseActivity {

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
    boolean isCollect = false;//是否收藏

    int goodsId;
    Context mContext;
    @Bind(R.id.iv_collect_in)
    ImageView mivCollectIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        super.onCreate(savedInstanceState);
/*        initView();*/
        initData();
        setListener();
        L.e("details", "goodsid=" + goodsId);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        NetDao.downloadGoodsDetail(this, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details=" + result);
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details,error=" + error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        mtvChName.setText(details.getGoodsName());
        mtvEnName.setText(details.getGoodsEnglishName());
        mtvPrice.setText(details.getShopPrice());
        mtvNowPrice.setText(details.getCurrencyPrice());
        msalv.startPlayLoop(mindicator, getAlbumImgUrl(details), getAlbumImgCount(details));
        mwvGoodBrief.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                urls[i] = albums[i].getImgUrl();
            }
        }
        return urls;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClollect();
    }

    /**
     * 是否收藏的方法
     */
    private void isClollect() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isColected(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess() == true) {
                        isCollect = true;
                        updateGoodsCollectStatus();
                    } else {
                        isCollect = false;
                        updateGoodsCollectStatus();
                    }
                }

                @Override
                public void onError(String error) {
                    isCollect = false;
                    updateGoodsCollectStatus();
                }
            });
        }
        updateGoodsCollectStatus();
    }

    private void updateGoodsCollectStatus() {
        if (isCollect) {
            mivCollectIn.setImageResource(R.mipmap.bg_collect_out);
        } else {
            mivCollectIn.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.ivBack)
    public void onBackOnClick() {
        MFGT.finish(this);
    }
    @OnClick(R.id.iv_collect_in)
    public void onCollectClick() {
        User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLogin((Activity) mContext);
        } else {
            if (isCollect) {
                NetDao.deleteCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = !isCollect;
                            updateGoodsCollectStatus();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                NetDao.addCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = !isCollect;
                            updateGoodsCollectStatus();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }
    @OnClick(R.id.iv_share)
    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    @OnClick(R.id.iv_collect)
    public void addCart() {
        User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLogin((Activity) mContext);
        } else {
            NetDao.addCart(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        CommonUtils.showShortToast("添加购物车成功");
                    } else {
                        CommonUtils.showShortToast("添加购物车失败");
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast("添加购物车失败");
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

}
