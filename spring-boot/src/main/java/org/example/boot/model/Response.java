package org.example.boot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Add phone number result", description = "The result of operation add phone numbers if not exists")
public class Response {

    @Schema(description = "Phone", defaultValue = "Added Phone number")
    @Pattern(regexp = "\\+7 [0-9]{3} [0-9]{3} [0-9]{2} [0-9]{2}")
    String phoneNumber;

    @Schema(description = "Phone exists", defaultValue = "If Phone number exists")
    @NotNull
    boolean exists;
}
