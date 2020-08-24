package com.cxd.cframe.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPageAdapterV extends PagerAdapter {

    // 界面列表
    private List<View> views;

    public ViewPageAdapterV(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }

        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position>=0 && position<views.size()){
            container.removeView(views.get(position));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    // 判断是否由对象生成界
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }
}