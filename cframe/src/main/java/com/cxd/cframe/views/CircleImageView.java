package com.cxd.cframe.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int mWidth , mHeight ;

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.addCircle(mWidth/2,mHeight/2,mWidth/2, Path.Direction.CW);
        canvas.clipPath(path);


        super.onDraw(canvas);
    }
}
