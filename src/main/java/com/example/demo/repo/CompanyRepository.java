package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	boolean existsByName(String name);

}
