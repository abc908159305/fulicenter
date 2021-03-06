package cn.ucai.fulicenter.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

public class BoutiqueAdapter extends RecyclerView.Adapter<BoutiqueAdapter.BoutiqueViewHolder> {
    Context mContext;
    ArrayList<BoutiqueBean> mList;

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> list) {
        this.mContext = mContext;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    @Override
    public BoutiqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        BoutiqueViewHolder holder = new BoutiqueViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_boutique, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(BoutiqueViewHolder holder, int position) {
        BoutiqueBean boutiqueBean = mList.get(position);
        holder.mtvBoutiqueTtile.setText(boutiqueBean.getTitle());
        holder.mtvBoutiqueName.setText(boutiqueBean.getName());
        holder.mtvBoutiqueDescription.setText(boutiqueBean.getDescription());
        ImageLoader.downloadImg(mContext, holder.mivBoutiqueImg, boutiqueBean.getImageurl());
        holder.mitem_Bourique_Layout.setTag(boutiqueBean);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivBoutiqueImg)
        ImageView mivBoutiqueImg;
        @Bind(R.id.tvBoutiqueTtile)
        TextView mtvBoutiqueTtile;
        @Bind(R.id.tvBoutiqueName)
        TextView mtvBoutiqueName;
        @Bind(R.id.tvBoutiqueDescription)
        TextView mtvBoutiqueDescription;
        @Bind(R.id.item_boutique_layout)
        RelativeLayout mitem_Bourique_Layout;
        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.item_boutique_layout)
        public void onBoutiqueClick(){
            BoutiqueBean bean = (BoutiqueBean) mitem_Bourique_Layout.getTag();
            MFGT.gotoBoutiqueChildActivity((Activity) mContext,bean);
        }
    }
}
