package smartcv.auth.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartcv.auth.serviceImpl.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/save/{userId}")
    public ResponseEntity<List<Reservation>> saveReservations(@PathVariable Long userId, @RequestBody List<ReservationDTO> reservationDTOs) {

        // Log the incoming reservation data for debugging
        System.out.println("Received reservation data: " + reservationDTOs);



        // Validate reservationDTOs (optional)
        if (reservationDTOs == null || reservationDTOs.isEmpty()) {
            System.out.println("Bad request: ReservationDTOs are null or empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Save reservations, optionally associating them with the user
        List<Reservation> savedReservations = reservationService.saveReservations(reservationDTOs, userId);

        // Log saved reservations
        System.out.println("Saved reservations: " + savedReservations);

        return new ResponseEntity<>(savedReservations, HttpStatus.CREATED);
    }
    // Get all reservations
    @GetMapping("/all/{id}")
    public ResponseEntity<List<Reservation>> getAllReservationsByUser(@PathVariable int id) {
        List<Reservation> reservations = reservationService.getAllReservationsByUser(id);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Get reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable int id) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation != null) {
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete reservation by ID
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }







}
