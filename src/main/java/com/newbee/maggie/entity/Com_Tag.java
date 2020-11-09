package com.newbee.maggie.entity;

public class Com_Tag {
    //商品id
    private Integer cmId;
    //tag
    private String tag;

    public Com_Tag(Integer cmId, String tag) {
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
        return "Com_Tag{" +
                "cmId=" + cmId +
                ", tag='" + tag + '\'' +
                '}';
    }
}
