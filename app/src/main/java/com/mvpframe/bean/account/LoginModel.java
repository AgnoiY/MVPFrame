package com.mvpframe.bean.account;

import com.mvpframe.bean.base.BaseResponseModel;

import java.util.List;

/**
 * <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class LoginModel extends BaseResponseModel<LoginModel> {

    /**
     * userId : 51
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiNTEiLCJpc3MiOiJTZXJ2aWNlIiwiZXhwIjoxNTU1OTk1NzY4LCJpYXQiOjE1NDgyMTk3Njh9.R3vVgsugepFWyTd06Pms3pnBU_1qck9WbP8JqCM6N-c
     */

    private int userId;
    private String token;
    /**
     * total : 6
     * list : [{"id":135,"orderNum":"20190506121558430010499712003055","orderId":98,"type":3,"title":"订单取消","content":"您的订单20190506121558430010499712003055已成功取消。如有疑问请联系客服400-121-143。","time":"2019-05-06 12:30"},{"id":133,"orderNum":"20190506121558430010499712003055","orderId":98,"type":2,"title":"待支付订单","content":"您的订单20190506121558430010499712003055尚未支付成功，请在15分钟内完成支付，逾期订单将被取消","time":"2019-05-06 12:15"},{"id":80,"orderNum":"20190422183319113010499712007428","orderId":60,"type":2,"title":"待支付订单","content":"您的订单20190422183319113010499712007428尚未支付成功，请在15分钟内完成支付，逾期订单将被取消","time":"2019-04-22 18:33"},{"id":76,"orderNum":"20190422171812338010499712003689","orderId":56,"type":2,"title":"待支付订单","content":"您的订单20190422171812338010499712003689尚未支付成功，请在15分钟内完成支付，逾期订单将被取消","time":"2019-04-22 17:18"},{"id":75,"orderNum":"20190422170149665010499712002306","orderId":55,"type":2,"title":"待支付订单","content":"您的订单20190422170149665010499712002306尚未支付成功，请在15分钟内完成支付，逾期订单将被取消","time":"2019-04-22 17:01"},{"id":73,"orderNum":"20190422163825182010499712008090","orderId":53,"type":2,"title":"待支付订单","content":"您的订单20190422163825182010499712008090尚未支付成功，请在15分钟内完成支付，逾期订单将被取消","time":"2019-04-22 16:38"}]
     * pageNum : 1
     * pageSize : 20
     * size : 6
     * startRow : 1
     * endRow : 6
     * pages : 1
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * firstPage : 1
     * lastPage : 1
     */

    private int total;
    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }


    public static class ListBean {
        /**
         * id : 135
         * orderNum : 20190506121558430010499712003055
         * orderId : 98
         * type : 3
         * title : 订单取消
         * content : 您的订单20190506121558430010499712003055已成功取消。如有疑问请联系客服400-121-143。
         * time : 2019-05-06 12:30
         */

        private int id;
        private String orderNum;
        private int orderId;
        private int type;
        private String title;
        private String content;
        private String time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
