package com.cxd.cframe.layout;

import android.content.Context;
import android.util.AttributeSet;

public class View extends android.view.View {
    public View(Context context) {
        super(context,null);
    }

    public View(Context context, AttributeSet attrs) {
        super(context, attrs);

        new BackgroundAnalysis(context,attrs).analysis(this);
    }
}
