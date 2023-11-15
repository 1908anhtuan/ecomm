    package fontys.individual.school.persistence.entity;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.experimental.SuperBuilder;

    import javax.persistence.Column;
    import javax.persistence.DiscriminatorValue;
    import javax.persistence.Entity;
    import javax.persistence.Table;

    @Entity
    @Table(name = "home_delivery")
    @DiscriminatorValue("H")
    @SuperBuilder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class HomeDeliveryEntity extends DeliveryEntity{
        @Column(name = "delivery_description",nullable = true, columnDefinition = "char(50) default 'empty'")
        private String deliveryDescription;
        @Column(name = "post_code",nullable = true, columnDefinition = "char(50) default 'empty'")
        private String postCode;
        @Column(name = "city",nullable = true, columnDefinition = "char(50) default 'empty'")
        private String city;
        @Column(name = "street",nullable = true, columnDefinition = "char(50) default 'empty'")
        private String street;
        @Column(name = "house_number",nullable = true, columnDefinition = "char(50) default 'empty'")
        private String houseNumber;
    }
