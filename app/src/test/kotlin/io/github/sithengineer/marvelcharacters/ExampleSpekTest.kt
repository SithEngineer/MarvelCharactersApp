package io.github.sithengineer.marvelcharacters

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Assertions.assertEquals

class ExampleSpekTest : Spek({
  val x = 2
  val y = 3

  given("x = $x and y = $y") {

    on("addition"){

      it("should be that x + y = 5") {
        val result = x + y
        assertEquals(5, result)
      }

      it("should be that y + x = 5") {
        val result = x + y
        assertEquals(5, result)
      }
    }

    on("subtraction"){

      it("should be that x - y = -1") {
        val result = x - y
        assertEquals(-1, result)
      }

      it("should be that y - x = 1") {
        val result = y - x
        assertEquals(1, result)
      }
    }

  }
})