package br.inatel.dm111promo.api.promo;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class PromoResponse {
    private String id;
    private String name;
    private Date starting;
    private Date expiration;
    private List<PromoRequest.Product> productsForYou;
    private List<PromoRequest.Product> products;

    public PromoResponse() {}

    public PromoResponse(String id, String name, Date starting, Date expiration, List<PromoRequest.Product> productsForYou, List<PromoRequest.Product> products) {
        this.id = id;
        this.name = name;
        this.starting = starting;
        this.expiration = expiration;
        this.productsForYou = productsForYou;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarting() {
        return formatDate(starting);
    }

    public void setStarting(Date starting) {
        this.starting = starting;
    }

    public String getExpiration() {
        return formatDate(expiration);
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public List<PromoRequest.Product> getProductsForYou() {
        return productsForYou;
    }

    public void setProductsForYou(List<PromoRequest.Product> productsForYou) {
        this.productsForYou = productsForYou;
    }

    public List<PromoRequest.Product> getProducts() {
        return products;
    }

    public void setProducts(List<PromoRequest.Product> products) {
        this.products = products;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
