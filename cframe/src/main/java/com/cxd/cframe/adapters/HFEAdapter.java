package com.cxd.cframe.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *               HFEAdapter
 *         ______|||______
 *        |       |      |
 *     Header  Footer  Empty
 *
 *
 * Create by chenxiaodong on 2019/11/23 11:54
 *
 * header & footer's priority is higher than empty
 * @param <Bean>
 * @param <VH>
 */
public abstract class HFEAdapter<Bean,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{
    protected final String TAG = "HFEAdapter->TAG";

    private final int TYPE_HEADER = -9999 ;
    private final int TYPE_FOOTER = -9998 ;
    private final int TYPE_EMPTY = -9997 ;

    private boolean isInitialize = true ;  //初次加载，也就是recyclerView.setAdapter时，默认显示空白
    private Boolean isEmpty = false ;
    private Boolean hasHeader = null ;
    private Boolean hasFooter = null ;
    private List<Bean> listData ;

    protected Context context ;

    protected HFEAdapter(Context context){
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

    protected List<Bean> getList(){
        return this.listData;
    }

    protected Bean getBean(int position){
        if(listData!=null && position < listData.size()){
            return listData.get(position);
        }else {
            return null ;
        }
    }

    /**
     * getXXXXXIdOrView()方法说明：
     * 之所以提供Object的返回选项，是为了多元化空布局的操作空间，让布局view可以跟用户交互
     * 该方法接收布局id或者view实例（方便设置交互）
     * 当使用view作为返回内容时，需要给view动态设置宽度
     */

    protected abstract Object getHeaderIdOrView();

    protected abstract Object getFooterIdOrView();

    protected abstract Object getEmptyIdOrView();

    protected abstract VH onCreateViewHolder(int itemType , ViewGroup parent);


    /**
     *
     * @param holder
     * @param bean
     * @param position 除去header和footer之后的position
     */
    protected abstract void onBindViewHolder(@NotNull VH holder, Bean bean , int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == TYPE_HEADER){
            if(getHeaderIdOrView() instanceof Integer){
                return new HFEViewholder(LayoutInflater.from(context).inflate((int) getHeaderIdOrView(),viewGroup,false));
            }else if(getHeaderIdOrView() instanceof View){
                return new HFEViewholder((View)getHeaderIdOrView());
            }else {
                throw new RuntimeException(" getHeaderIdOrView()只接收布局id或者view类型！！！");
            }
        }

        if(i == TYPE_FOOTER){
            if(getFooterIdOrView() instanceof Integer){
                return new HFEViewholder(LayoutInflater.from(context).inflate((int) getFooterIdOrView(),viewGroup,false));
            }else if(getFooterIdOrView() instanceof View){
                return new HFEViewholder( (View) getFooterIdOrView());
            }else {
                throw new RuntimeException(" getFooterIdOrView()只接收布局id或者view类型！！！");
            }
        }

        if(i == TYPE_EMPTY){
            if( getEmptyIdOrView() instanceof Integer){
                return new HFEViewholder(LayoutInflater.from(context).inflate((int) getEmptyIdOrView(),viewGroup,false));
            }else if( getEmptyIdOrView() instanceof View){
                return new HFEViewholder((View) getEmptyIdOrView());
            }else {
                throw new RuntimeException(" getEmptyIdOrView()只接收布局id或者view类型！！！");
            }
        }

        return onCreateViewHolder(i , viewGroup );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if(hasHeader == null || hasFooter == null){
            throw new RuntimeException("异常");
        }

        if(isEmpty == false && listData != null ){
            if(hasHeader == true && hasFooter == true && i>0 && i<=listData.size()){//有头有尾
                onBindViewHolder((VH)holder,listData.get(i-1) , i-1);
            }

            if(hasHeader == true && hasFooter == false && i>0){//有头没尾
                onBindViewHolder((VH)holder,listData.get(i-1) , i-1);
            }

            if(hasHeader == false && hasFooter == true && i<listData.size()){//无头有尾
                onBindViewHolder((VH)holder,listData.get(i) , i);
            }

            if(hasHeader == false && hasFooter == false){ //无头无尾
                onBindViewHolder((VH)holder,listData.get(i) , i);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(isInitialize == true){
            return 0 ;
        }

        int count = 0 ;
        if(hasHeader == null ){
            hasHeader = (getHeaderIdOrView() != null);
        }else if(hasHeader == true){
            count ++ ;
        }

        if(hasFooter == null ){
            hasFooter = (getFooterIdOrView() != null);
        }else if(hasFooter == true){
            count ++ ;
        }

        if(listData == null || listData.size() == 0){
            if(count > 0){
                //list为空，但是有头尾布局，则空布局失效
                isEmpty = false ;
                return count ;
            }else{
                isEmpty = true ;
                return (getEmptyIdOrView() != null) ? 1:0 ;
            }
        }else{
            isEmpty = false ;
            return count + listData.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(getItemCount() == 1 && isEmpty == true){
            return TYPE_EMPTY ;
        }

        if(hasHeader == true && position == 0){
            return TYPE_HEADER ;
        }

        if(listData != null && listData.size()>0){
            if(hasHeader == true && hasFooter == true && position == listData.size()+1){//有头有尾
                return TYPE_FOOTER ;
            }else if(hasHeader == false && hasFooter == true  && position == listData.size()){//无头有尾
                return TYPE_FOOTER ;
            }
        }else{
            if(hasHeader == true && hasFooter == true && position == 1){//有头有尾
                return TYPE_FOOTER ;
            }else if(hasHeader != null && hasFooter == true && position == 0){//无头有尾
                return TYPE_FOOTER ;
            }
        }

        return super.getItemViewType(position);
    }

    private class HFEViewholder extends RecyclerView.ViewHolder{
        public HFEViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}