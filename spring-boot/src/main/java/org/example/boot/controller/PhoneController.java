package org.example.boot.controller;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.boot.model.Request;
import org.example.boot.model.Response;
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
public class PhoneController {

    private final Collection<String> phoneNumbers = Sets.newConcurrentHashSet(); // Multi-thread impl as DB


    @Operation(summary = "Get number with filter") // OpenAPI main annotation
    @GetMapping // url tomcat:port + /api/v1?path=value
    @Cacheable(value = "dummy", key = "#root.methodName") // Тут создается мапа 'dummy' и кэш записывается по String ключ = имени метода
    // @Cacheable(value = "dummy") // Тут создается мапа 'dummy' и кэш записывается по Object ключ 'new SimpleKey(path, servletRequest)'
    public Collection<String> getAllReports(@RequestParam(required = false) String path,
                                            HttpServletRequest servletRequest) {
        log.info("[REST] GET on url " + url(servletRequest));

        if (StringUtils.isBlank(path)) {
            // return phoneNumbers; // Вот оно !!! Почему не работал кэш !!! Он запоминал ссылку на коллекцию, которая обновлялась !!!
            // return Collections.unmodifiableCollection(phoneNumbers); // И так тоже, ибо этот метод не копирует, а выдает представление
            return ImmutableSet.copyOf(phoneNumbers);
        } else {
            return phoneNumbers.stream().filter(p -> p.contains(path)).collect(Collectors.toSet());
        }
    }

    @Operation(summary = "Save number") // OpenAPI main annotation
    @PostMapping //  url tomcat:port + /api/v1
    @ResponseStatus(HttpStatus.CREATED)
    // @CacheEvict(value = "dummy", allEntries = true) // Clear cache
    public Collection<Response> generateReport(
        @RequestBody @NotNull @Valid Request request,
        HttpServletRequest servletRequest
    ) {
        log.info("[REST] POST on url " + url(servletRequest));

        return request.getPhoneNumbers().stream().map(phone -> {
                Response response = new Response();
                response.setPhoneNumber(phone);
                response.setExists(!phoneNumbers.add(phone));
                return response;
            }
        ).collect(Collectors.toSet());

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    private static String url(final HttpServletRequest request) {
        return String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
    }
}