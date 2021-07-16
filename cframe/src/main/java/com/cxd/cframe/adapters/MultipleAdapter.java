package com.cxd.cframe.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MultipleAdapter extends BaseEmptyAdapter {
    private final String TAG = "MultipleAdapterTAG";
    private Context context ;
    private HashMap<Class<?>,Integer> itemTypesByElementClass;
    private HashMap<Integer,Class<?>> elementClassesByItemType;
    private HashMap<Class<?>,Object> fieldOrMethodsByElementClass;
    private final int TYPE_EMPTY = -Integer.MAX_VALUE ;
    private List<Object> elements;
    private boolean isInitialize = true ;
    private int mAutoIncrementItemType = 0;

    public MultipleAdapter(Context context) {
        this.context = context;

        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(isInitialize){
                    isInitialize = false ;
                }
                MultipleAdapter.this.unregisterAdapterDataObserver(this);
            }
        });

        /*遍历方法*/
        Method[] methods = this.getClass().getMethods();
        for(Method method : methods){
            AdapterItem itemClass = method.getAnnotation(AdapterItem.class);
            if(itemClass == null){
                continue;
            }else{
                register(itemClass.value(),method);
            }
        }

        /*遍历成员变量*/
        Field[] fields = this.getClass().getFields();
        for (Field field :fields){
            AdapterItem itemClass = field.getAnnotation(AdapterItem.class);
            if(itemClass == null){
                continue;
            }else if(field.getType() == int.class || field.getType() == Integer.class){
                register(itemClass.value(),field);
            }
        }
    }

    private void register(Class<?> clazz , Object fieldOrMethod){
        if(fieldOrMethodsByElementClass == null){
            fieldOrMethodsByElementClass = new HashMap<>();
        }

        if(itemTypesByElementClass == null){
            itemTypesByElementClass = new HashMap<>();
        }
        if(elementClassesByItemType == null){
            elementClassesByItemType = new HashMap<>();
        }

        mAutoIncrementItemType += 1;
        fieldOrMethodsByElementClass.put(clazz,fieldOrMethod);
        itemTypesByElementClass.put(clazz, mAutoIncrementItemType);
        elementClassesByItemType.put(mAutoIncrementItemType,clazz);
    }

    public void update(final List<?> list){
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

    public void append(final List<?> list){
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

    public List<?> getElements(){
        return elements ;
    }

    public Object getElement(int index){
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

    protected abstract void onBindViewHolder(View item , Object element , int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Object oItem = null ;
        View item = null;

        if(i == TYPE_EMPTY){
            oItem = getEmptyIdOrView() ;
        }else{
            try {
                Object fieldOrMethod = fieldOrMethodsByElementClass.get(elementClassesByItemType.get(i));
                if(fieldOrMethod instanceof Method){
                    oItem = ((Method)fieldOrMethod).invoke(this);
                }else if(fieldOrMethod instanceof Field){
                    oItem = ((Field) fieldOrMethod).get(this) ;
                    Log.i(TAG, "onCreateViewHolder: "+((Field) fieldOrMethod).get(this));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if(oItem instanceof Integer){
            item = LayoutInflater.from(context).inflate((int) oItem,viewGroup,false);
        }else if(oItem instanceof View){
            item = (View) oItem;
        }
        return new RecyclerView.ViewHolder(item){};

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(!isEmpty()){
            onBindViewHolder(viewHolder.itemView,elements.get(i),i);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isEmpty()){
            return TYPE_EMPTY ;
        }else{
            return itemTypesByElementClass.get(elements.get(position).getClass()) ;
        }
    }

    private boolean isEmpty(){
        return elements == null || elements.isEmpty() ;
    }

    @Override
    public int getItemCount() {
        if(isInitialize){
            return 0 ;
        }else if(isEmpty()){
            return getEmptyIdOrView() == null ? 0 : 1 ;
        }else{
            return elements.size();
        }
    }
}
