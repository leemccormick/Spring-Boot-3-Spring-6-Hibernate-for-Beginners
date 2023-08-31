package com.leemccormick.posdemo.entity;

public class SaleInfo {
    private int countOfAllOrders;
    private double totalAmountSalesOfAllOrders;
    private int countOfPendingOrders;
    private double totalAmountSalesOfPendingOrders;
    private int countOfProcessingOrders;
    private double totalAmountSalesOfProcessingOrders;
    private int countOfShippedOrders;
    private double totalAmountSalesOfShippedOrders;
    private int countOfDeliveredOrders;
    private double totalAmountSalesOfDeliveredOrders;
    private int countOfCancelledOrders;
    private double totalAmountSalesOfCancelledOrders;
    private int countOfProducts;
    private int countOfProductsItem;
    private int countOfProductsOutOfStock;
    private double totalAmountOfProducts;

    public SaleInfo() {
    }

    public SaleInfo(int countOfAllOrders, double totalAmountSalesOfAllOrders, int countOfPendingOrders, double totalAmountSalesOfPendingOrders, int countOfProcessingOrders, double totalAmountSalesOfProcessingOrders, int countOfShippedOrders, double totalAmountSalesOfShippedOrders, int countOfDeliveredOrders, double totalAmountSalesOfDeliveredOrders, int countOfCancelledOrders, double totalAmountSalesOfCancelledOrders, int countOfProducts, int countOfProductsItem, int countOfProductsOutOfStock, double totalAmountOfProducts) {
        this.countOfAllOrders = countOfAllOrders;
        this.totalAmountSalesOfAllOrders = totalAmountSalesOfAllOrders;
        this.countOfPendingOrders = countOfPendingOrders;
        this.totalAmountSalesOfPendingOrders = totalAmountSalesOfPendingOrders;
        this.countOfProcessingOrders = countOfProcessingOrders;
        this.totalAmountSalesOfProcessingOrders = totalAmountSalesOfProcessingOrders;
        this.countOfShippedOrders = countOfShippedOrders;
        this.totalAmountSalesOfShippedOrders = totalAmountSalesOfShippedOrders;
        this.countOfDeliveredOrders = countOfDeliveredOrders;
        this.totalAmountSalesOfDeliveredOrders = totalAmountSalesOfDeliveredOrders;
        this.countOfCancelledOrders = countOfCancelledOrders;
        this.totalAmountSalesOfCancelledOrders = totalAmountSalesOfCancelledOrders;
        this.countOfProducts = countOfProducts;
        this.countOfProductsItem = countOfProductsItem;
        this.countOfProductsOutOfStock = countOfProductsOutOfStock;
        this.totalAmountOfProducts = totalAmountOfProducts;
    }

    public int getCountOfAllOrders() {
        return countOfAllOrders;
    }

    public void setCountOfAllOrders(int countOfAllOrders) {
        this.countOfAllOrders = countOfAllOrders;
    }

    public double getTotalAmountSalesOfAllOrders() {
        return totalAmountSalesOfAllOrders;
    }

    public void setTotalAmountSalesOfAllOrders(double totalAmountSalesOfAllOrders) {
        this.totalAmountSalesOfAllOrders = totalAmountSalesOfAllOrders;
    }

    public int getCountOfPendingOrders() {
        return countOfPendingOrders;
    }

    public void setCountOfPendingOrders(int countOfPendingOrders) {
        this.countOfPendingOrders = countOfPendingOrders;
    }

    public double getTotalAmountSalesOfPendingOrders() {
        return totalAmountSalesOfPendingOrders;
    }

    public void setTotalAmountSalesOfPendingOrders(double totalAmountSalesOfPendingOrders) {
        this.totalAmountSalesOfPendingOrders = totalAmountSalesOfPendingOrders;
    }

    public int getCountOfProcessingOrders() {
        return countOfProcessingOrders;
    }

    public void setCountOfProcessingOrders(int countOfProcessingOrders) {
        this.countOfProcessingOrders = countOfProcessingOrders;
    }

    public double getTotalAmountSalesOfProcessingOrders() {
        return totalAmountSalesOfProcessingOrders;
    }

    public void setTotalAmountSalesOfProcessingOrders(double totalAmountSalesOfProcessingOrders) {
        this.totalAmountSalesOfProcessingOrders = totalAmountSalesOfProcessingOrders;
    }

    public int getCountOfShippedOrders() {
        return countOfShippedOrders;
    }

    public void setCountOfShippedOrders(int countOfShippedOrders) {
        this.countOfShippedOrders = countOfShippedOrders;
    }

    public double getTotalAmountSalesOfShippedOrders() {
        return totalAmountSalesOfShippedOrders;
    }

    public void setTotalAmountSalesOfShippedOrders(double totalAmountSalesOfShippedOrders) {
        this.totalAmountSalesOfShippedOrders = totalAmountSalesOfShippedOrders;
    }

    public int getCountOfDeliveredOrders() {
        return countOfDeliveredOrders;
    }

    public void setCountOfDeliveredOrders(int countOfDeliveredOrders) {
        this.countOfDeliveredOrders = countOfDeliveredOrders;
    }

    public double getTotalAmountSalesOfDeliveredOrders() {
        return totalAmountSalesOfDeliveredOrders;
    }

    public void setTotalAmountSalesOfDeliveredOrders(double totalAmountSalesOfDeliveredOrders) {
        this.totalAmountSalesOfDeliveredOrders = totalAmountSalesOfDeliveredOrders;
    }

    public int getCountOfCancelledOrders() {
        return countOfCancelledOrders;
    }

    public void setCountOfCancelledOrders(int countOfCancelledOrders) {
        this.countOfCancelledOrders = countOfCancelledOrders;
    }

    public double getTotalAmountSalesOfCancelledOrders() {
        return totalAmountSalesOfCancelledOrders;
    }

    public void setTotalAmountSalesOfCancelledOrders(double totalAmountSalesOfCancelledOrders) {
        this.totalAmountSalesOfCancelledOrders = totalAmountSalesOfCancelledOrders;
    }

    public int getCountOfProducts() {
        return countOfProducts;
    }

    public void setCountOfProducts(int countOfProducts) {
        this.countOfProducts = countOfProducts;
    }

    public int getCountOfProductsItem() {
        return countOfProductsItem;
    }

    public void setCountOfProductsItem(int countOfProductsItem) {
        this.countOfProductsItem = countOfProductsItem;
    }

    public int getCountOfProductsOutOfStock() {
        return countOfProductsOutOfStock;
    }

    public void setCountOfProductsOutOfStock(int countOfProductsOutOfStock) {
        this.countOfProductsOutOfStock = countOfProductsOutOfStock;
    }

    public double getTotalAmountOfProducts() {
        return totalAmountOfProducts;
    }

    public void setTotalAmountOfProducts(double totalAmountOfProducts) {
        this.totalAmountOfProducts = totalAmountOfProducts;
    }

    @Override
    public String toString() {
        return "SaleInfo{" +
                "countOfAllOrders=" + countOfAllOrders +
                ", totalAmountSalesOfAllOrders=" + totalAmountSalesOfAllOrders +
                ", countOfPendingOrders=" + countOfPendingOrders +
                ", totalAmountSalesOfPendingOrders=" + totalAmountSalesOfPendingOrders +
                ", countOfProcessingOrders=" + countOfProcessingOrders +
                ", totalAmountSalesOfProcessingOrders=" + totalAmountSalesOfProcessingOrders +
                ", countOfShippedOrders=" + countOfShippedOrders +
                ", totalAmountSalesOfShippedOrders=" + totalAmountSalesOfShippedOrders +
                ", countOfDeliveredOrders=" + countOfDeliveredOrders +
                ", totalAmountSalesOfDeliveredOrders=" + totalAmountSalesOfDeliveredOrders +
                ", countOfCancelledOrders=" + countOfCancelledOrders +
                ", totalAmountSalesOfCancelledOrders=" + totalAmountSalesOfCancelledOrders +
                ", countOfProducts=" + countOfProducts +
                ", countOfProductsItem=" + countOfProductsItem +
                ", countOfProductsOutOfStock=" + countOfProductsOutOfStock +
                ", totalAmountOfProducts=" + totalAmountOfProducts +
                '}';
    }
}
