package org.example.boot.jpa;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
@Table(name = "phones")
public class PhoneEntity {

    public PhoneEntity() {}

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private java.time.LocalDateTime createdDateTime;

    @NotEmpty
    @Pattern(regexp = "\\+7 [0-9]{3} [0-9]{3} [0-9]{2} [0-9]{2}")
    private String phoneNumber;

    public PhoneEntity(String phoneNumber) {
        this.createdDateTime = LocalDateTime.now();
        this.phoneNumber = phoneNumber;
    }
}
