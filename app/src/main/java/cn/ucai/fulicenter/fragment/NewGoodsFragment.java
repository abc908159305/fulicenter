package cn.ucai.fulicenter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;


public class NewGoodsFragment extends Fragment {

    @Bind(R.id.srl)
    SwipeRefreshLayout msrl;
    @Bind(R.id.refresh)
    TextView mrefresh;
    @Bind(R.id.rv)
    RecyclerView mrv;
    MainActivity mContext;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    int pageId = 1;
    public NewGoodsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        mContext = (MainActivity) getContext();
        mAdapter = new GoodsAdapter(mContext, mList);
        ButterKnife.bind(this, layout);
        initView();
        initData();
        return layout;
    }

    private void initData() {
        NetDao.downloadNewGoods(mContext, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
        msrl.setColorSchemeColors(
                    getResources().getColor(R.color.google_blue),
                    getResources().getColor(R.color.google_green),
                    getResources().getColor(R.color.google_red),
                    getResources().getColor(R.color.google_green)
                );
        GridLayoutManager glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        mrv.setLayoutManager(glm);
        mrv.setHasFixedSize(true);
        mrv.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
