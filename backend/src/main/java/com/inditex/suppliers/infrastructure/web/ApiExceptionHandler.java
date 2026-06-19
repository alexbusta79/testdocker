package com.inditex.suppliers.infrastructure.web;

import com.inditex.suppliers.domain.exception.BusinessException;
import com.inditex.suppliers.infrastructure.country.CountryServiceUnavailableException;
import com.inditex.suppliers.infrastructure.web.dto.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	ResponseEntity<ErrorDto> business(BusinessException ex) {
		if (ex.status() == HttpStatus.NOT_FOUND)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.status(ex.status()).body(new ErrorDto(ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ErrorDto> unprocessable(MethodArgumentNotValidException ex) {
		return ResponseEntity.unprocessableEntity().body(new ErrorDto("Unprocessable Content"));
	}

	@ExceptionHandler({ BindException.class, ConstraintViolationException.class, IllegalArgumentException.class })
	ResponseEntity<ErrorDto> badRequest(Exception ex) {
		return ResponseEntity.badRequest().body(new ErrorDto("Bad Request"));
	}

	@ExceptionHandler(CountryServiceUnavailableException.class)
	ResponseEntity<ErrorDto> country(CountryServiceUnavailableException ex) {
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorDto("Country service unavailable"));
	}
}
