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
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectsAdapter;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CollectsActivity extends BaseActivity {

    @Bind(R.id.refresh)
    TextView mrefresh;
    @Bind(R.id.rv)
    RecyclerView mrv;
    @Bind(R.id.srl)
    SwipeRefreshLayout msrl;

    CollectsAdapter mAdapter;
    ArrayList<CollectBean> mList;
    GridLayoutManager glm;
    int pageId = 1;
    CollectsActivity mContext;
    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new CollectsAdapter(mContext, mList);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setRefreshing(true);
                mrefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadCollects(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downloadCollects(final int action) {

        NetDao.downloadCollects(mContext,user.getMuserName(), pageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                    msrl.setRefreshing(false);
                    mAdapter.setMore(true);
                    mrefresh.setVisibility(View.GONE);
                    if (result != null && result.length > 0) {
                        ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                        if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                            mAdapter.initData(list);
                        } else {
                            mAdapter.addData(list);
                        }
                        if (list.size() < I.PAGE_SIZE_DEFAULT) {
                            mAdapter.setMore(false);
                        }
                    } else {
                        mAdapter.setMore(false);
                    }
                }

            @Override
            public void onError(String error) {
                msrl.setRefreshing(false);
                mrefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
                L.e("error" + error);
            }
        });
    }

    private void setPullUpListener() {
        mrv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastposition = glm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastposition == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageId++;
                    downloadCollects(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        }
        downloadCollects(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,getResources().getString(R.string.collect_title));
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
}
