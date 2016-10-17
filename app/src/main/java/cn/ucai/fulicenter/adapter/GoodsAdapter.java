package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends Adapter<ViewHolder> {
    List<NewGoodsBean> mlist;
    Context context;

    String FooterText;

    public GoodsAdapter(List<NewGoodsBean> list, Context context) {
        this.mlist = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            View inflate = View.inflate(context, R.layout.item_footer, null);
            holder = new FooterViewHolder(inflate);
        } else {
            View inflate = View.inflate(context, R.layout.item_goods, null);
            holder = new GoodsViewHolder(inflate);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder fv = (FooterViewHolder) holder;
            fv.tvFooter.setText(FooterText);
        } else {
            GoodsViewHolder gv = (GoodsViewHolder) holder;
            NewGoodsBean newGoodsBean = mlist.get(position);
            //图片
            ImageLoader.downloadImg(context,gv.ivPicture, newGoodsBean.getGoodsThumb());
            gv.tvgoodsName.setText(newGoodsBean.getGoodsName());
            gv.tvCost.setText(newGoodsBean.getCurrencyPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mlist != null ? mlist.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    static class FooterViewHolder extends ViewHolder {
        @Bind(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class GoodsViewHolder extends ViewHolder{
        @Bind(R.id.ivPicture)
        ImageView ivPicture;
        @Bind(R.id.tvgoodsName)
        TextView tvgoodsName;
        @Bind(R.id.tvCost)
        TextView tvCost;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
