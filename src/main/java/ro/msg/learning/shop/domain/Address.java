package ro.msg.learning.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Embeddable
public class Address {

    @Column(name="ADDRESS")
    @Builder.Default
    private String fullAddress = "";

    @Builder.Default
    private String country = "";

    @Builder.Default
    private String city = "";

    @Builder.Default
    private String region = "";
}
