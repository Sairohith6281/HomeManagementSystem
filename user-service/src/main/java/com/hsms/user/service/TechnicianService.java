package com.hsms.user.service;

import com.hsms.user.dto.TechnicianDTO;
import com.hsms.user.entity.Technician;
import com.hsms.user.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public List<TechnicianDTO> getAllTechnicians() {
        List<Technician> technicians = technicianRepository.findAll();
        return technicians.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TechnicianDTO getTechnicianById(Long id) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        return convertToDTO(technician);
    }

    @Transactional
    public TechnicianDTO createTechnician(TechnicianDTO technicianDTO) {
        Technician technician = Technician.builder()
                .skillSet(technicianDTO.getSkill())
                .experience(technicianDTO.getExperience())
                .rating(technicianDTO.getRating() != null ? 
                    java.math.BigDecimal.valueOf(technicianDTO.getRating()) : 
                    java.math.BigDecimal.ZERO)
                .isAvailable(technicianDTO.getAvailabilityStatus() != null ? 
                    technicianDTO.getAvailabilityStatus() : true)
                .isVerified(technicianDTO.getIsVerified() != null ? 
                    technicianDTO.getIsVerified() : false)
                .build();
        
        technician = technicianRepository.save(technician);
        return convertToDTO(technician);
    }

    @Transactional
    public TechnicianDTO updateTechnician(Long id, TechnicianDTO technicianDTO) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        
        if (technicianDTO.getSkill() != null) {
            technician.setSkillSet(technicianDTO.getSkill());
        }
        if (technicianDTO.getExperience() != null) {
            technician.setExperience(technicianDTO.getExperience());
        }
        if (technicianDTO.getRating() != null) {
            technician.setRating(java.math.BigDecimal.valueOf(technicianDTO.getRating()));
        }
        if (technicianDTO.getAvailabilityStatus() != null) {
            technician.setIsAvailable(technicianDTO.getAvailabilityStatus());
        }
        if (technicianDTO.getIsVerified() != null) {
            technician.setIsVerified(technicianDTO.getIsVerified());
        }
        
        technician = technicianRepository.save(technician);
        return convertToDTO(technician);
    }

    @Transactional
    public void deleteTechnician(Long id) {
        if (!technicianRepository.existsById(id)) {
            throw new RuntimeException("Technician not found");
        }
        technicianRepository.deleteById(id);
    }

    @Transactional
    public TechnicianDTO verifyTechnician(Long id) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        technician.setIsVerified(true);
        technician = technicianRepository.save(technician);
        return convertToDTO(technician);
    }

    @Transactional
    public TechnicianDTO updateAvailability(Long id, Boolean available) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        technician.setIsAvailable(available);
        technician = technicianRepository.save(technician);
        return convertToDTO(technician);
    }

    public List<TechnicianDTO> getAvailableTechnicians() {
        List<Technician> technicians = technicianRepository.findByIsAvailableAndIsVerified(true, true);
        return technicians.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TechnicianDTO> getTechniciansBySkill(String skill) {
        List<Technician> technicians = technicianRepository.findBySkillSetContaining(skill);
        return technicians.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TechnicianDTO> getVerifiedTechnicians() {
        List<Technician> technicians = technicianRepository.findAll()
                .stream()
                .filter(Technician::getIsVerified)
                .collect(Collectors.toList());
        return technicians.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TechnicianDTO convertToDTO(Technician technician) {
        return TechnicianDTO.builder()
                .technicianId(technician.getTechnicianId())
                .firstName(technician.getUser() != null ? technician.getUser().getFirstName() : "")
                .lastName(technician.getUser() != null ? technician.getUser().getLastName() : "")
                .skill(technician.getSkillSet())
                .experience(technician.getExperience())
                .rating(technician.getRating() != null ? technician.getRating().doubleValue() : 0.0)
                .availabilityStatus(technician.getIsAvailable())
                .isVerified(technician.getIsVerified())
                .build();
    }
}
