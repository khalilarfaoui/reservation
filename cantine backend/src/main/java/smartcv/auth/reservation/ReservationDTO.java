package smartcv.auth.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.util.Date;

@Data
public class ReservationDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd yyyy", locale = "en", timezone = "UTC")
    private Date date; // Matches the reservation date
    private Integer id;           // Use Integer to allow null values
    private String entree;      // Matches entree
    private String mainCourse;  // Matches platPrincipal
    private String garnish;     // Matches garniture
    private String dessert;      // Matches dessert
    private String sandwich;     // Matches sandwich
    private String otherReservetion;     // Matches sandwich



}
