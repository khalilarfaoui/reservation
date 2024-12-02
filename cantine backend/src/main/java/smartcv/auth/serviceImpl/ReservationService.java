package smartcv.auth.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartcv.auth.model.User;
import smartcv.auth.menu.MenuRepository;
import smartcv.auth.repository.UserRepository;
import smartcv.auth.reservation.Reservation;
import smartcv.auth.reservation.ReservationDTO;
import smartcv.auth.reservation.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MenuRepository menuRepository; // Assuming you have a MenuRepository to fetch Menu

    @Autowired
    private UserRepository userRepository;


    public List<Reservation> saveReservations(List<ReservationDTO> reservationDTOs, Long userId) {
        List<Reservation> reservations = new ArrayList<>();

        for (ReservationDTO dto : reservationDTOs) {
            Reservation reservation = (dto.getId() != null) ? reservationRepository.getReferenceById(dto.getId()) : new Reservation();
            reservation.setReservationDate(dto.getDate());
            reservation.setSelectedEntree(dto.getEntree());
            reservation.setSelectedMainCourse(dto.getMainCourse());
            reservation.setSelectedGarnish(dto.getGarnish());
            reservation.setSelectedDessert(dto.getDessert());
            reservation.setSelectedSandwich(dto.getSandwich());

            // Fetching Menu and User from the repository
            Optional<User> user = userRepository.findById(userId);

            // Setting menu and user if they are found
            user.ifPresent(reservation::setUser);

            reservations.add(reservation);
        }

        return reservationRepository.saveAll(reservations);
    }
    // New method to get all reservations
    public List<Reservation> getAllReservationsByUser(int id) {
        return reservationRepository.findByUserId(id);
    }
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // New method to get reservation by ID
    public Reservation getReservationById(int id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.orElse(null);
    }

    // New method to delete reservation by ID
    public void deleteReservation(int id) {
        reservationRepository.deleteById(id);
    }
}


