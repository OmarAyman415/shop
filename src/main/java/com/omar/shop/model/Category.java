package com.omar.shop.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;


    /* to avoid infinite loop oof showing category's products and product's category and so on,
     we need to use this annotation as it provides in product list only the IDs
        {
            "id": 1,
                "name": "Samsung smart TV",
                "brand": "Samsung",
                "description": "Samsung smart electronics",
                "price": 200.00,
                "inventory": 20,
                "category": {
            "id": 1,
                    "name": "Electronics",
                    "products": [
            1
                    ]
        },
            "images": []
        }

     Without it the result json will be like this
            {
                "id": 1,
                    "name": "Samsung smart TV",
                    "brand": "Samsung",
                    "description": "Samsung smart electronics",
                    "price": 200.00,
                    "inventory": 20,
                    "category": {
                "id": 1,
                        "name": "Electronics",
                        "products": [
                {
                    "id": 1,
                        "name": "Samsung smart TV",
                        "brand": "Samsung",
                        "description": "Samsung smart electronics",
                        "price": 200.00,
                        "inventory": 20,
                        "category": {
                    "id": 1,
                            "name": "Electronics",
                            "products": [
                    {
                        "id": 1,
                            "name": "Samsung smart TV",
                            "brand": "Samsung",
                            "description": "Samsung smart electronics",
                            "price": 200.00,
                            "inventory": 20,
                            "category": {
                        "id": 1,
                                "name": "Electronics",


    */
    @OneToMany(mappedBy = "category")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
