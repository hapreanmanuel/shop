package ro.msg.learning.shop.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Table(name = "AUTHORITY")
@Data
@Entity
public class Authority{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private int authorityId;

    @Column(nullable=false, unique=true, name="AUTHORITY_NAME")
    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    @Builder
    public Authority(AuthorityType authorityType) {
        this.authorityType = authorityType;
    }
}
