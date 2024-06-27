package com.example.demo.repo;

import java.util.Collection;
import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.CarPartCategory;
import com.example.demo.entity.Company;
import com.example.demo.entity.Model;
import com.example.demo.entity.Parts;

@Repository
public interface PartsRepository extends JpaRepository<Parts, Long> {

	List<Parts> findByCategoryAndModel(CarPartCategory category, Model model);

	Page<Parts> findByCategoryAndModel(CarPartCategory category, Model model, Pageable pageable);

	Page<Parts> findByCategoryAndModelAndPartTitle(CarPartCategory category, Model model, String partTitle,Pageable pageable);

	Page<Parts> findByModel(Model model, Pageable pageable);

	//////////////
	Page<Parts> findByModelAndCategory(Model model, CarPartCategory carPartCategory, Pageable pageable);

	List<Parts> findByModel(Model model);

	Page<Parts> findByCategory(CarPartCategory carPartCategory, Pageable pageable);

	Page<Parts> findByPartTitle(String partTitle, Pageable pageable);

	List<Parts> findByPartTitleContainingIgnoreCase(String partTitle);

	Page<Parts> findByModelAndPartTitle(Model model, String partTitle, Pageable pageable);

	Page<Parts> findByCategoryAndPartTitle(CarPartCategory carPartCategory, String partTitle, Pageable pageable);

	List<Parts> findByCategory(CarPartCategory category);


    List<Parts> findAllByCategory(CarPartCategory category);
}
