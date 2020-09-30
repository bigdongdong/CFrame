package com.cxd.cframe.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * 2020/9/30
 * 实现抖音评论弹窗的parent
 * 可以用于各种情况
 * 唯一缺陷：先下拉后上提，contentView底部被裁剪，
 *      而且手指越过top会触发dialog的dismiss事件
 */
public class LikeDouYinFrameLayout extends FrameLayout {
    private final String TAG = "LikeDouYinParentLayout";
    private final float DEFAULT_CLOSE_POSITION_RATIO = 0.35f ; //默认关闭位置坐标比例
    private final int DEFAULT_CLOSE_DURATION = 300 ; //默认关闭时间
    private VelocityTracker velocityTracker ;
    private final int DEFAULT_MAX_CLOSE_TIMESTAMP = 150 ;

    private ValueAnimator mAnimator; //回弹和关闭的属性动画
    private OnCloseListener onCloseListener ;

    public LikeDouYinFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float mEventDownY ;
    private float mLastEventMoveY ;
    private long mEventDownTimestamp ;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(!isContentViewReachTheTop()){
            return false;
        }
        final int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastEventMoveY = mEventDownY = event.getY();
                mEventDownTimestamp = System.currentTimeMillis();
                return false;
            case MotionEvent.ACTION_MOVE:
                final float curEventMoveY = event.getY();
                final float moveDy = curEventMoveY - mLastEventMoveY ;
                mLastEventMoveY = curEventMoveY ;

                if(moveDy > 0 && isContentViewReachTheTop()){
                    return true ;
                }else if(moveDy < 0 && getTop() > 0){
                    return true ;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(getTop() > 0){
                    return true ;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_MOVE:
                initVelocityTrackerIfNotExists();
                velocityTracker.addMovement(event);
                final float moveDy = event.getY() - mEventDownY;
                layout(0, (int) (getTop()+moveDy),getMeasuredWidth(),getMeasuredHeight());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                initVelocityTrackerIfNotExists();
                velocityTracker.computeCurrentVelocity(1000);
                if(getTop() > getMeasuredHeight() * DEFAULT_CLOSE_POSITION_RATIO){
                    //关闭
                    closeOnAnimation();
                    velocityTracker.clear();
                }else if(System.currentTimeMillis() - mEventDownTimestamp < DEFAULT_MAX_CLOSE_TIMESTAMP){
                    //关闭
                    closeOnAnimation();
                    velocityTracker.clear();
                }else{
                    //回弹
                    springBackOnAnimation();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void initVelocityTrackerIfNotExists(){
        if(velocityTracker == null){
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private void initAnimatorIfNotExists(){
        if(mAnimator == null){
            mAnimator = new ValueAnimator();
            mAnimator.setDuration(DEFAULT_CLOSE_DURATION);
            mAnimator.setInterpolator(new DecelerateInterpolator());
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setEnabled(true);
                    if(onCloseListener != null && getTop() == getMeasuredHeight()){
                        onCloseListener.onClose();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    setEnabled(false);
                }
            });
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    layout(0,(int)animation.getAnimatedValue(),getMeasuredWidth(),getMeasuredHeight());
                }
            });
        }
    }

    private boolean isContentViewReachTheTop(){
        View contentView = getChildAt(0);
        if(contentView == null){
            return true;
        }else{
            return contentView.canScrollVertically(1);
        }

    }

    private void springBackOnAnimation(){
        initAnimatorIfNotExists();
        mAnimator.setIntValues(getTop(),0);
        mAnimator.start();
    }

    private void closeOnAnimation(){
        initAnimatorIfNotExists();
        mAnimator.setIntValues(getTop(),getMeasuredHeight());
        mAnimator.start();
    }

    /********************** api ***********************/
    public void setOnCloseListener(OnCloseListener listener){
        this.onCloseListener = listener;
    }

    public interface OnCloseListener{
        void onClose();
    }
}
