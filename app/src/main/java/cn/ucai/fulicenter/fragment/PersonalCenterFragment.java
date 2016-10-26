package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.SettingActivity;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;


public class PersonalCenterFragment extends BaseFragment {
    private static final String TAG = "PersonalCenterFragment.class.getSimpleName()";
    MainActivity mContext;
    @Bind(R.id.ivPicture)
    ImageView mivPicture;
    @Bind(R.id.tvUserName)
    TextView mtvUserName;
    @Bind(R.id.tvCollectGoodsHint)
    TextView mtvCollectGoodsHint;

    User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        mContext = (MainActivity) getActivity();
        ButterKnife.bind(this, layout);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        L.e(TAG,"user = "+user);
        if (user == null) {
            MFGT.gotoLogin(mContext);
        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,mivPicture);
            mtvUserName.setText(user.getMuserNick());
        }
    }

    /**
     * 实时更新头像的方法
     */
    private void syncUserInfo() {
        NetDao.syncUserInfo(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result != null) {
                    User u = (User) result.getRetData();
                    if (!user.equals(u)) {
                        UserDao dao = new UserDao(mContext);
                        boolean b = dao.saveUser(u);
                        if (b) {
                            FuLiCenterApplication.setUser(u);
                            user = u;
                            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,mivPicture);
                            mtvUserName.setText(user.getMuserNick());
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     * 获取用户收藏数量的方法
     */
    private void syncCollectsCount() {
        NetDao.getCollectsCount(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {

            @Override
            public void onSuccess(MessageBean result) {
                L.e("result==="+result);
                if (result!=null & result.isSuccess()) {
                    mtvCollectGoodsHint.setText(result.getMsg());
                } else {
                    mtvCollectGoodsHint.setText("0");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,mivPicture);
            mtvUserName.setText(user.getMuserNick());
            syncUserInfo();
            syncCollectsCount();
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tvSetting)
    public void onClick() {
        MFGT.startActivity(mContext, SettingActivity.class);
    }
}
