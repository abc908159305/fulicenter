package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
    ArrayList<NewGoodsBean> mlist;
    Context context;

    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public GoodsAdapter(Context context, ArrayList<NewGoodsBean> list) {
        this.context = context;
        mlist = new ArrayList<>();
        mlist.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(context,R.layout.item_footer,null));
        }
        if (viewType == I.TYPE_ITEM) {
            holder = new GoodsViewHolder(View.inflate(context, R.layout.item_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position)==I.TYPE_FOOTER) {
            FooterViewHolder fv = (FooterViewHolder) holder;
            fv.tvFooter.setText(getFootString());
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
        }
        return I.TYPE_ITEM;

    }

    public void initData(ArrayList<NewGoodsBean> list) {
        if (mlist != null) {
            mlist.clear();
        }
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public int getFootString() {
        return isMore?R.string.load_more:R.string.no_more;
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
