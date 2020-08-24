package com.cxd.cframe.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.cxd.cframe.utils.DensityUtil;

/**
 * create by chenxiaodong on 2020/7/31
 * 自带阴影、圆角
 */
public class ShadowRelativeLayout extends RelativeLayout {
    public ShadowRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        /*关闭硬件加速*/
        this.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        int conner = DensityUtil.dip2px(context,10);
        int padding = this.getPaddingLeft();
        this.setBackground(new ShapeDrawable(new ShadowShape(context,conner,padding)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*绘制阴影和圆角*/
        super.onDraw(canvas);
    }

    public class ShadowShape extends Shape {
        private final int conner ; //四个圆角半径
        private final int padding ; //阴影的padding
        private Context context ;
        private int w , h ;
        private Path path ;
        private RectF rect ;
        private Paint paint ;

        public ShadowShape(Context context , int conner ,int padding) {
            this.context = context;
            this.conner = conner ;
            this.padding = padding ;

            path = new Path();
            rect = new RectF();

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setShadowLayer(padding,0,0,Color.parseColor("#33111111"));
        }

        @Override
        protected void onResize(float width, float height) {
            super.onResize(width, height);
            w = (int) width;
            h = (int) height;

            path.reset();
            rect.set(padding,padding,w-padding,h-padding);
            path.addRoundRect(rect,conner,conner, Path.Direction.CW);
            path.close();

        }

        @Override
        public void draw(Canvas canvas, Paint p) {
            canvas.drawPath(path,paint);
        }
    }
}
