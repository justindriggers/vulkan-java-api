package com.justindriggers.vulkan.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Maskable {

    int getBitValue();

    static <E extends Enum<E> & Maskable> E fromBit(final int bit, final Class<E> enumType) {
        Objects.requireNonNull(enumType);

        return Arrays.stream(enumType.getEnumConstants())
                .filter(enumValue -> enumValue.getBitValue() == bit)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unrecognized bit value: " + bit));
    }

    static <E extends Enum<E> & Maskable> int toBit(final E enumValue) {
        return Optional.ofNullable(enumValue)
                .map(Maskable.class::cast)
                .map(Maskable::getBitValue)
                .orElse(0);
    }

    static <E extends Enum<E> & Maskable> Set<E> fromBitMask(final int bitmask, final Class<E> enumType) {
        Objects.requireNonNull(enumType);

        return Arrays.stream(enumType.getEnumConstants())
                .filter(enumValue -> (enumValue.getBitValue() & bitmask) != 0)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(enumType)));
    }

    @SafeVarargs
    static <E extends Enum<E> & Maskable> int toBitMask(final E... enumValues) {
        final Stream<E> enumStream = Optional.ofNullable(enumValues)
                .map(Arrays::stream)
                .orElseGet(Stream::empty);

        return toBitMask(enumStream);
    }

    static <E extends Enum<E> & Maskable> int toBitMask(final Set<E> enumSet) {
        final Stream<E> enumStream = Optional.ofNullable(enumSet)
                .orElseGet(Collections::emptySet)
                .stream();

        return toBitMask(enumStream);
    }

    static <E extends Enum<E> & Maskable> int toBitMask(final Stream<E> enumStream) {
        return Optional.ofNullable(enumStream)
                .orElseGet(Stream::empty)
                .map(Maskable.class::cast)
                .mapToInt(Maskable::getBitValue)
                .reduce((a, b) -> a | b)
                .orElse(0);
    }
}
