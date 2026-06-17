package com.hsms.feedbackservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hsms.feedbackservice.dto.FeedbackDTO;
import com.hsms.feedbackservice.entity.Feedback;
import com.hsms.feedbackservice.service.FeedbackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public Feedback saveFeedback(
            @Valid @RequestBody FeedbackDTO feedbackDTO) {

        Feedback feedback = new Feedback();

        feedback.setUserId(feedbackDTO.getUserId());
        feedback.setServiceRequestId(
                feedbackDTO.getServiceRequestId());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComments(feedbackDTO.getComments());

        return feedbackService.saveFeedback(feedback);
    }

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    public Feedback updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackDTO feedbackDTO) {

        Feedback feedback = new Feedback();

        feedback.setUserId(feedbackDTO.getUserId());
        feedback.setServiceRequestId(
                feedbackDTO.getServiceRequestId());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComments(feedbackDTO.getComments());

        return feedbackService.updateFeedback(id, feedback);
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable Long id) {

        feedbackService.deleteFeedback(id);

        return "Feedback deleted successfully";
    }
}