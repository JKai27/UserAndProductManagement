package com.marcapo.template.service;

import com.marcapo.template.documents.Product;
import com.marcapo.template.dto.CreateProductRequest;
import com.marcapo.template.exceptions.PriceCanNotBeLessThanZero;
import com.marcapo.template.exceptions.ProductNotFoundException;
import com.marcapo.template.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProducts(List<CreateProductRequest> requestList) throws PriceCanNotBeLessThanZero {
        List<Product> newProducts = new ArrayList<>();
        for (CreateProductRequest request : requestList) {
            Product product = new Product();
            product.setName(request.getName());
            if (request.getPrice() < 0) {
                throw new PriceCanNotBeLessThanZero("Price can not be less than zero");
            } else {
                product.setPrice(request.getPrice());
            }
            product.setDescription(request.getDescription());
            product.setManufacturingDate(request.getManufacturingDate());
            newProducts.add(product);
        }
        productRepository.saveAll(newProducts);
    }

    public Product findByName(String name) throws Exception {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product with " + name + " is not found. Please enter the exact name of the product."));

    }

    public List<Product> findByPriceBetween(double price1, double price2) throws PriceCanNotBeLessThanZero {
        if (price1 < 0 || price2 < 0) {
            throw new PriceCanNotBeLessThanZero("Price cannot be less than zero");
        }
        return productRepository.findByPriceBetween(price1, price2);
    }

}
