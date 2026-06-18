package com.hsms.user.repository;

import com.hsms.user.entity.Technician;
import com.hsms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    List<Technician> findByIsAvailableAndIsVerified(Boolean isAvailable, Boolean isVerified);
    List<Technician> findBySkillSetContaining(String skill);
    boolean existsByUser(User user);
}