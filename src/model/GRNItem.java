/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Asus
 */
public class GRNItem {
    private String ProductId;
    private String size;
    private String modelName;
    private String brandName;
    private String productName;
    private String stockId;
    private double qty;
    private double bprice;
    private double sprice;
    private Date mfd;
    private Date exp;

    /**
     * @return the ProductId
     */
    public String getProductId() {
        return ProductId;
    }

    /**
     * @param ProductId the ProductId to set
     */
    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the modelName
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * @param modelName the modelName to set
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * @return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the stockId
     */
    public String getStockId() {
        return stockId;
    }

    /**
     * @param stockId the stockId to set
     */
    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    /**
     * @return the qty
     */
    public double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(double qty) {
        this.qty = qty;
    }

    /**
     * @return the bprice
     */
    public double getBprice() {
        return bprice;
    }

    /**
     * @param bprice the bprice to set
     */
    public void setBprice(double bprice) {
        this.bprice = bprice;
    }

    /**
     * @return the sprice
     */
    public double getSprice() {
        return sprice;
    }

    /**
     * @param sprice the sprice to set
     */
    public void setSprice(double sprice) {
        this.sprice = sprice;
    }

    /**
     * @return the mfd
     */
    public Date getMfd() {
        return mfd;
    }

    /**
     * @param mfd the mfd to set
     */
    public void setMfd(Date mfd) {
        this.mfd = mfd;
    }

    /**
     * @return the exp
     */
    public Date getExp() {
        return exp;
    }

    /**
     * @param exp the exp to set
     */
    public void setExp(Date exp) {
        this.exp = exp;
    }

}