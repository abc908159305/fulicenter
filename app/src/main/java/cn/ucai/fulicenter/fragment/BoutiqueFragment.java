package cn.ucai.fulicenter.fragment;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiqueFragment extends BaseFragment{

    @Bind(R.id.refresh)
    TextView mrefresh;
    @Bind(R.id.rv)
    RecyclerView mrv;
    @Bind(R.id.srl)
    SwipeRefreshLayout msrl;

    LinearLayoutManager llm;
    MainActivity mContext;
    BoutiqueAdapter mAdapter;
    ArrayList<BoutiqueBean> mList;

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, inflate);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(mContext, mList);
        super.onCreateView(inflater,container,savedInstanceState);
/*        initView();
        initData();
        Listener();*/
        return inflate;
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
                downloadBoutique();
            }
        });
    }
    @Override
    protected void initData() {
        downloadBoutique();
    }

    private void downloadBoutique() {
        NetDao.downloadBoutique(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                    msrl.setRefreshing(false);
                    mrefresh.setVisibility(View.GONE);
                    L.e("result="+result);
                    if (result != null && result.length > 0) {
                        ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                        mAdapter.initData(list);
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
    @Override
    protected void initView() {
        msrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green)
        );
        llm = new LinearLayoutManager(mContext);
        mrv.setLayoutManager(llm);
        mrv.setHasFixedSize(true);
        mrv.setAdapter(mAdapter);
        mrv.addItemDecoration(new SpaceItemDecoration(12));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
