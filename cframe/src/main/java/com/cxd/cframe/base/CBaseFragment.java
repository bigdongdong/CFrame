package com.cxd.cframe.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.cxd.cframe.views.dialog.LoadingDialog;
import com.cxd.eventbox.EventBox;


public abstract class CBaseFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener{
//    private Unbinder unbinder;
    private Config config ;

    protected Activity context ;
    protected View view ;
    protected String TAG ;
    private LoadingDialog loadingDialog ;

    private Boolean isAlreadyInvokedLazyLoad  = false;  //是否已经加载过lazyLoad

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        TAG = this.getClass().getSimpleName()+"TAG";
        view = LayoutInflater.from(context).inflate(getLayoutId(),container,false);
//        unbinder = ButterKnife.bind(this,view);
        config = new Config();

        configure(config);
        onBundle(getArguments());

        view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        return view ;
    }

    protected abstract void configure(Config c);
    protected abstract void onBundle(Bundle b);
    protected abstract int getLayoutId();
    protected abstract void initialize();
    protected abstract void setListeners();
    protected abstract void lazyLoad();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(config.isRegisterEventBox == true){
            EventBox.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();

        /*在onDestroy()中注销，是为了能够及时处理*/
        if(config.isRegisterEventBox == true) {
            EventBox.getDefault().unregister(this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser == false){
            isAlreadyInvokedLazyLoad = false ; //隐藏之后，重新将该变量置为false
        }
    }

    @Override
    public void onGlobalLayout() {

        if(getUserVisibleHint() && isAlreadyInvokedLazyLoad == false){
            //do something ...
            lazyLoad();

            // onGlobalLayout()会重复调用
            // 所以避免多次调用，lazyLoad()一旦调用，立即将 isAlreadyInvokedLazyLoad 置为true
            isAlreadyInvokedLazyLoad = true ;
        }
    }


    protected class Config{
        public boolean isRegisterEventBox ;
        public Config() {
            isRegisterEventBox = false ;
        }
    }

    /********************api*******************/
    protected int dp2px(float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected void toast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    protected void loading(){
        if(loadingDialog == null){
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.show();
    }

    protected void unLoading(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }
}
