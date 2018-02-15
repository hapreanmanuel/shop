package ro.msg.learning.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@Table(name="Location")
public class Location {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    @Column(unique=true)
    private String name;

    @Embedded
    private Address address;

    @Builder
    public Location(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
