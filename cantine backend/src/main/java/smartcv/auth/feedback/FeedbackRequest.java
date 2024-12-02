package smartcv.auth.feedback;

public class FeedbackRequest {
    private int feedbackRating; // Numeric feedback rating (1-10)
    private int reservationId;

    // Getters and Setters
    public int getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(int feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
}
