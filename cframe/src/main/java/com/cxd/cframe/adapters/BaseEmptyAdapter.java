package com.cxd.cframe.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
