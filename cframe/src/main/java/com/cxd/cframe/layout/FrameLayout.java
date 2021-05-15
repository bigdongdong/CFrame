package com.cxd.cframe.layout;

import android.content.Context;
import android.util.AttributeSet;

public class FrameLayout extends android.widget.FrameLayout {
    public FrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        new BackgroundAnalysis(context,attrs).analysis(this);

    }

}
