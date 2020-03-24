package com.jameni.jamenilist;


import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jameni.jamenilistlib.adapter.BrvahAdapter;

public class Myadapter extends BrvahAdapter<TestModel> {


    public Myadapter() {
        super(R.layout.item_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestModel item) {
        helper.setText(R.id.tv, getSelfValue(item.getText()));
        addChildClickViewIds(R.id.tv, R.id.tv2);

    }

}
