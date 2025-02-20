package com.marcapo.template.repository;

import com.marcapo.template.documents.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(String name);

    List<Product> findByManufacturingDateBetween(String startDate, String endDate);
    List<Product> findByPriceBetween(double price1, double price2);
}
