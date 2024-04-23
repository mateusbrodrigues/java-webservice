package br.inatel.dm111promo.api.promo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/*{
    "name": "Promoção do Dia das Mães",
    "starting": "15/04/2024",
    "expiration": "15/05/2024",
    "products": [
        {
        "productId": "id do produto",
        "discount": 15 // Qual o porcentagem do desconto no valor do
    produto. ex: 5, 10, 15, 50, 70... limitado a 99%
        },
        {
        "productId": "id do produto",
        "discount": 15 // Qual o porcentagem do desconto no valor do
    produto. ex: 5, 10, 15, 50, 70... limitado a 99%
        }
    ]
   }
*/
    public class PromoRequest {

        private String id;
        private String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date starting;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date expiration;
        private List<Product> products;

        public PromoRequest() {}

        public PromoRequest(String id, String name, Date starting, Date expiration, List<Product> products) {
            this.id = id;
            this.name = name;
            this.starting = starting;
            this.expiration = expiration;
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

        public Date getStarting() {
            return starting;
        }

        public Date getExpiration() {
            return expiration;
        }

        public List<Product> getProducts() {
            return products;
        }

        public static class Product {
            private String productId;
            private int discount;
            public Product() {}

            @JsonCreator
            public Product(@JsonProperty("productId") String productId,
                           @JsonProperty("discount") int discount) {
                this.productId = productId;
                if (discount <= 0 || discount >= 100) {
                    throw new IllegalArgumentException("Discount must be between 1 and 99.");
                }
                this.discount = discount;
            }

            public String getProductId() {
                return productId;
            }

            public int getDiscount() {
                return discount;
            }
        }

    }
