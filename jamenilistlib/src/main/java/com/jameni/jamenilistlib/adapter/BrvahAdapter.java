package com.jameni.jamenilistlib.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jameni on 2018/7/12
 */
public abstract class BrvahAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BrvahAdapter(int layoutId) {
        super(layoutId);
    }

    public void update(List data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        notifyDataSetChanged();
    }


    public void setListData(List data) {
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    public boolean isNotNull(Object obj) {
        return obj != null;
    }

    public boolean isNotEmpty(String txt) {
        return txt != null && !txt.equals("");
    }


    public String getSelfValue(String txt) {
        return isNotNull(txt) ? txt : "";
    }
}
