package com.justindriggers.vulkan.models;

import java.util.Arrays;
import java.util.Objects;

public interface HasValue<T> {

    T getValue();

    static <T, E extends Enum<E> & HasValue<T>> T getValue(E enumValue) {
        Objects.requireNonNull(enumValue);

        return enumValue.getValue();
    }

    static <T, E extends Enum<E> & HasValue<T>> E getByValue(T value, Class<E> enumType) {
        Objects.requireNonNull(enumType);

        return Arrays.stream(enumType.getEnumConstants())
                .filter(enumValue -> Objects.nonNull(enumValue.getValue()))
                .filter(enumValue -> value.equals(enumValue.getValue()))
                .findFirst()
                .orElse(null);
    }
}
