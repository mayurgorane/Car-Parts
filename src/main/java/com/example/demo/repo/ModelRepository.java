package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

	List<Model> findAllByCompany_Id(Long companyId);

	List<Model> findByCompanyId(Long companyId);

}
