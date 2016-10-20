package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CategoryActivity extends BaseActivity {

    @Bind(R.id.tvTitle)
    TextView mtvTitle;
    @Bind(R.id.refresh)
    TextView mrefresh;
    @Bind(R.id.rv)
    RecyclerView mrv;
    @Bind(R.id.srl)
    SwipeRefreshLayout msrl;

    CategoryActivity mContext;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager glm;
    int pageId = 1;
    int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        if (catId == 0) {
            finish();
        }
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext, mList);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        msrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green)
        );
        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        mrv.setLayoutManager(glm);
        mrv.setHasFixedSize(true);
        mrv.setAdapter(mAdapter);
        mrv.addItemDecoration(new SpaceItemDecoration(12));
    }

    @Override
    protected void initData() {
        downloadCategoryGoods(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void setListener() {
        setPullDownListener();
    }
    private void setPullDownListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setRefreshing(true);
                mrefresh.setVisibility(View.VISIBLE);
                downloadCategoryGoods(I.ACTION_DOWNLOAD);
            }
        });
    }
    private void downloadCategoryGoods(final int action) {
        NetDao.downloadCategoryGoods(mContext,catId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                if (result != null && result.length > 0) {
                    msrl.setRefreshing(false);
                    mrefresh.setVisibility(View.GONE);
                    mAdapter.setMore(true);
                    L.e("result="+result);
                    if (result != null && result.length > 0) {
                        ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                        if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                            mAdapter.initData(list);
                        } else {
                            mAdapter.addData(list);
                        }
                        if (list.size() < I.PAGE_SIZE_DEFAULT) {
                            mAdapter.setMore(false);
                        }
                    }else {
                        mAdapter.setMore(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                msrl.setRefreshing(false);
                mrefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
                L.e("error"+error);
            }
        });
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(this);
    }
}
