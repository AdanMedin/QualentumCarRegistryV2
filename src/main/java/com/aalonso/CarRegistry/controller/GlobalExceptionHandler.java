package com.aalonso.CarRegistry.controller;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Indica que el servidor no pudo entender la solicitud debido a una sintaxis incorrecta. Acceso a un objeto null.
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Indica que la solicitud no pudo completarse debido a una sintaxis incorrecta. Argumentos inválidos.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Indica que la solicitud no pudo completarse debido a un conflicto con el estado actual del recurso
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLockingFailureException(OptimisticLockingFailureException e) {
        return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.CONFLICT);
    }

    // Indica que el servidor no pudo entender la solicitud debido a intento de convertir un objeto a un tipo de clase incorrecto.
    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<Object> handleClassCastException(ClassCastException e) {
        return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Indica que el servidor no soporta la funcionalidad necesaria para cumplir con la solicitud.
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Object> handleUnsupportedOperationException(UnsupportedOperationException e) {
        return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }

    // Indica que el servidor no pudo encontrar el recurso solicitado.
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
