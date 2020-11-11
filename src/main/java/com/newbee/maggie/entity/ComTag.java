package com.newbee.maggie.entity;

public class ComTag {
    //商品id
    private Integer cmId;
    //tag
    private String tag;

    public ComTag(Integer cmId, String tag) {
        this.cmId = cmId;
        this.tag = tag;
    }

    public Integer getCmId() {
        return cmId;
    }

    public void setCmId(Integer cmId) {
        this.cmId = cmId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ComTag{" +
                "cmId=" + cmId +
                ", tag='" + tag + '\'' +
                '}';
    }
}