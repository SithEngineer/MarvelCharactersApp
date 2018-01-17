package io.github.sithengineer.marvelcharacters.util

import java.security.MessageDigest

// code from https://www.samclarke.com/kotlin-hash-strings/
// with minor modifications
/**
 * Hashing Utils
 * @author Sam Clarke <www.samclarke.com>
 * @license MIT
 */
object HashUtils {
  fun md5(input: String) = hashString("MD5", input)

  fun sha1(input: String) = hashString("SHA-1", input)

  fun sha256(input: String) = hashString("SHA-256", input)

  fun sha512(input: String) = hashString("SHA-512", input)

  private val HEX_CHARS = "0123456789abcdef"

  /**
   * Supported algorithms on Android:
   *
   * Algorithm	Supported API Levels
   * MD5          1+
   * SHA-1	      1+
   * SHA-224	    1-8,22+
   * SHA-256	    1+
   * SHA-384	    1+
   * SHA-512	    1+
   */
  private fun hashString(type: String, input: String): String {
    val bytes = MessageDigest
        .getInstance(type)
        .digest(input.toByteArray())

    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
      val i = it.toInt()
      result.append(HEX_CHARS[i shr 4 and 0x0f])
      result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
  }
}