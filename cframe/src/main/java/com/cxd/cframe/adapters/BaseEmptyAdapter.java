package com.cxd.cframe.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

abstract class BaseEmptyAdapter extends RecyclerView.Adapter {
    private final String TAG = "BaseEmptyAdapterTAG" ;
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recycler) {
        super.onAttachedToRecyclerView(recycler);

        RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if(manager != null){
            if(manager instanceof GridLayoutManager){

            }

        }
    }
}
