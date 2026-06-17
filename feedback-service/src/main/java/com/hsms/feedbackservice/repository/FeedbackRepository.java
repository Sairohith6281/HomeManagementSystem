package com.hsms.feedbackservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.feedbackservice.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}