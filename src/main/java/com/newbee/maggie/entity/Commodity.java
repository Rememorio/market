package com.newbee.maggie.entity;

public class Commodity {
    //商品id
    private Integer cmId;
    //商品名称
    private String name;
    //商品分类
    /**
     * 0-置空[不筛选类别]，1-电子产品，2-讲座票，3-校园网，4-日用品，5-书籍，6-文具，7-美妆，8-零食，9-其他
     */
    private int classify;
    //描述
    private String details;
    //价格
    private float price;
    //用户id
    private Integer userId;
    //地址
    /**
     * 0-置空[不筛选地址]，1-大学城，2-五山，3-国际，4-其他
     */
    private int address;
    //图片url
    private String pictureUrl;
    //上传时间
    private String launchTime;
    //是否全新
    /**
     * 0-非全新，1-全新
     */
    private int isNew;
    //状态
    /**
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定，5-已付款售出，6-被举报
     */
    private int state;

    /**
     * 合并图片url
     * @param pictureUrls
     * @return
     */
    private String combineUrls(String[] pictureUrls) {
        StringBuilder pictureUrl = new StringBuilder();
        for (String url: pictureUrls) {
            pictureUrl.append(url).append(",");
        }
        if (!pictureUrl.toString().equals("")) {//如果不为空，则删除最后一个","
            return pictureUrl.substring(0, pictureUrl.length()-1);
        } else {
            return pictureUrl.toString();
        }
    }

    public Commodity(Integer cmId, String name, int classify, String details, float price, Integer userId, int address, String pictureUrl, String launchTime, int isNew, int state) {
        this.cmId = cmId;
        this.name = name;
        this.classify = classify;
        this.details = details;
        this.price = price;
        this.userId = userId;
        this.address = address;
        this.pictureUrl = pictureUrl;
        this.launchTime = launchTime;
        this.isNew = isNew;
        this.state = state;
    }

    public Commodity(String name, int classify, String details, float price, Integer userId, int address, String[] pictureUrls, String launchTime, int isNew) {
        //这个构造函数差cmId
        this.name = name;
        this.classify = classify;
        this.details = details;
        this.price = price;
        this.userId = userId;
        this.address = address;
        this.launchTime = launchTime;
        this.pictureUrl = combineUrls(pictureUrls);
        this.isNew = isNew;
        this.state = 1;//待审核
    }

    public Integer getCmId() {
        return cmId;
    }

    public void setCmId(Integer cmId) {
        this.cmId = cmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "cmId=" + cmId +
                ", name='" + name + '\'' +
                ", classify=" + classify +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", userId=" + userId +
                ", address=" + address +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", date='" + launchTime + '\'' +
                ", isNew=" + isNew +
                ", state=" + state +
                '}';
    }
}
