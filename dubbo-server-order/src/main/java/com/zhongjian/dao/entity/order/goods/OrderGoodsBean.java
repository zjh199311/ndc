package com.zhongjian.dao.entity.order.goods;

import java.math.BigDecimal;

public class OrderGoodsBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 菜品分类
     */
    private Integer cid;

    /**
     * 商户id
     */
    private Integer pid;

    /**
     * 菜品名称
     */
    private String gname;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 单位
     */
    private String unit;

    /**
     * 创建时间
     */
    private Integer ctime;

    /**
     * 是否删除1为删除
     */
    private Integer isDelete;

    /**
     * 正常1下架
     */
    private Byte state;

    /**
     * 菜品图片
     */
    private String pic;

    /**
     * 商品描述
     */
    private String content;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 菜品分类
     * @return cid 菜品分类
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * 菜品分类
     * @param cid 菜品分类
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     * 商户id
     * @return pid 商户id
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 商户id
     * @param pid 商户id
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 菜品名称
     * @return gname 菜品名称
     */
    public String getGname() {
        return gname;
    }

    /**
     * 菜品名称
     * @param gname 菜品名称
     */
    public void setGname(String gname) {
        this.gname = gname == null ? null : gname.trim();
    }

    /**
     * 商品价格
     * @return price 商品价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 商品价格
     * @param price 商品价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 单位
     * @return unit 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 单位
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * 创建时间
     * @return ctime 创建时间
     */
    public Integer getCtime() {
        return ctime;
    }

    /**
     * 创建时间
     * @param ctime 创建时间
     */
    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }

    /**
     * 是否删除1为删除
     * @return is_delete 是否删除1为删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 是否删除1为删除
     * @param isDelete 是否删除1为删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 正常1下架
     * @return state 正常1下架
     */
    public Byte getState() {
        return state;
    }

    /**
     * 正常1下架
     * @param state 正常1下架
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * 菜品图片
     * @return pic 菜品图片
     */
    public String getPic() {
        return pic;
    }

    /**
     * 菜品图片
     * @param pic 菜品图片
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 商品描述
     * @return content 商品描述
     */
    public String getContent() {
        return content;
    }

    /**
     * 商品描述
     * @param content 商品描述
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}