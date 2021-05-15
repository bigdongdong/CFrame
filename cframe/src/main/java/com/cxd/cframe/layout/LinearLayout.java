package com.cxd.cframe.layout;

import android.content.Context;
import android.util.AttributeSet;

public class LinearLayout extends android.widget.LinearLayout {
    public LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        new BackgroundAnalysis(context,attrs).analysis(this);

    }

}
