package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.view.FooterViewHolder;

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;
    boolean isMore;

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> list) {
        this.mContext = mContext;
        mList = new ArrayList<>();
        mList.addAll(list);
    }
    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new BoutiqueViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_boutique, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).tvFooter.setText(getFooterString());
        }
        if (holder instanceof BoutiqueViewHolder) {
            BoutiqueBean boutiqueBean = mList.get(position);
            ((BoutiqueViewHolder) holder).mtvBoutiqueTtile.setText(boutiqueBean.getTitle());
            ((BoutiqueViewHolder) holder).mtvBoutiqueName.setText(boutiqueBean.getName());
            ((BoutiqueViewHolder) holder).mtvBoutiqueDescription.setText(boutiqueBean.getDescription());
            ImageLoader.downloadImg(mContext,((BoutiqueViewHolder) holder).mivBoutiqueImg,boutiqueBean.getImageurl());
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<BoutiqueBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    static class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivBoutiqueImg)
        ImageView mivBoutiqueImg;
        @Bind(R.id.tvBoutiqueTtile)
        TextView mtvBoutiqueTtile;
        @Bind(R.id.tvBoutiqueName)
        TextView mtvBoutiqueName;
        @Bind(R.id.tvBoutiqueDescription)
        TextView mtvBoutiqueDescription;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
