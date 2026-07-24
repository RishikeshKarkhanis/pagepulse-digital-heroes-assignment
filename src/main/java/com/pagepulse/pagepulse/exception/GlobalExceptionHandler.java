package com.pagepulse.pagepulse.exception;

import java.io.UncheckedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.http.HttpTimeoutException;
import java.nio.channels.UnresolvedAddressException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidUrl(IllegalArgumentException ex, Model model) {

        model.addAttribute("error", ex.getMessage());

        return "index";
    }

    @ExceptionHandler(UncheckedIOException.class)
    public String handleIOException(UncheckedIOException ex, Model model) {

        Throwable t = ex;

        while (t != null) {

            System.out.println(t.getClass().getName());
            System.out.println(t.getMessage());

            if (t instanceof SocketTimeoutException
                    || t instanceof HttpTimeoutException) {

                model.addAttribute(
                        "error",
                        "The website took too long to respond. Please try again later.");

                return "index";
            }

            if (t instanceof UnknownHostException
                    || t instanceof UnresolvedAddressException) {

                model.addAttribute(
                        "error",
                        "Server could not be reached. Please check the website URL.");

                return "index";
            }

            if (t instanceof ConnectException) {

                model.addAttribute(
                        "error",
                        "Unable to establish a connection to the server.");

                return "index";
            }

            t = t.getCause();
        }

        model.addAttribute(
                "error",
                "Unable to fetch the webpage. Please try again.");

        return "index";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {

        model.addAttribute(
                "error",
                "Something went wrong. Please try again.");

        return "index";
    }
}
