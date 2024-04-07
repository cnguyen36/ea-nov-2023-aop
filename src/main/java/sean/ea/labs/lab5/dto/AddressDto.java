package sean.ea.labs.lab5.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sean.ea.labs.lab5.entity.User;

@Getter
@Setter
public class AddressDto {
    private String street;
    private String zip;
    private String city;
}
