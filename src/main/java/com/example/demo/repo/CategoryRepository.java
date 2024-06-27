package com.example.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Parts;
import com.example.demo.entity.CarPartCategory;

@Repository
public interface CategoryRepository extends JpaRepository<CarPartCategory, Long> {

	List<Parts> findPartsByCategoryId(Long categoryId);

	Optional<CarPartCategory> findByCategoryName(String categoryName);


}
