package com.hsms.feedbackservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hsms.feedbackservice.dto.NotificationDTO;
import com.hsms.feedbackservice.entity.Feedback;
import com.hsms.feedbackservice.repository.FeedbackRepository;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String NOTIFICATION_URL =
            "http://localhost:8082/api/notifications";

    @Override
    public Feedback saveFeedback(Feedback feedback) {

        // BUSINESS RULE:
        // Only one feedback per service request

        if (feedbackRepository.existsByServiceRequestId(
                feedback.getServiceRequestId())) {

            throw new RuntimeException(
                    "Feedback already exists for this Service Request");
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);

        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(savedFeedback.getUserId());
        notification.setMessage(
                "Thank you for your feedback. Your rating: "
                        + savedFeedback.getRating());
        notification.setStatus("SENT");

        try {
            restTemplate.postForObject(
                    NOTIFICATION_URL,
                    notification,
                    NotificationDTO.class
            );
        } catch (Exception e) {
            System.out.println(
                    "Notification Service failed: "
                            + e.getMessage());
        }

        return savedFeedback;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback getFeedbackById(Long id) {

        return feedbackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Feedback not found with id: " + id));
    }

    @Override
    public Feedback updateFeedback(Long id,
                                   Feedback feedbackDetails) {

        Feedback existingFeedback =
                feedbackRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Feedback not found with id: " + id));

        existingFeedback.setUserId(
                feedbackDetails.getUserId());

        existingFeedback.setServiceRequestId(
                feedbackDetails.getServiceRequestId());

        existingFeedback.setRating(
                feedbackDetails.getRating());

        existingFeedback.setComments(
                feedbackDetails.getComments());

        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public void deleteFeedback(Long id) {

        if (!feedbackRepository.existsById(id)) {

            throw new RuntimeException(
                    "Feedback not found with id: " + id);
        }

        feedbackRepository.deleteById(id);
    }
}