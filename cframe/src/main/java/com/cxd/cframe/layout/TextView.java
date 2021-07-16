package com.cxd.cframe.layout;

import android.content.Context;
import android.util.AttributeSet;

public class TextView extends androidx.appcompat.widget.AppCompatTextView {
    public TextView(Context context) {
        super(context,null);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        new BackgroundAnalysis(context,attrs).analysis(this);
    }
}
