package com.newbee.maggie.entity;

public class User {

    public User(Integer userId, String contactInformation, String nickname, String defaultShippingAddress, Integer grade, int authority) {
        this.userId = userId;
        this.contactInformation = contactInformation;
        this.nickname = nickname;
        this.defaultShippingAddress = defaultShippingAddress;
        this.grade = grade;
        this.authority = authority;
    }

    public User(String nickname) {
        this.nickname = nickname;
        this.grade = 2018;
        this.authority = 0;
    }

    //用户id
    private Integer userId;
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

    //set用户id
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    //get用户id
    public Integer getUserId(){
        return this.userId;
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
                ", contactInformation='" + contactInformation + '\'' +
                ", nickname='" + nickname + '\'' +
                ", defaultShippingAddress='" + defaultShippingAddress + '\'' +
                ", grade=" + grade +
                ", authority=" + authority +
                '}';
    }
}
