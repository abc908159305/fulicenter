package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.layout_new_good)
    RadioButton layoutNewGood;
    @Bind(R.id.layout_boutique)
    RadioButton layoutBoutique;
    @Bind(R.id.layout_category)
    RadioButton layoutCategory;
    @Bind(R.id.layout_cart)
    RadioButton layoutCart;
    @Bind(R.id.layout_personal_center)
    RadioButton layoutPersonalCenter;


    int index;
    int currentIndex;
    RadioButton rbs[];
    Fragment[] mFragment;
    NewGoodsFragment mNewGoodsFragment;
    Fragment mBoutiqueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initrbs();
        initFragment();
    }

    private void initFragment() {
        mFragment = new Fragment[5];
        mNewGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mFragment[0] = mNewGoodsFragment;
        mFragment[1] = mBoutiqueFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,mBoutiqueFragment)
                .add(R.id.fragment_container,mNewGoodsFragment)
                .hide(mBoutiqueFragment)
                .show(mNewGoodsFragment)
                .commit();
    }

    private void initrbs() {
        rbs = new RadioButton[5];
        rbs[0] = layoutNewGood;
        rbs[1] = layoutBoutique;
        rbs[2] = layoutCategory;
        rbs[3] = layoutCart;
        rbs[4] = layoutPersonalCenter;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                index = 3;
                break;
            case R.id.layout_personal_center:
                index = 4;
                break;
        }
        setFragment();

    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragment[currentIndex]);
            if (!mFragment[index].isAdded()) {
                ft.add(R.id.fragment_container, mFragment[index]);
            }
            ft.show(mFragment[index]).commit();
        }
        setRadioButton();
        currentIndex = index;
    }

    private void setRadioButton() {
        for (int i = 0; i < rbs.length; i++) {
            if (i == index) {
                rbs[i].setChecked(true);
            } else {
                rbs[i].setChecked(false);
            }
        }
    }
}
