package com.shoplife.productsmanager.api;

import lombok.Value;

public record Product(String name, double price) {
    public String asJson() {
        return "{\"name\":\"" + name + "\",\"price\":" + price + "}";
    }
}
