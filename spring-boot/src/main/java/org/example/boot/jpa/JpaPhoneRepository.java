package org.example.boot.jpa;

import java.util.Collection;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaPhoneRepository extends JpaRepository<PhoneEntity, Long> {

    @NonNull
    Collection<PhoneEntity> findByPhoneNumberIgnoreCaseContainingOrderByCreatedDateTimeDesc(@NonNull final String phoneNumberPart);

    @Query(value = "SELECT COUNT(1) = 1 " +
        "FROM phones " +
        "WHERE phones.phone_number = :phone",
        nativeQuery = true)
    boolean isExists(@NonNull @Param("phone") final String phoneNumber);
}
