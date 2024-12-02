package smartcv.auth.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartcv.auth.feedback.Feedback;
import smartcv.auth.feedback.FeedbackRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbacksByReservationId(int reservationId) {
        return feedbackRepository.findByReservationId(reservationId);
    }

    public Map<Integer, Long> countFeedbacksByDate(LocalDate date) {
        Map<Integer, Long> feedbackCounts = new HashMap<>();

        for (int rating = 1; rating <= 10; rating++) {
            feedbackCounts.put(rating, feedbackRepository.countByFeedbackRatingAndReservation_ReservationDate(rating, date));
        }

        return feedbackCounts;
    }
}
