package org.de.jdbc.mapper;

import java.time.LocalDateTime;

public class Product {
    int id;
    String name;
    LocalDateTime updatedAt;
    String contents;
    int price;
    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getContents() {
        return contents;
    }

    public void setPrice(int price) {
        this.price = price;
    }



    public Product(int id, String name, LocalDateTime updatedAt, String contents, int price) {
        this.id = id;
        this.name = name;
        this.updatedAt = updatedAt;
        this.contents = contents;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", updatedAt=" + updatedAt +
                ", contents='" + contents + '\'' +
                ", price=" + price +
                '}';
    }
}
