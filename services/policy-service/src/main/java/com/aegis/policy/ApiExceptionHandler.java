package com.aegis.policy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PolicyNotFoundException.class)
    public ProblemDetail handlePolicyNotFound(PolicyNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Policy not found");
        return problem;
    }

    @ExceptionHandler(QuoteNotFoundException.class)
    public ProblemDetail handleQuoteNotFound(QuoteNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        problem.setTitle("Quote not found");
        return problem;
    }

    @ExceptionHandler(QuoteNotBindableException.class)
    public ProblemDetail handleNotBindable(QuoteNotBindableException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problem.setTitle("Quote not bindable");
        return problem;
    }

    @ExceptionHandler(QuoteServiceUnavailableException.class)
    public ProblemDetail handleUpstreamDown(QuoteServiceUnavailableException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
        problem.setTitle("Upstream unavailable");
        return problem;
    }
}
