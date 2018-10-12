# refreshLib
项目中常用的刷新框架，基于别人的框架再次封装的

        lvMain.refreshComplete();//停止刷新
        lvMain.loadMoreComplete();//加载更多
        lvMain.update(datalist);//刷新列表数据
        //判断是否是底部
        if (isFoot(totalPage, pageIndex)) {
            lvMain.setOnLoadMoreEnable(false);//是最后一页，隐藏加载跟多
        }else {
            lvMain.setOnLoadMoreEnable(true);//是最后一页，展示加载跟多
        }

