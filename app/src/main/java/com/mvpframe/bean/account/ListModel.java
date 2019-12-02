package com.mvpframe.bean.account;


import com.mvpframe.bean.base.BaseResponseModel;

import java.util.List;

/**
 * <实体类>
 * <p>
 * Data：2019/07/23
 *
 * @author yong
 */
public class ListModel extends BaseResponseModel<List<ListModel>> {
    /**
     * matchedCount : 0
     * arriveDate : 2019.05.15-2019.05.25
     * totalCount : 10
     * storeNames : ["001号小门店","我要删除你","啊啊啊","控制室","dd","qqqqqqqqqq"]
     * arriveTime : 00:00-00:00
     * stayTime : 0分钟-60分钟
     * taskName : 测试环境999
     * status : 0
     * dbaMatchedCount : 0
     * dbcMatchedCount : 0
     */

    private int matchedCount;
    private String arriveDate;
    private int totalCount;
    private String arriveTime;
    private String stayTime;
    private String taskName;
    private int status;
    private Integer dbaMatchedCount;
    private Integer dbcMatchedCount;
    private List<String> storeNames;

    public int getMatchedCount() {
        return matchedCount;
    }

    public ListModel setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
        return this;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public ListModel setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
        return this;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ListModel setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public ListModel setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
        return this;
    }

    public String getStayTime() {
        return stayTime;
    }

    public ListModel setStayTime(String stayTime) {
        this.stayTime = stayTime;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public ListModel setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public ListModel setStatus(int status) {
        this.status = status;
        return this;
    }

    public Integer getDbaMatchedCount() {
        return dbaMatchedCount;
    }

    public ListModel setDbaMatchedCount(Integer dbaMatchedCount) {
        this.dbaMatchedCount = dbaMatchedCount;
        return this;
    }

    public Integer getDbcMatchedCount() {
        return dbcMatchedCount;
    }

    public ListModel setDbcMatchedCount(Integer dbcMatchedCount) {
        this.dbcMatchedCount = dbcMatchedCount;
        return this;
    }

    public List<String> getStoreNames() {
        return storeNames;
    }

    public ListModel setStoreNames(List<String> storeNames) {
        this.storeNames = storeNames;
        return this;
    }

    /**
     * goods_name : 一级分类01
     * thumbnail : 192.168.0.123:9090/image/goods/15439038990301.png
     * lsDetail : [{"unit":"超大箱","quantity":"16","stock_var":12,"price":800,"goods_measureid":30,"measure_limit":null},{"unit":"大箱","quantity":"8","stock_var":11,"price":500,"goods_measureid":29,"measure_limit":null},{"unit":"件","quantity":"2","stock_var":10,"price":300,"goods_measureid":13,"measure_limit":10},{"unit":"斤","quantity":"10","stock_var":10,"price":200,"goods_measureid":12,"measure_limit":10},{"unit":"瓶","quantity":"4","stock_var":1,"price":200,"goods_measureid":31,"measure_limit":null}]
     * goods_id : 19
     * id : 60
     * state : 1
     * total_stock : 2.0
     */

    private String goods_name;
    private String thumbnail;
    private int goods_id;
    private int id;
    private int state = 1;
    private double total_stock;
    private boolean isGoneDelet;//是否显示删除收藏
    private boolean isAdd = true;//商品添加下拉
    private boolean isAddAllGoods = true;//展示全部商品价格
    private boolean isAddRefresh = false;//添加购物车刷新布局

    public String getGoods_name() {
        return goods_name;
    }

    public ListModel setGoods_name(String goods_name) {
        this.goods_name = goods_name;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public ListModel setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public ListModel setGoods_id(int goods_id) {
        this.goods_id = goods_id;
        return this;
    }

    public int getId() {
        return id;
    }

    public ListModel setId(int id) {
        this.id = id;
        return this;
    }

    public int getState() {
        return state;
    }

    public ListModel setState(int state) {
        this.state = state;
        return this;
    }

    public double getTotal_stock() {
        return total_stock;
    }

    public ListModel setTotal_stock(double total_stock) {
        this.total_stock = total_stock;
        return this;
    }

    public boolean isGoneDelet() {
        return isGoneDelet;
    }

    public ListModel setGoneDelet(boolean goneDelet) {
        isGoneDelet = goneDelet;
        return this;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public ListModel setAdd(boolean add) {
        isAdd = add;
        return this;
    }

    public boolean isAddAllGoods() {
        return isAddAllGoods;
    }

    public ListModel setAddAllGoods(boolean addAllGoods) {
        isAddAllGoods = addAllGoods;
        return this;
    }

    public boolean isAddRefresh() {
        return isAddRefresh;
    }

    public ListModel setAddRefresh(boolean addRefresh) {
        isAddRefresh = addRefresh;
        return this;
    }
}
