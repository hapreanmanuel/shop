package ro.msg.learning.shop.unit;

import lombok.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.domain.Customer;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
public class BuilderUnitTest {

    @Test
    public void checkBuilderAnnotation(){
        //Fully specified fields
        Address ad = Address.builder()
                .fullAddress("Hometown")
                .city("Cluj-Napoca")
                .country("Romania")
                .region("CJ")
                .build();
        System.out.println(ad.toString());

        //Default fields
        Address ad2 = Address.builder().build();

        System.out.println(ad2.toString());
    }
    @Test
    public void checkConstructorBuilderAnnotation(){

        //Check the behaviour of builder with Setter(AccesLevel=NONE) on fields

        BuilderAnnotatedDummy a = BuilderAnnotatedDummy.builder()
                .id(24L)
                .name("Jack")
                .nickname("Daniels")
                .secret(22L)
                .build();

        //Print instance with all fields set -> id and secret should have not been accessible through build
        System.out.println(a.toString());

        ConstructorBuilderAnnotatedDummy ad = ConstructorBuilderAnnotatedDummy.builder()
                .name("Joshua")
                .nickname("King")
                .build();

        /*
            Print instance with name and nickname set through builder
            - should have secret from Builder.Default (hardcoded value)
            - should have an ID (lombok.Generated annotation)           (not)
         */

        System.out.println(ad.toString());

        //Using Customer class
        Customer c1 = Customer.builder()//Specific id (should not be possible and generated automatically)
                .firstName("Mat")
                .lastName("Rick")
                .userName("stranger101")
                .build();
        Customer c2 = Customer.builder()            //id field has been omited
                .firstName("SS")
                .lastName("Patrick")
                .userName("Master of champions")
                .build();

        Customer c3 = Customer.builder()            //id field has been omited -> see if incremented
                .firstName("SS")
                .lastName("Patrick")
                .userName("Pro chess player")
                .build();

        Customer c4 = Customer.builder()            //Nickname allready in use ?
                .firstName("SS")
                .lastName("Patrick")
                .userName("Master of champions")
                .build();

        System.out.println(c1.toString());
        System.out.println(c2.toString());
        System.out.println(c3.toString());
        System.out.println(c4.toString());
    }

    @Autowired
    private NoDataDummyRepo dataDummyRepo;

    @Test
    public void noDataAnnotationDummyBuilder(){
        NoDataAnnotationDummy d = NoDataAnnotationDummy.builder()
                .firstName("Mike")
                .lastName("Jacks")
                .userName("cha ching")
                .build();
        NoDataAnnotationDummy d2 = NoDataAnnotationDummy.builder()
                .firstName("Mike2")
                .lastName("Jacks2")
                .userName("cha ching2")
                .build();

        System.out.println(d.toString());
        System.out.println(d2.toString());

        //Save entity
        dataDummyRepo.save(d);

     //   dataDummyRepo.save(d2);



    }


}
@Builder
@Data
class BuilderAnnotatedDummy{

    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String nickname;
    private Long secret;

}


@Data
class ConstructorBuilderAnnotatedDummy{

    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private Long secret = 21237192312L;

    @Builder
    //We don't want secret as part of the builder
    public ConstructorBuilderAnnotatedDummy(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }
}

@Entity
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
class NoDataAnnotationDummy{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    private String firstName;

    private String lastName;

    private String userName;
}

interface NoDataDummyRepo extends JpaRepository<NoDataAnnotationDummy, Integer> {}

