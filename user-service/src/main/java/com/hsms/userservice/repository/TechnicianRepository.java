package com.hsms.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.userservice.entity.Technician;

public interface TechnicianRepository extends JpaRepository<Technician, Long>{

}