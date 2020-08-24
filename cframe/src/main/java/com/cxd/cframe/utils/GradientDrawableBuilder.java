package com.cxd.cframe.utils;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * create by chenxiaodong on 2020/7/1
 * 快速生成背景
 */
public class GradientDrawableBuilder {
    private Integer conner ;
    private float[] conners ;
    private Integer color ;
    private int[] colors ;
    private GradientDrawable.Orientation orientation ;
    private Integer storkeColor ;
    private Integer storkeWidth ;

    public GradientDrawable build(){
        GradientDrawable gd = new GradientDrawable();
        if(conner != null){
            gd.setCornerRadius(conner);
        }

        if(conners != null){
            gd.setCornerRadii(conners);
        }

        if(color != null){
            gd.setColor(color);
        }

        if(colors != null){
            gd.setColors(colors);
        }

        if(orientation != null){
            gd.setOrientation(orientation);
        }

        if(storkeWidth != null && storkeColor != null){
            gd.setStroke(storkeWidth,storkeColor);
        }

        return gd;
    }

    public void into(View view){
        GradientDrawable gd = new GradientDrawable();
        if(conner != null){
            gd.setCornerRadius(conner);
        }

        if(conners != null){
            gd.setCornerRadii(conners);
        }

        if(color != null){
            gd.setColor(color);
        }

        if(colors != null){
            gd.setColors(colors);
        }

        if(orientation != null){
            gd.setOrientation(orientation);
        }

        if(storkeWidth != null && storkeColor != null){
            gd.setStroke(storkeWidth,storkeColor);
        }

        view.setBackground(gd);
    }

    public GradientDrawableBuilder conner(int conner){
        this.conner = conner ;
        return this ;
    }

    public GradientDrawableBuilder conners(float[] conners){
        this.conners = conners ;
        return this ;
    }

    public GradientDrawableBuilder color(int color){
        this.color = color ;
        return this ;
    }

    public GradientDrawableBuilder colors(int[] colors){
        this.colors = colors ;
        return this ;
    }

    public GradientDrawableBuilder storkeColor(int storkeColor){
        this.storkeColor = storkeColor ;
        return this ;
    }

    public GradientDrawableBuilder storkeWidth(int storkeWidth){
        this.storkeWidth = storkeWidth ;
        return this ;
    }
    public GradientDrawableBuilder orientation(GradientDrawable.Orientation orientation){
        this.orientation = orientation ;
        return this ;
    }
}
