package ro.msg.learning.shop.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name="revenue")
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="revenue_id")
    private int revenueId;

    @OneToOne
    @JoinColumn(name="location_id")
    private Location location;

    @Temporal(TemporalType.DATE)
    @Column(name="date")
    private Date date = new Date();

    @Column(name="total_revenue")
    private BigDecimal total;

    @Builder
    public Revenue(Location location, BigDecimal total) {
        this.location = location;
        this.total = total;
    }
}
