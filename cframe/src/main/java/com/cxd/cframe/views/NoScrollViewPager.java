package com.cxd.cframe.views;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * create by chenxiaodong on 2019/12/10
 *
 * 禁止左右滑动的viewpager，所有事件都向下分发，自身不做处理即可
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否保留翻页动画，默认是不保留
     * @param isRetainPageTurningAnim
     */
    private boolean mIsRetainPageTurningAnim = false ;
    public void setIsRetainPageTurningAnim(boolean isRetainPageTurningAnim){
        mIsRetainPageTurningAnim = isRetainPageTurningAnim ;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        if(mIsRetainPageTurningAnim){
            super.setCurrentItem(item);
            return;
        }else{
            //去除页面切换时的滑动翻页效果
            super.setCurrentItem(item, false);
            return;
        }
    }
}
