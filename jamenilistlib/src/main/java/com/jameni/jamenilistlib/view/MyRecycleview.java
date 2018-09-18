package com.jameni.jamenilistlib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by jameni on 2018/1/23.
 * 分装了自带分割线
 */

public class MyRecycleview extends RecyclerView {

    private Context mContext;

    public MyRecycleview(Context context) {
        super(context);
        initView(context);
    }

    public MyRecycleview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyRecycleview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(context);

    }

    private void initView(Context context) {

        this.mContext = context;


    }

    public void setDivider() {

        //橫向的分割线
        MyDivider divider = new MyDivider(mContext);
        addItemDecoration(divider);

    }


    public void setDivider_Vertical() {
        //默认是纵向的分割线
        MyDivider divider = new MyDivider(mContext, LinearLayoutManager.VERTICAL);
        addItemDecoration(divider);


    }
}
