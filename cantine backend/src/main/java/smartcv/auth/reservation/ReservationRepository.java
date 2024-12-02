package smartcv.auth.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    // Find reservations by user's email
    List<Reservation> findByUserId(int id);
}
