package smartcv.auth.menu;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name ;
    @ElementCollection
    @CollectionTable(name = "valeur", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "valeur")
    private List<String> valeur;




}
