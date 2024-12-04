
package model;

/**
 *
 * @author User
 */
public class InvoiceItem {
    
    private String stockID;
    private String brand;
    private String name;
    private String quantity;
    private String sellingprice;
    private String total;

  
    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

   
    public String getBrand() {
        return brand;
    }

    
    public void setBrand(String brand) {
        this.brand = brand;
    }

  
    public String getName() {
        return name;
    }

   
    public void setName(String name) {
        this.name = name;
    }

   
    public String getQuantity() {
        return quantity;
    }

   
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

  
    public String getSellingprice() {
        return sellingprice;
    }

       public void setSellingprice(String sellingprice) {
        this.sellingprice = sellingprice;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
}
