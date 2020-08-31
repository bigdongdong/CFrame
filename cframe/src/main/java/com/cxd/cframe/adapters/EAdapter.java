package com.cxd.cframe.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/*
 * Create by chenxiaodong on 2019/11/23 0023 0:03
 */
public abstract class EAdapter<Bean,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{
    protected final String TAG = "EAdapter->TAG";

    private boolean isInitialize = true ;  //初次加载，也就是recyclerView.setAdapter时，默认显示空白
    private final int TYPE_EMPTY = -9999 ;
    private boolean isEmpty = false ;
    private List<Bean> listData ;
    protected Context context ;


    protected EAdapter(Context context){
        this.context = context ;
    }

    public void update(List<Bean> listData){
        if(isInitialize == true){
            isInitialize = false ;
        }

        if(this.listData == null){
            this.listData = new ArrayList<>();
        }else{
            this.listData.clear();
        }

        if(listData != null){
            this.listData.addAll(listData) ;
        }
        notifyDataSetChanged();
    }

    public void append(List<Bean> listData){
        if(isInitialize == true){
            isInitialize = false ;
        }

        if(this.listData == null){
            this.listData = new ArrayList<>();
        }
        if(listData != null){
            this.listData.addAll(listData) ;
        }
        notifyDataSetChanged();
    }

    protected  List<Bean> getList(){
        return this.listData;
    }

    protected  Bean getBean(int position){
        if(listData!=null && position < listData.size()){
            return listData.get(position);
        }else {
            return null ;
        }
    }

    /**
     * 之所以提供view的返回选项，是为了多元化空布局的操作空间，让空布局view可以跟用户交互
     * @return 获取空布局的布局id或者view实例
     */
    protected abstract Object getEmptyIdOrView();

    protected abstract VH onCreateViewHolder( int itemType ,ViewGroup viewGroup);

    protected abstract void onBindViewHolder(VH holder, Bean bean , int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == TYPE_EMPTY){
            if( getEmptyIdOrView() instanceof Integer){
                return new EViewHolder(LayoutInflater.from(context).inflate((int) getEmptyIdOrView(),viewGroup,false));
            }else if( getEmptyIdOrView() instanceof View){
                return new EViewHolder((View) getEmptyIdOrView());
            }else {
                throw new RuntimeException(" getEmptyIdOrView()只接收布局id或者view类型！！！");
            }
        }else{
            return onCreateViewHolder(i , viewGroup );
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if(isEmpty == false){
            onBindViewHolder((VH)holder,listData.get(i) , i);
        }
    }

    @Override
    public int getItemCount() {
        if(isInitialize == true){
            return 0 ;
        }

        if(listData == null || listData.size() == 0){
            isEmpty = (getEmptyIdOrView() != null);
            return (getEmptyIdOrView() != null) ? 1:0 ;
        }else{
            isEmpty = false ;
            return listData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(getItemCount() == 1 && isEmpty == true){
            return TYPE_EMPTY ;
        }else{
            return super.getItemViewType(position);
        }
    }

    private class EViewHolder extends RecyclerView.ViewHolder{
        public EViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @Deprecated
    public void doTest(final int count){
        List<Bean> os = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            os.add(null);
        }
        this.update(os);
    }

}

