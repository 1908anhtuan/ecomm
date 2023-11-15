package fontys.individual.school.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "facilities")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "facility_name", columnDefinition = "char(50) default 'empty'")
    private String name;
    @Column(name = "city", columnDefinition = "char(50) default 'empty'")
    private String city;
    @Column(name = "post_code", columnDefinition = "char(50) default 'empty'")
    private String postCode;
    @Column(name = "open_time", columnDefinition = "TIME")
    private LocalTime openTime;
    @Column(name = "close_time", columnDefinition = "TIME")
    private LocalTime closeTime;


}
