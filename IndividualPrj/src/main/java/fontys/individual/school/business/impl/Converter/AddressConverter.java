package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.Address;
import fontys.individual.school.persistence.entity.AddressEntity;
import lombok.NoArgsConstructor;

import javax.swing.text.html.Option;
import java.util.Optional;
@NoArgsConstructor
public class AddressConverter {


    public static Optional<Address> convertToAddress(AddressEntity address){
         Address addressToBeConverted = Address.builder()
                .city(address.getCity())
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .postCode(address.getPostCode())
                .build();
         Optional<Address> addrss = Optional.ofNullable(addressToBeConverted);
         return addrss;

    }
}
