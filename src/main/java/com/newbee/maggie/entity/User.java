package com.newbee.maggie.entity;

public class User {

    public User(Integer userId, String sessionKey, String contactInformation, String nickname, String defaultShippingAddress, Integer grade, int authority, String openId) {
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.contactInformation = contactInformation;
        this.nickname = nickname;
        this.defaultShippingAddress = defaultShippingAddress;
        this.grade = grade;
        this.authority = authority;
        this.openId = openId;
    }

    public User(String nickname){
        this.nickname = nickname;
    }

    public User(String nickname, String sessionKey, String openId) {
        this.nickname = nickname;
        this.sessionKey = sessionKey;
        this.openId = openId;
        this.grade = 2018;
        this.authority = 0;
        this.defaultShippingAddress = "请输入收货地址";
        this.contactInformation = "请输入联系方式";
    }

    //用户id
    private Integer userId;
    //微信生成的session_key
    private String sessionKey;
    //联系信息：手机号
    private String contactInformation;
    //昵称
    private String nickname;
    //默认地址
    private String defaultShippingAddress;
    //年级
    private Integer grade;
    //权限
    private int authority;
    //微信生成的openid
    private String openId;

    //set用户id
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    //get用户id
    public Integer getUserId(){
        return this.userId;
    }
    //setSessionKey
    public void setSessionKey(String sessionKey){
        this.sessionKey = sessionKey;
    }
    //getSessionKey
    public String getSessionKey(){
        return this.sessionKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    //set手机号
    public void setContactInformation(String contactInformation){
        this.contactInformation = contactInformation;
    }
    //get手机号
    public String getContactInformation(){
        return this.contactInformation;
    }
    //set昵称
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    //get昵称
    public String getNickname(){
        return this.nickname;
    }
    //set默认地址
    public void setDefaultShippingAddress(String defaultShippingAddress){
        this.defaultShippingAddress = defaultShippingAddress;
    }
    //get默认地址
    public String getDefaultShippingAddress(){
        return this.defaultShippingAddress;
    }
    //set年级
    public void setGrade(Integer grade){
        this.grade = grade;
    }
    //get年级
    public Integer getGrade(){
        return this.grade;
    }
    //set权限
    public void setAuthority(int authority) {
        this.authority = authority;
    }    //get权限
    //get权限
    public int getAuthority() {
        return authority;
    }

    //重写toString方法
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", sessionKey='" + sessionKey + '\'' +
                ", contactInformation='" + contactInformation + '\'' +
                ", nickname='" + nickname + '\'' +
                ", defaultShippingAddress='" + defaultShippingAddress + '\'' +
                ", grade=" + grade +
                ", authority=" + authority +
                ", openId='" + openId + '\'' +
                '}';
    }
}
