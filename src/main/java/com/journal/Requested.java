package com.journal;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by jurnal on 11/23/16.
 */
@Value
@ToString
@AllArgsConstructor
public class Requested {

    @Wither
    private String name;

    @Wither
    private String email;
}
