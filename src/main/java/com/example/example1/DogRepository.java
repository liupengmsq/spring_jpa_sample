package com.example.example1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DogRepository extends CommonRepository<Dog> {
    Page<Dog> findByFoodContaining(String containFood, Pageable page);
}
