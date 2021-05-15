package com.cxd.cframe.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class BackgroundAnalysis {
    private String background ;
    private final String TAG = "BackgroundAnalysis";

    public BackgroundAnalysis(Context context,AttributeSet attrs) {
        /*从attrs中取出android:background*/
        TypedArray ta = context.obtainStyledAttributes(attrs, new int[] {android.R.attr.tag});
        String background = ta.getString(0);
        ta.recycle();

        this.background = background ;
    }

    public void analysis(View view){
        if(background == null || background.equals("null")){
            return;
        }

        /*格式化,取出多余的空格*/
        background = background.replace(" ","");

        /*进行解析*/
        GradientDrawable gd = new GradientDrawable();

        /*数组类*/
        List<String[]> delicates = new ArrayList<>();  //用于存放详细圆角和渐变色
        while(background.contains("[") && background.contains("]")){
            int start = background.indexOf("[") ;
            int end = background.indexOf("]")+1 ;
            String[] array = background.substring(start+1,end-1).split(","); //+1 -1 用来剥离中括号
            background = background.substring(0,start)+background.substring(Math.min(end+1,background.length()));
            delicates.add(array);
        }

        /*常规类*/
        String[] curdes = background.split(",");
        for(String str : curdes){
            /*背景色*/
            if(str.contains("#")){
                gd.setColor(Color.parseColor(str));
                continue;
            }

            /*圆角*/
            if(str.contains("px")){
                str = str.substring(0,str.length()-2) ;
                gd.setCornerRadius(Float.valueOf(str));
                continue;
            }
            if(str.contains("dp")){
                str = str.substring(0,str.length()-2) ;
                gd.setCornerRadius(dip2px(view.getContext(),Float.valueOf(str)));
                continue;
            }
        }

        /*在加上数组类*/
        for(String[] strs : delicates){
            String firstElement = strs[0] ;
            if(firstElement.contains("px") || firstElement.contains("dp") || firstElement.equals("0")){
                /*该strs是圆角*/
                float[] conners = new float[8];
                if(strs.length != 4 && strs.length != 8){
                    throw new RuntimeException("圆角数组长度必须为4或8！");
                }
                /*格式化成8个的*/
                if(strs.length == 4){
                    String[] four = new String[4];
                    for (int i = 0; i < 4; i++) {
                        four[i] = strs[i];
                    }
                    strs = new String[8];
                    strs[0] = strs[1] = four[0];
                    strs[2] = strs[3] = four[1];
                    strs[4] = strs[5] = four[2];
                    strs[6] = strs[7] = four[3];
                }

                for (int i = 0; i < 8; i++) {
                    String str = strs[i];
                    if(str.equals(0)){
                        conners[i] = 0 ;
                    }else if(str.contains("px")){
                        str = str.substring(0,str.length()-2) ;
                        conners[i] = Float.valueOf(str);
                    }else if(str.contains("dp")){
                        str = str.substring(0,str.length()-2) ;
                        conners[i] = dip2px(view.getContext(),Float.valueOf(str));
                    }
                }
                gd.setCornerRadii(conners);

                continue;
            }

            /*背景色渐变*/
            switch (firstElement){
                case "ltr":
                    gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                    break;
                case "rtl":
                    gd.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                    break;
                case "ttb":
                    gd.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                    break;
                case "btt":
                    gd.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                    break;
                case "lttrb":
                    gd.setOrientation(GradientDrawable.Orientation.TL_BR);
                    break;
                case "rttlb":
                    gd.setOrientation(GradientDrawable.Orientation.TR_BL);
                    break;
                case "lbtrt":
                    gd.setOrientation(GradientDrawable.Orientation.BL_TR);
                    break;
                case "rbtlt":
                    gd.setOrientation(GradientDrawable.Orientation.BR_TL);
                    break;
                default:
                    throw new RuntimeException("未指明背景色渐变方向！");
            }
            int[] colors = new int[strs.length-1];
            for (int i = 1; i < strs.length; i++) {
                colors[i-1] = Color.parseColor(strs[i]);
            }
            gd.setColors(colors);

        }

        view.setBackground(gd);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
