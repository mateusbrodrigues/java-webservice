package br.inatel.dm111promo.api.product;

public record ProductRequest(String name,
                             String brand,
                             String unit,
                             long amount,
                             long price) {
}
