package json.chao.com.wanandroid.ui.main.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import json.chao.com.wanandroid.app.Constants;
import json.chao.com.wanandroid.base.activity.BaseActivity;
import json.chao.com.wanandroid.R;
import json.chao.com.wanandroid.contract.main.LoginContract;
import json.chao.com.wanandroid.presenter.main.LoginPresenter;
import json.chao.com.wanandroid.utils.CommonUtils;
import json.chao.com.wanandroid.utils.StatusBarUtil;

/**
 * @author quchao
 * @date 2018/2/26
 */

//loginactivity继承baseactivity,实现了loginview的接口
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.login_group)
    RelativeLayout mLoginGroup;
    @BindView(R.id.login_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.login_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.login_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_register_btn)
    Button mRegisterBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
    }

    @Override
    protected void initEventAndData() {
        subscribeLoginClickEvent();
    }

    @Override
    public void showLoginSuccess() {
        CommonUtils.showMessage(this, getString(R.string.login_success));
        onBackPressedSupport();
    }

    @OnClick({R.id.login_register_btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register_btn:
                startRegisterPager();
                break;
            default:
                break;
        }
    }

    private void startRegisterPager() {
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(mRegisterBtn,
                mRegisterBtn.getWidth() / 2, mRegisterBtn.getHeight() / 2, 0, 0);
        startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
    }

    private void subscribeLoginClickEvent() {
//        mpreserter 是一个T类型，是父类baseactivity的一个属性,把后面一大段参数传到basepresenter的addrxbindingsubscribe()中，
//        这里我们需要mpresenter,先跳转过去，
        mPresenter.addRxBindingSubscribe(RxView.clicks(mLoginBtn).throttleFirst(Constants.CLICK_TIME_AREA, TimeUnit.MILLISECONDS).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object o) throws Exception {
                return mPresenter != null;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
//        观察者观察被观察者的点击动作，消费事件时，拿到控件上的用户名密码传参数过去执行登录逻辑
//        mpreserter 是一个T类型，是父类baseactivity的一个属性,
                mPresenter.getLoginData(mAccountEdit.getText().toString().trim(),
                        mPasswordEdit.getText().toString().trim());
            }
        }));
    }

}
