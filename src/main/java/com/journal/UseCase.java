package com.journal;

import javaslang.Function1;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.control.Try;
import javaslang.control.Validation;

public class UseCase {

    private static Function1<Void, Requested> receiveRequest = (Void) -> {
        return new Requested("name", "email");
    };

    private static Validation<String, Requested> validateName(Requested requested) {
        return !requested.getName().isEmpty()
            ? Validation.valid(requested)
            : Validation.invalid("Name is empty");
    }

    private static Validation<String, Requested> nameLength50(Requested requested) {
        return requested.getName().length() >= 50
            ? Validation.valid(requested)
            : Validation.invalid("name length must >= 50 characters");
    }

    private static Validation<String, Requested> emailNotBlank(Requested requested) {
        return !requested.getEmail().isEmpty()
            ? Validation.valid(requested)
            : Validation.invalid("email is empty");
    }

    private static Function1<Requested, Validation<List<String>, Requested>> validateRequest = (requested) -> {
        return Validation.combine(
            validateName(requested),
            nameLength50(requested),
            emailNotBlank(requested)
        ).ap((a, b, c) -> requested);
    };

    private static Function1<Requested, Requested> canonicalizeEmail = (requested) -> {
        return requested.withEmail("canonicalizeEmail");
    };

    private static Function1<Requested, Try<Requested>> updateDbFromRequest = (requested) -> {
        return Try.of(() -> requested);
    };

    private static Function1<Requested, Try<Requested>> sendEmail = (requested) -> {
        return Try.of(() -> requested);
    };

    private static Function1<Void, Try<Requested>> updateCustomerWithErrorHandling = (Void) -> {

        return Try.of(() -> receiveRequest.apply(Void))
            .flatMap(requested -> validateRequest.apply(requested).toTry())
            .map(canonicalizeEmail)
            .flatMap(updateDbFromRequest)
            .flatMap(sendEmail);
    };

    public Future<Try<Requested>> updateCustomerWithFuture() {

        return Future.fromTry(
            Try.of(() -> receiveRequest.apply(null))
                .flatMap(requested -> validateRequest.apply(requested).toTry())
                .map(canonicalizeEmail)
                .flatMap(updateDbFromRequest)
        ).map(sendEmail);
    };

    public Try<Requested> updateCustomerWithErrorHandling() {

        return updateCustomerWithErrorHandling
            .apply(null);
    };
}
