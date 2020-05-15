package org.example.boot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Add phone numbers", description = "Add phone numbers if not exists")
public class Request {

    @Schema(description = "Phone collection", defaultValue = "Phone collection. Each phone number must match to phone pattern",
        example = "[\"+7 903 712 02 61\"]")
    @NotEmpty
    Collection<@Pattern(regexp = "\\+7 [0-9]{3} [0-9]{3} [0-9]{2} [0-9]{2}") @Valid String> phoneNumbers;
}
