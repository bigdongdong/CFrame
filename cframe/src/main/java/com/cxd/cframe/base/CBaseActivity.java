package com.cxd.cframe.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxd.cframe.R;
import com.cxd.cframe.utils.StatusBarUtil;
import com.cxd.cframe.views.dialog.LoadingDialog;
import com.cxd.eventbox.EventBox;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author CXD
 * @date :2019/5/20 9:52
 * TAG : xxxxActivityTAG
 * 内置loading pop弹窗
 */
@SuppressLint("CheckResult")
public abstract class CBaseActivity extends AppCompatActivity {

    protected Activity context ;
    protected String TAG ;

    private Unbinder unbinder;
    private Config config ;
    private TextView titleTV ;
    private ImageView backIV ;
    private LoadingDialog loadingDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this ;
        TAG = this.getLocalClassName()+"TAG";//xxxxActivityTAG
        config = new Config(this);
        configure(config);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);

        onCreateView();
        initialize();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(config.isRegisterEventBox == true){
            EventBox.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        /*在onDestroy()中注销，是为了能够及时处理*/
        if(config.isRegisterEventBox == true) {
            EventBox.getDefault().unregister(this);
        }
    }

    protected abstract void configure(Config c); //基础配置
    protected abstract int getLayoutId(); //获取布局layoutid
    protected abstract void onCreateView();  //用来对view进行进一步设置
    protected abstract void initialize();  //主要是一些数据的初始化操作
    protected abstract void setListeners(); //监听设置

    /**
     * 不使用切换动画
     * 设置切换动画，子类使用该方法时，前提是 isUseDefaultEnterAnim == fasle
     * @param emActivityAnim 枚举类对象
     */
    protected void withActivityAnim(EmActivityAnim emActivityAnim){
//        overridePendingTransition(emActivityAnim.enterAnimId, emActivityAnim.exitAnimId);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        withActivityAnim(config.activityEnterAnim);
    }

    @Override
    public void finish() {
        super.finish();
        withActivityAnim(config.activityExitAnim);
    }

    /**
     * 方便动态修改config内容，获取到config，
     * 修改config属性值后，调用invalidate
     * @return
     */
    protected Config getConfig(){
        return config ;
    }

    /* 更新设置 */
    protected void invalidate(){
        titleTV.setText(config.title);
        titleTV.setTextColor(config.titleColor);
        backIV.setImageResource(config.toolbarBackResId);
    }

    /**
     * 填装toolbar+子布局
     * @param layoutResID 具体的activity的子布局id
     */
    @Override
    public  void setContentView(int layoutResID) {
        ViewGroup cp = findViewById(android.R.id.content);  //contentParent
        cp.removeAllViews();
        StatusBarUtil.fullScreen(this);
        StatusBarUtil.StatusBarLightMode(this);
        cp.setClipChildren(false);

        RelativeLayout rl = new RelativeLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-1);
        rl.setLayoutParams(lp);
        cp.addView(rl);

            /*底层内容布局*/
            LinearLayout ll = new LinearLayout(this);
            lp = new LinearLayout.LayoutParams(-1,-1) ;
            ll.setLayoutParams(lp);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setClipChildren(false);
            rl.addView(ll);

                View v = null;
                if(config.isUseDefaultImmersePlan == true){
                    /*顶部沉浸站位view*/
                    v = new View(this);
                    lp = new LinearLayout.LayoutParams(-1,StatusBarUtil.getStatusBarHeight(this)) ;
                    v.setLayoutParams(lp);
                    v.setBackgroundColor(config.immerseColor);
                    ll.addView(v);

                    if(config.isUseDefaultToolbar == true){
                        /*toolbar*/
                        ViewGroup toolbar = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.frame_toolbar,null);
                        lp = new LinearLayout.LayoutParams(-1, dp2px(45) );
                        toolbar.setLayoutParams(lp);
                        toolbar.setBackgroundColor(config.immerseColor);//沉浸式部分颜色
                        backIV = (ImageView) toolbar.getChildAt(0);
                        if(config.toolbarBackResId != null){  //null的时候不响应点击事件
                            backIV.setImageResource(config.toolbarBackResId);
                            backIV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CBaseActivity.this.finish();
                                }
                            });
                        }
                        titleTV = (TextView) toolbar.getChildAt(1);
                        titleTV.setTextColor(config.titleColor);
                        titleTV.setTextSize(15);
                        titleTV.setText(config.title);
                        ll.addView(toolbar);
                    }
                }

                /*content*/
                v = LayoutInflater.from(this).inflate(layoutResID,  null) ;
                lp = new LinearLayout.LayoutParams(-1,-1);
                v.setLayoutParams(lp);
                ll.addView(v);


        if(config.isUseDefaultImmersePlan == true && config.isUseDefaultToolbar == true){
            /*渐变阴影*/
            v = new View(this);
            lp = new LinearLayout.LayoutParams(-1,dp2px(3));
            v.setLayoutParams(lp);
            v.setTranslationY(StatusBarUtil.getStatusBarHeight(this) + dp2px(45));
            GradientDrawable gd = new GradientDrawable();
            gd.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            gd.setColors(new int[]{Color.parseColor("#33111111"),
                    Color.parseColor("#09111111"),
                    Color.TRANSPARENT});
            v.setBackground(gd);
            rl.addView(v);
        }
    }

    /**
     * 配置清单
     */
    protected class Config {
        public String title ; //ToolBar title标题文字
        public int immerseColor ; //沉浸式颜色
        public int titleColor ; //toolbar标题内容
        public Integer toolbarBackResId ; //toolbar左侧返回按钮的id , null则隐藏
        public boolean isRegisterEventBox ; //是否注册EventBox
        public boolean isUseDefaultToolbar ; //是否使用默认的ToolBar（保留站位view）
        public boolean isUseDefaultImmersePlan ; //是否使用默认沉浸式方案
        public EmActivityAnim activityEnterAnim ; //activity  enter动画
        public EmActivityAnim activityExitAnim ; //activity exit 动画



        public Config(Context context) {
            immerseColor = Color.WHITE;
            titleColor = Color.BLACK ;
            toolbarBackResId = R.mipmap.frame_toolbar_black_back_icon ;
            isRegisterEventBox = false ;
            isUseDefaultToolbar = true ;
            isUseDefaultImmersePlan = true ;
            activityEnterAnim = EmActivityAnim.ENTER_RIGHT_IN_AND_LEFT_HALF_OUT ;
            activityExitAnim = EmActivityAnim.EXIT_RIGHT_IN_AND_LEFT_OUT ;

        }
    }

    /**
     * Activity 切换动画 枚举类
     */
    protected enum EmActivityAnim {
        //enter:右侧进入
        ENTER_RIGHT_IN(R.anim.activity_anim_right_in, R.anim.activity_anim_none),
        //exit:右侧退出
        EXIT_RIGHT_OUT(R.anim.activity_anim_none, R.anim.activity_anim_right_out),

        //enter:右侧渐变进入
        ENTER_RIGHT_IN_WITH_SCALE(R.anim.activity_anim_right_in_with_scale, R.anim.activity_anim_none),
        //exit:右侧渐变退出
        EXIT_RIGHT_OUT_WITH_SCALE(R.anim.activity_anim_none, R.anim.activity_anim_right_out_with_scale),

        //enter:右进左出，从右侧拉入新的activity，原activity向左侧滑动退出
        ENTER_RIGHT_IN_AND_LEFT_OUT(R.anim.activity_anim_right_in, R.anim.activity_anim_left_out),
        //exit:右进左出，从左侧恢复上一个activity，原activity向右侧滑动退出
        EXIT_RIGHT_IN_AND_LEFT_OUT(R.anim.activity_anim_left_in, R.anim.activity_anim_right_out),

        //enter:右进左出，从右侧拉入新的activity，原activity向左侧滑动（一半）退出
        ENTER_RIGHT_IN_AND_LEFT_HALF_OUT(R.anim.activity_anim_right_in, R.anim.activity_anim_left_half_out),
        //enter:右进左出，从左侧恢复上一个activity，原activity向右侧滑动（一半）退出
        EXIT_RIGHT_IN_AND_LEFT_HALF_OUT(R.anim.activity_anim_right_half_in, R.anim.activity_anim_left_out),

        //enter:中心渐变
        ENTER_SCALE_IN(R.anim.activity_anim_scale_in, R.anim.activity_anim_none),
        //exit:渐变退出
        EXIT_SCALE_OUT(R.anim.activity_anim_none, R.anim.activity_anim_scale_out),

        //啥动画都没有
        WITHOUT_ANIM(0, 0);


        private int enterAnimId; //新activity进入的动画id
        private int exitAnimId; //旧activity退出的动画id

        EmActivityAnim(int enterAnimId, int exitAnimId) {
            this.enterAnimId = enterAnimId;
            this.exitAnimId = exitAnimId;
        }
    }

    /**********************api*******************/
    protected int dp2px(float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected void toast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    protected void loading(){
        if(loadingDialog == null){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    protected void unLoading(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }
}
