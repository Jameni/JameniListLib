package com.jameni.jamenilistlib.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jameni.jamenilistlib.R;
import com.jameni.jamenilistlib.adapter.BrvahAdapter;
import com.jameni.jamenilistlib.adapter.MultiBrvahAdapter;
import com.jameni.jamenilistlib.i.ItemChildViewClickListener;
import com.jameni.jamenilistlib.i.ItemClickListener;
import com.jameni.jamenilistlib.i.ItemLongClickListener;
import com.jameni.jamenilistlib.i.OnLoadMoreListener;
import com.jameni.jamenilistlib.i.OnRefreshListener;
import com.jameni.jamenilistlib.model.ItemSpanModel;

import java.util.List;

/**
 * Created by jameni on 2018/1/23.
 * 封装 刷新列表
 */

public class RefreshView extends RelativeLayout implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemLongClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    private Context mContext;

    private ImageView imgHasNoData;
    private MyRecycleview myRecycleview;
    private SwipeRefreshLayout refreshLayout;
    private OnRefreshListener onRefreshListener;
    private OnLoadMoreListener onLoadMoreListener;

    private ItemChildViewClickListener itemChildViewClickListener;
    private ItemClickListener itemClickListener;
    private ItemLongClickListener itemLongClickListener;

    public RefreshView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_refresh_content, null);

        myRecycleview = contentView.findViewById(R.id.jameni_refresh_list);
        refreshLayout = contentView.findViewById(R.id.jameni_refresh_layout);
        imgHasNoData = contentView.findViewById(R.id.imgHasNoData);
        refreshLayout.setEnabled(false);

        //设置默认刷新圈圈颜色
        setLoadingViewColor(Color.rgb(42, 170, 235));

        addView(contentView);
    }


    public MyRecycleview getRecycleview() {
        return myRecycleview;
    }


    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    //设置流线布局管理器
    public void setLinearManager() {
        setLayoutManager(new LinearLayoutManager(mContext));
    }

    //设置格子布局管理器
    public void setGridManager(int column) {
        setLayoutManager(new GridLayoutManager(mContext, column));
    }

    //设置布局管理器
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (myRecycleview != null) {
            myRecycleview.setLayoutManager(layoutManager);
        }
    }

    //设置不规则的list ，每个item占据的宽度
    public void setItemSpan() {

        if (getRecycleview() != null && getAdapter() != null && getAdapter() instanceof BaseQuickAdapter) {

            ((BaseQuickAdapter) getAdapter()).setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    int szie = 0;
                    if (((BaseQuickAdapter) getAdapter()).getItem(position) instanceof ItemSpanModel) {
                        ItemSpanModel model = (ItemSpanModel) ((BaseQuickAdapter) getAdapter()).getItem(position);
                        szie = model.getItemSpan();
                    }
                    return szie;
                }
            });

        } else {
            Log.i("RefreshView:", "Please set setItemSpan after setAdapter methond");
        }
    }


    //是否开启下来刷新
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            refreshLayout.setEnabled(true);
        }
        refreshLayout.setRefreshing(refreshing);
    }

    //刷新完成，
    public void refreshComplete() {
        setRefreshing(false);
        disableLoadMoreIfNotFullPage();
    }


    //设置刷新监听
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(this);
            refreshLayout.setEnabled(true);
        }
    }

    //加载更多监听
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;

        if (getRecycleview() != null) {

            if (getAdapter() != null) {

                if (getAdapter() instanceof BaseQuickAdapter) {
                    if (onLoadMoreListener != null) {
                        ((BaseQuickAdapter) getAdapter()).setOnLoadMoreListener(this, getRecycleview());
                    }
                }
            } else {
                //请在设置了adapter 之后设置 加载更多监听方法，谢谢，这是我自己写的英文，屌屌的
                Log.i("RefreshView:", "Please set OnLoadMoreListener after setAdapter methond");
            }
        }
    }


    //    item项里的view点击事件监听
    public void setItemChildViewClickListener(ItemChildViewClickListener itemChildViewClickListener) {
        this.itemChildViewClickListener = itemChildViewClickListener;

        if (getRecycleview() != null) {

            if (getAdapter() != null) {

                if (getAdapter() instanceof BaseQuickAdapter) {
                    ((BaseQuickAdapter) getAdapter()).setOnItemChildClickListener(this);
                }
            } else {
                Log.i("RefreshView:", "Please set setItemChildViewClickListener after setAdapter methond");
            }
        }
    }

    //加载更多监听--去掉加载更多，或者加载完成的底部
    public void setOnLoadMoreEnable(boolean enable) {

        if (getRecycleview() != null) {

            if (getAdapter() != null) {

                if (getAdapter() instanceof BaseQuickAdapter) {
                    ((BaseQuickAdapter) getAdapter()).setEnableLoadMore(enable);
                }
            } else {
                //请在设置了adapter 之后设置 加载更多监听方法，谢谢，这是我自己写的英文，屌屌的
                Log.i("RefreshView:", "Please set setOnLoadMoreEnable after setAdapter methond");
            }
        }
    }


    //    加载结束，底部会有提示，没有更多数据
    public void setLoadMoreEnd() {

        if (getRecycleview() != null) {

            if (getAdapter() != null) {

                if (getAdapter() instanceof BaseQuickAdapter) {
                    ((BaseQuickAdapter) getAdapter()).loadMoreEnd();
                }
            } else {
                //请在设置了adapter 之后设置 加载更多监听方法，谢谢，这是我自己写的英文，屌屌的
                Log.i("RefreshView:", "Please set setOnLoadMoreEnable after setAdapter methond");
            }
        }
    }

    //item点击事件监听
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        if (getRecycleview() != null) {
            if (getAdapter() != null) {
                if (getAdapter() instanceof BaseQuickAdapter) {
                    ((BaseQuickAdapter) getAdapter()).setOnItemClickListener(this);
                }
            } else {
                Log.i("RefreshView:", "Please set setItemClickListener after setAdapter methond");
            }
        }
    }

    //item长按点击
    public void setItemLongClickListener(ItemLongClickListener itemClickListener) {
        this.itemLongClickListener = itemClickListener;
        if (getRecycleview() != null) {
            if (getAdapter() != null) {
                if (getAdapter() instanceof BaseQuickAdapter) {
                    ((BaseQuickAdapter) getAdapter()).setOnItemLongClickListener(this);
                }
            } else {
                Log.i("RefreshView:", "Please set setItemClickListener after setAdapter methond");
            }
        }
    }


    //    设置适配器
    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter, false);
    }

    public void setAdapter(RecyclerView.Adapter adapter, boolean isShowEmptyView) {
        if (myRecycleview != null) {
            myRecycleview.setAdapter(adapter);
        }

        setEmptyViewVisiable(isShowEmptyView);
    }


    /// 是否显示空数据 提示图片
    public void setEmptyViewVisiable(boolean visiable) {

        if (imgHasNoData != null) {
            imgHasNoData.setVisibility(visiable ? View.VISIBLE : View.GONE);
        }
    }

    //设置空数据图片
    public void setEmptyImage(int img_resid) {
        if (imgHasNoData != null) {
            imgHasNoData.setImageResource(img_resid);
        }
    }


    //设置列表背景为白色
    public void setListBackgroundWhite() {
        if (myRecycleview != null) {
            myRecycleview.setBackgroundColor(Color.WHITE);
        }
    }

    //设置列表背景
    public void setListBackground(int bg_resId) {
        if (myRecycleview != null) {
            myRecycleview.setBackgroundResource(bg_resId);
        }
    }

    //获取到适配器
    public RecyclerView.Adapter getAdapter() {
        if (myRecycleview != null) {
            return myRecycleview.getAdapter();
        }
        return null;
    }


    //加载更多监听
    @Override
    public void onLoadMoreRequested() {
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
    }


    //设置加载进度圈圈颜色
    public void setLoadingViewColor(int rgbColor) {
        if (refreshLayout != null) {
            refreshLayout.setColorSchemeColors(rgbColor);
        }
    }

    //下拉刷新监听
    @Override
    public void onRefresh() {
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (itemChildViewClickListener != null) {
            itemChildViewClickListener.onItemChildViewClick(view.getId(), position);
        }

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(adapter.getItem(position), position);
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        if (itemLongClickListener != null) {
            itemLongClickListener.onItemLongClick(adapter.getItem(position), position);
        }

        return false;
    }

    public SwipeRefreshLayout.LayoutParams getRecycleviewLayoutParmas() {

        if (getRecycleview() != null) {
            SwipeRefreshLayout.LayoutParams params = getRecycleview().getLayoutParams();
            return params;
        }
        return null;
    }

    public void setRecycleviewPadding(int left, int top, int right, int bottom) {

        if (getRecycleview() != null) {
            getRecycleview().setPadding(left, top, right, bottom);
        }
    }

    //    添加头部
    public View addHeadView(int layoutId) {

        MyRecycleview recycleview = getRecycleview();
        if (recycleview != null && getAdapter() != null) {

            View headView = LayoutInflater.from(mContext).inflate(layoutId, (ViewGroup) recycleview.getParent(), false);

            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            adapter.addHeaderView(headView);
            return headView;
        }
        return null;
    }


    //    添加头部
    public void addHeadView(int layoutId, String viewTag) {

        View headView = addHeadView(layoutId);
        if (viewTag != null && !viewTag.equals("")) {
            //设置tag是为了删除的时候方便,直接通过tag找到相应的headView
            headView.setTag(viewTag);
        }
    }


    //    添加头部
    public void addHeadView(int layoutId, OnClickListener clickListener) {

        View headView = addHeadView(layoutId);
        if (clickListener != null) {
            headView.setOnClickListener(clickListener);
        }
    }

    //    添加头部
    public void addHeadView(int layoutId, OnClickListener clickListener, String viewTag) {

        View headView = addHeadView(layoutId);
        if (clickListener != null) {
            headView.setOnClickListener(clickListener);
        }

        if (viewTag != null && !viewTag.equals("")) {
            //设置tag是为了删除的时候方便,直接通过tag找到相应的footveiw
            headView.setTag(viewTag);
        }

    }


    //    添加头部
    public void addHeadView(View headView) {

        if (getAdapter() != null) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            adapter.addHeaderView(headView);
        }
    }


    /**
     * 删除指定布局的headview
     */
    public void removeHeadView(String viewTag) {

        if (getAdapter() != null && viewTag != null && !viewTag.equals("")) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            LinearLayout headLayout = adapter.getHeaderLayout();
            int count = headLayout.getChildCount();

            if (count > 0) {

                for (int i = 0; i < count; i++) {
                    View childView = headLayout.getChildAt(i);

                    if (childView != null) {
                        String tag = (String) childView.getTag();
                        if (tag != null && !tag.equals("") && tag.equals(viewTag)) {
                            adapter.removeHeaderView(childView);
                            break;
                        }
                    }
                }
            }
        }
    }


    public void removeHeadView(View headView) {

        if (getAdapter() != null && headView != null) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            adapter.removeHeaderView(headView);
        }
    }


    //    添加底部
    public View addFootView(int layoutId) {

        MyRecycleview recycleview = getRecycleview();
        if (recycleview != null && getAdapter() != null) {

            View footView = LayoutInflater.from(mContext).inflate(layoutId, (ViewGroup) recycleview.getParent(), false);

            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            adapter.addFooterView(footView);

            return footView;
        }

        return null;
    }

    //    添加底部
    public void addFootView(int layoutId, String viewTag) {

        View footView = addFootView(layoutId);
        if (viewTag != null && !viewTag.equals("")) {
            //设置tag是为了删除的时候方便,直接通过tag找到相应的footveiw
            footView.setTag(viewTag);
        }
    }


    public void addFootView(int layoutId, OnClickListener clickListener) {

        View footView = addFootView(layoutId);
        if (clickListener != null) {
            footView.setOnClickListener(clickListener);
        }
    }

    public void addFootView(int layoutId, OnClickListener clickListener, String viewTag) {

        View footView = addFootView(layoutId);
        if (clickListener != null) {
            footView.setOnClickListener(clickListener);
        }

        if (viewTag != null && !viewTag.equals("")) {
            //设置tag是为了删除的时候方便,直接通过tag找到相应的footveiw
            footView.setTag(viewTag);
        }

    }


    public void addFootView(View footView) {

        if (getAdapter() != null) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            adapter.addFooterView(footView);
        }
    }


    /**
     * 删除指定布局的footview
     */
    public void removeFootView(String viewTag) {

        if (getAdapter() != null && viewTag != null && !viewTag.equals("")) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            LinearLayout footLayout = adapter.getFooterLayout();
            int count = footLayout.getChildCount();

            if (count > 0) {

                for (int i = 0; i < count; i++) {
                    View childView = footLayout.getChildAt(i);

                    if (childView != null) {
                        String tag = (String) childView.getTag();
                        if (tag != null && !tag.equals("") && tag.equals(viewTag)) {
                            adapter.removeFooterView(childView);
                            break;
                        }
                    }
                }
            }
        }
    }


    public void removeFootView(View footView) {

        if (getAdapter() != null && footView != null) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
            adapter.removeFooterView(footView);
        }
    }

    //设置分割线--水平分割线
    public void setDivider() {
        if (myRecycleview != null) {
            myRecycleview.setDivider();
        }
    }

    //设置分割线--垂直的分割线
    public void setDivider_Vertical() {
        if (myRecycleview != null) {
            myRecycleview.setDivider_Vertical();
        }
    }

    //设置分割线--横竖都有
    public void setDivider_Square() {
        if (myRecycleview != null) {
            myRecycleview.setDivider();
            myRecycleview.setDivider_Vertical();
        }
    }




    public void loadMoreComplete() {
        BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
        if (adapter != null) {
            adapter.loadMoreComplete();
        } else {
            Log.i("RefreshView:", "Please set loadMoreComplete after setAdapter methond");
        }
    }

    //第一次加载会默认调用加载更多，这个方法可以屏蔽
    public void disableLoadMoreIfNotFullPage() {
        BaseQuickAdapter adapter = (BaseQuickAdapter) getAdapter();
        if (adapter != null) {
            adapter.disableLoadMoreIfNotFullPage(getRecycleview());
        } else {
            Log.i("RefreshView:", "Please set loadMoreComplete after setAdapter methond");
        }
    }


    //    当datalist 数据变化后，调用一下这个方法，就刷新了列表
    public void update(List data) {
        if (getAdapter() != null) {
            if (getAdapter() instanceof BrvahAdapter) {
                BrvahAdapter adapter = (BrvahAdapter) getAdapter();
                adapter.update(data);
            } else if (getAdapter() instanceof MultiBrvahAdapter) {
                MultiBrvahAdapter adapter = (MultiBrvahAdapter) getAdapter();
                adapter.update(data);
            }
        }
    }


}
