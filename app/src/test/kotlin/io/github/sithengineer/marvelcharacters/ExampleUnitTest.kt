package io.github.sithengineer.marvelcharacters

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
  private val x = 2
  private val y = 3

  @Test
  fun addition_isCorrect() {
    assertEquals(5, x + y)
    assertEquals(5, y + x)
  }

  @Test
  fun substraction_isCorrect() {
    assertEquals(-1, x - y)
    assertEquals(1, y - x)
  }
}
