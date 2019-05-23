package com.zhongjian.dao.entity.cart.store;

import java.math.BigDecimal;

public class CartStoreStpBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private BigDecimal startingPrice;

    /**
     * 
     */
    private Integer sid;

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
     * 
     * @return starting_price 
     */
    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    /**
     * 
     * @param startingPrice 
     */
    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    /**
     * 
     * @return sid 
     */
    public Integer getSid() {
        return sid;
    }

    /**
     * 
     * @param sid 
     */
    public void setSid(Integer sid) {
        this.sid = sid;
    }
}