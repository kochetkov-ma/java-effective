package org.example.boot.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.boot.jpa.JpaPhoneRepository;
import org.example.boot.jpa.PhoneEntity;
import org.example.boot.model.Request;
import org.example.boot.model.Response;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping(path = "/api/v1")
@Transactional
public class PhoneController {

    private final JpaPhoneRepository phoneRepository;


    @Operation(summary = "Get number with filter") // OpenAPI main annotation
    @GetMapping // url tomcat:port + /api/v1?phonePart=value
    @Cacheable(value = "dummy", key = "#root.methodName") // Тут создается мапа 'dummy' и кэш записывается по String ключ = имени метода SpeL
    public Collection<String> getPhone(@RequestParam(required = false) String phonePart,
                                       HttpServletRequest servletRequest) {
        log.info("[REST] GET on url " + url(servletRequest));

        Collection<PhoneEntity> res;
        if (StringUtils.isBlank(phonePart)) {
            res = phoneRepository.findByPhoneNumberIgnoreCaseContainingOrderByCreatedDateTimeDesc("");

        } else {
            res = phoneRepository.findByPhoneNumberIgnoreCaseContainingOrderByCreatedDateTimeDesc(phonePart);
        }

        return res.stream().map(PhoneEntity::getPhoneNumber).collect(Collectors.toList());
    }

    @Operation(summary = "Save number") // OpenAPI main annotation
    @PostMapping //  url tomcat:port + /api/v1
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "dummy", allEntries = true) // Clear cache
    public Collection<Response> addPhone(
        @RequestBody @NotNull @Valid Request request,
        HttpServletRequest servletRequest
    ) {
        log.info("[REST] POST on url " + url(servletRequest));

        return request.getPhoneNumbers().stream().map(phone -> {
                Response response = new Response();
                response.setPhoneNumber(phone);
                response.setExists(phoneRepository.isExists(phone));

                if (!response.isExists()) {
                    phoneRepository.save(new PhoneEntity(phone));
                }

                return response;
            }
        ).collect(Collectors.toSet());

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    //region Private methods
    private static String url(final HttpServletRequest request) {
        return String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
    }
//endregion
}