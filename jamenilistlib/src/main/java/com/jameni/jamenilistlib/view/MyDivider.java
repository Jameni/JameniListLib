package com.jameni.jamenilistlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jameni.jamenilistlib.R;


/**
 * Created by Jameni on 2018/1/15
 * recycleview 分割线
 */

public class MyDivider extends RecyclerView.ItemDecoration {


    private int dividerWidth;
    private Paint dividerPaint;
    private int orientation;
    private Context context;

    public MyDivider(Context context) {
        init(context, LinearLayoutManager.HORIZONTAL, 2);//默认横线分割线
    }


    public MyDivider(Context context, int orientation) {
        init(context, orientation, 2);
    }

    private void init(Context context, int orientation, int dividerWidth) {
        this.context = context;
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.divider_gray));
        this.dividerWidth = dividerWidth;
        this.orientation = orientation;

    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.bottom = dividerWidth;
        } else {
            outRect.right = dividerWidth;
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizotalLines(c, parent);
        } else {
            drawVerticalLines(c, parent);
        }

    }

    /**
     * 绘制水平布局 横向的分割线
     */
    private void drawHorizotalLines(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerWidth;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

    /**
     * 绘制水平布局 竖直的分割线
     */
    private void drawVerticalLines(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + dividerWidth;
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);

            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }


}
