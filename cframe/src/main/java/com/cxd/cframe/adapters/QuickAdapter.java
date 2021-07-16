package com.cxd.cframe.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class QuickAdapter<E> extends BaseEmptyAdapter {
    private final String TAG = "FreeAdapterTAG";
    private final int TYPE_EMPTY = -Integer.MAX_VALUE ;
    private final int TYPE_NORMAL = -Integer.MAX_VALUE + 1 ;

    private Context context ;
    private List<E> elements ;
    private boolean isInitialize = true ;


    public QuickAdapter(Context context) {
        this.context = context;

        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(isInitialize){
                    isInitialize = false ;
                }
                QuickAdapter.this.unregisterAdapterDataObserver(this);
            }
        });

    }

    public void update(final List<E> list){
        if(elements != null){
            elements.clear();
        }
        if(list != null){
            if(elements == null){
                elements = new ArrayList<>();
            }
            elements.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void append(final List<E> list){
        if(list != null && !list.isEmpty()){
            if(elements == null){
                elements = new ArrayList<>();
            }

            int start = elements.size() ;
            elements.addAll(list);
            if(start == 0){
                notifyDataSetChanged();
            }else{
                notifyItemRangeInserted(start, list.size());
            }
        }
    }

    public List<E> getElements(){
        return elements ;
    }

    public E getElement(int index){
        if(elements == null){
            return null ;
        }
        return elements.get(index);
    }

    /**
     * getXXXXXIdOrView()方法说明：
     * 之所以提供Object的返回选项，是为了多元化空布局的操作空间，让布局view可以跟用户交互
     * 该方法接收布局id或者view实例（方便设置交互）
     */

    protected abstract Object getEmptyIdOrView();

    protected abstract Object getItemViewOrId();

    protected abstract void onBindViewHolder(View item , E element , int position);

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Object oItem = null ;
        View item = null;

        if(i == TYPE_EMPTY){
            oItem = getEmptyIdOrView() ;
        }else if(i == TYPE_NORMAL){
            oItem = getItemViewOrId();
        }

        if(oItem == null){
            throw new NullPointerException("EAdapter#getItemViewOrId() can't be null ");
        }

        if(oItem instanceof Integer){
            item = LayoutInflater.from(context).inflate((int) oItem,viewGroup,false);
        }else if(oItem instanceof View){
            item = (View) oItem;
        }
        return new RecyclerView.ViewHolder(item){};
    }

    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(!isEmpty()){
            onBindViewHolder(viewHolder.itemView,elements.get(i),i);
        }
    }

    private boolean isEmpty(){
        return elements == null || elements.isEmpty() ;
    }

    @Override
    public int getItemViewType(int position) {
        if(isEmpty()){
            return TYPE_EMPTY ;
        }else{
            return TYPE_NORMAL ;
        }
    }

    @Override
    public final int getItemCount() {
        if(isInitialize){
            return 0 ;
        }else if(isEmpty()){
            return getEmptyIdOrView() == null ? 0 : 1 ;
        }else{
            return elements.size();
        }
    }
}
