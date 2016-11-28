package com.journal;

import javaslang.collection.List;
import javaslang.control.Either;
import org.junit.Test;

import static com.journal.UseCaseEither.updateCustomerWithErrorHandling;
import static org.junit.Assert.assertEquals;

/**
 * Created by jurnal on 11/26/16.
 */
public class UseCaseEitherTest {

    @Test
    public void updateCustomerWithNullRequest() throws Exception {

        Either<List<String>, Requested> value = updateCustomerWithErrorHandling(
            () -> Either.right(null)
        );

        assertEquals(List.of("request is null"), value.getLeft());
    }

    @Test
    public void updateCustomerWithInvalidRequest() throws Exception {

        Either<List<String>, Requested> value = updateCustomerWithErrorHandling(
            () -> Either.right(new Requested("", ""))
        );

        assertEquals(
            List
                .of(
                    "Name is empty",
                    "email is empty"),
            value.getLeft()
        );
    }

    @Test
    public void testException() throws Exception {

        Either value = updateCustomerWithErrorHandling(
            () -> Either.right(new Requested("ada ", "datanya"))
        );

        assertEquals(RuntimeException.class, value.getLeft().getClass());
    }

}