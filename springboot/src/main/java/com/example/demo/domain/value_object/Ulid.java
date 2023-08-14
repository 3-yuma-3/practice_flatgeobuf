package com.example.demo.domain.value_object;

import de.huxhorn.sulky.ulid.ULID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Ulid {
    private String value;

    public Ulid generate() {
        ULID ulid = new ULID();
        return new Ulid(ulid.nextULID());
    }

    public static Ulid fromString(String stringUlid) {
        return new Ulid(stringUlid);
    }
}
