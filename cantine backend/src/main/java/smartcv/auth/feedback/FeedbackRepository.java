package smartcv.auth.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByReservationId(int reservationId);

    long countByFeedbackRatingAndReservation_ReservationDate(int feedbackRating, LocalDate reservationDate);
}
