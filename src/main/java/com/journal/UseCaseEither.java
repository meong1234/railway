package com.journal;

import javaslang.Function1;
import javaslang.collection.List;
import javaslang.control.Either;
import javaslang.control.Try;
import javaslang.control.Validation;

import java.util.function.Supplier;

/**
 * Created by jurnal on 11/26/16.
 */
public class UseCaseEither {

    private static Validation<List<String>, Requested> requestExist(Requested requested) {
        return (requested != null)
            ? Validation.valid(requested)
            : Validation.invalid(List.of("request is null"));
    }

    private static Validation<String, Requested> validateName(Requested requested) {
        return !requested.getName().isEmpty()
            ? Validation.valid(requested)
            : Validation.invalid("Name is empty");
    }

    private static Validation<String, Requested> nameLength50(Requested requested) {
        return requested.getName().length() <= 50
            ? Validation.valid(requested)
            : Validation.invalid("name length must >= 50 characters");
    }

    private static Validation<String, Requested> emailNotBlank(Requested requested) {
        return !requested.getEmail().isEmpty()
            ? Validation.valid(requested)
            : Validation.invalid("email is empty");
    }

    private static Function1<Requested, Either> validateRequest = (requested) -> {
        return requestExist(requested)
            .flatMap(request ->
                Validation.combine(
                    validateName(request),
                    nameLength50(request),
                    emailNotBlank(request)
                ).ap((a, b, c) -> request)
            )
            .toEither();
    };

    private static Function1<Requested, Requested> canonicalizeEmail = (requested) -> {
        return requested.withEmail("canonicalizeEmail");
    };

    public static void throwError() {
        throw new RuntimeException("test error");
    }


    private static Function1<Requested, Either<Throwable, Requested>> updateDbFromRequest = (requested) -> {
        return Try.of(() -> requested).toEither();
    };

    private static Function1<Requested, Either<Throwable, Requested>> sendEmail = (requested) -> {
        return Try.of(() -> {
            throwError();

            return requested;
        }).toEither();
    };

    @SuppressWarnings("unchecked")
    public static Either updateCustomerWithErrorHandling(Supplier<Either> requester) {

        return requester.get()
            .flatMap(validateRequest)
            .map(canonicalizeEmail)
            .flatMap(updateDbFromRequest)
            .flatMap(sendEmail);
    }
}
