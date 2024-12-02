package smartcv.auth.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartcv.auth.reservation.Reservation;
import smartcv.auth.reservation.ReservationRepository;
import smartcv.auth.serviceImpl.FeedbackService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping("/add")
    public ResponseEntity<Feedback> addFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        int feedbackRating = feedbackRequest.getFeedbackRating();
        int reservationId = feedbackRequest.getReservationId();

        // Retrieve Reservation entity using the provided ID
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found"));

        // Create Feedback entity
        Feedback feedback = new Feedback();
        feedback.setFeedbackRating(feedbackRating);
        feedback.setReservation(reservation);

        // Save Feedback
        Feedback savedFeedback = feedbackService.addFeedback(feedback);
        return ResponseEntity.ok(savedFeedback);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/menu/{menuId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByMenuId(@PathVariable int menuId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByReservationId(menuId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<Integer, Long>> getFeedbackCountsByDate(@RequestParam String date) {
        LocalDate selectedDate = LocalDate.parse(date); // Assuming date is in "yyyy-MM-dd" format
        Map<Integer, Long> counts = feedbackService.countFeedbacksByDate(selectedDate);
        return ResponseEntity.ok(counts);
    }
}
