package com.cxd.cframe.views.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cxd.cframe.R;
import com.cxd.cframe.utils.DensityUtil;


public class LoadingDialog extends CBaseDialog {
    public LoadingDialog(Activity context) {
        super(context);
    }

    @Override
    protected void onConfig(Config c) {
        c.canceledOnTouchOutside = false;
        c.cancelAble = false;
    }

    @Override
    protected void onCreateView(View view) {

    }

    @Override
    protected Object getLayoutIdOrView() {
        RelativeLayout rl = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-1);
        rl.setLayoutParams(params);

        View v = new View(context);
        params = new RelativeLayout.LayoutParams(-1,-1);
        v.setLayoutParams(params);
        rl.addView(v);
        v.setOnClickListener(null);

        ImageView iv = new ImageView(context);
        final int w = DensityUtil.dip2px(context,100);
        params = new RelativeLayout.LayoutParams(w,w);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        iv.setLayoutParams(params);
        Glide.with(context).load(R.mipmap.loading).into(iv);
        rl.addView(iv);
        return rl;
    }
}
