package ro.msg.learning.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "USER_TABLE")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private int userid;
    @Column(name = "USERNAME", nullable=false,unique=true)
    private String username;

    @JsonIgnore
    @Column(name="PASSWORD")
    private String password;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_AUTHORITY",
            joinColumns =
            @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            inverseJoinColumns =
            @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "AUTHORITY_ID"))
    private Authority authority;

    @Builder
    public User(String username, String password, Authority authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }
}
