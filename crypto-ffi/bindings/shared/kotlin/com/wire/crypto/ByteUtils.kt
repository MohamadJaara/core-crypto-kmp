package com.wire.crypto

private val HEX_CHARS = "0123456789abcdef".toCharArray()

internal fun ByteArray.toHex(): String {
    val result = StringBuilder(size * 2)
    for (byte in this) {
        val i = byte.toInt()
        result.append(HEX_CHARS[(i shr 4) and 0x0F])
        result.append(HEX_CHARS[i and 0x0F])
    }
    return result.toString()
}
