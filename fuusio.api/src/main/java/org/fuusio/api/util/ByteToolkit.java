package org.fuusio.api.util;

public class ByteToolkit {

    public static byte[] concatenate(final byte[]... pByteArrays) {
        int length = 0;

        for (final byte[] array : pByteArrays) {
            length += array.length;
        }

        final byte[] concatenatedArray = new byte[length];
        int index = 0;

        for (final byte[] array : pByteArrays) {
            System.arraycopy(array, 0, concatenatedArray, index, array.length);
            index += array.length;
        }
        return concatenatedArray;
    }
}
