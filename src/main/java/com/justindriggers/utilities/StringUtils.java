package com.justindriggers.utilities;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;

public class StringUtils {

    /**
     * Safely converts the Set of Strings into a PointerBuffer containing an array of pointers to the Strings.
     *
     * @param strings A nullable Set of Strings
     * @return a PointerBuffer containing an array of pointers to all validation layers. The PointerBuffer and the
     * Strings that its contents point to must be disposed of manually.
     */
    public static PointerBuffer getPointerBufferFromStrings(final Set<String> strings) {
        final Set<String> stringsSafe = Optional.ofNullable(strings)
                .orElseGet(Collections::emptySet);

        final PointerBuffer result = memAllocPointer(stringsSafe.size());

        stringsSafe.stream()
                .map(MemoryUtil::memUTF8)
                .forEach(result::put);

        result.flip();

        return result;
    }
}
