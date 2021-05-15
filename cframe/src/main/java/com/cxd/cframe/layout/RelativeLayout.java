package com.cxd.cframe.layout;

import android.content.Context;
import android.util.AttributeSet;

public class RelativeLayout extends android.widget.RelativeLayout {
    public RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        new BackgroundAnalysis(context,attrs).analysis(this);

    }

}
