package com.jameni.jamenilist;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jameni.jamenilistlib.adapter.BrvahAdapter;

public class Myadapter extends BrvahAdapter<TestModel> {


    public Myadapter() {
        super(R.layout.item_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestModel item) {
        helper.setText(R.id.tv, getSelfValue(item.getText()));
    }

}
