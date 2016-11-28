# railway
Railway Oriented Programming on Java8 + Javaslang

```sh
public static Either updateCustomerWithErrorHandling(Supplier<Either> requester) {

        return requester.get()
            .flatMap(validateRequest)
            .map(canonicalizeEmail)
            .flatMap(updateDbFromRequest)
            .flatMap(sendEmail);
    }
```
