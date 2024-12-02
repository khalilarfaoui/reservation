package smartcv.auth.reservation;

import jakarta.persistence.*;
import lombok.*;
import smartcv.auth.feedback.Feedback;
import smartcv.auth.model.User;


import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date reservationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the user making the reservation

    private String selectedSandwich;  // The selected sandwich option
    private String selectedGarnish;    // The selected garnish option
    private String selectedMainCourse; // The selected main course option
    private String selectedDessert;     // The selected dessert option
    private String selectedEntree;      // The selected entrée option (added)
    @CollectionTable
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Feedback> feedbacks;
    // Additional fields as necessary


    @ElementCollection
    @CollectionTable(name = "reservation", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "reservation")
    private List<String> otherMenu;

    @Override
    public String toString() {
        return "Reservation{" +
                "date=" + reservationDate +
                // Avoid calling user.toString() directly to prevent recursion
                ", userId=" + (user != null ? user.getId() : "null") + // Only show necessary information
                '}';
    }


}
