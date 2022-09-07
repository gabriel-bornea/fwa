package eu.techzo.fwa.arrow

import arrow.core.NonEmptyList
import arrow.core.Validated
import arrow.core.invalidNel
import arrow.core.valid
import io.kotest.assertions.arrow.core.shouldBeInvalid
import io.kotest.assertions.arrow.core.shouldBeValid
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ExtensionsSpec : FreeSpec({

  "zipMany2 should return a Pair if both evaluations are Valid" {
    checkAll(Arb.int(), Arb.int()) { a: Int, b: Int ->
      Validated.zipMany<Throwable, Int, Int>(a.valid(), b.valid()) shouldBeValid Pair(a, b)
    }
  }

  "zipMany2 should return a Nel if one evaluation fails" {
    checkAll(Arb.int(), Arb.int()) { a: Int, b: Int ->
      Validated.zipMany<Int, Int, Int>(a.valid(), b.invalidNel()) shouldBeInvalid NonEmptyList.fromListUnsafe(listOf(b))
    }
  }

  "zipMany2 should return a Nel if both evaluations fail" {
    checkAll(Arb.int(), Arb.int()) { a: Int, b: Int ->
      Validated.zipMany<Int, Int, Int>(a.invalidNel(), b.invalidNel()) shouldBeInvalid NonEmptyList.fromListUnsafe(listOf(a, b))
    }
  }

  "zipMany3 should return a Triple if all evaluations are Valid" {
    checkAll(Arb.int(), Arb.int(), Arb.int()) { a: Int, b: Int, c: Int ->
      Validated.zipMany<Throwable, Int, Int, Int>(a.valid(), b.valid(), c.valid()) shouldBeValid Triple(a, b, c)
    }
  }

  "zipMany3 should return a Nel if one evaluation fails" {
    checkAll(Arb.int(), Arb.int(), Arb.int()) { a: Int, b: Int, c: Int ->
      Validated.zipMany<Int, Int, Int, Int>(a.valid(), b.invalidNel(), c.valid()) shouldBeInvalid NonEmptyList.fromListUnsafe(listOf(b))
    }
  }

  "zipMany3 should return a Nel if all evaluations fail" {
    checkAll(Arb.int(), Arb.int(), Arb.int()) { a: Int, b: Int, c: Int ->
      Validated.zipMany<Int, Int, Int, Int>(a.invalidNel(), b.invalidNel(), c.invalidNel()) shouldBeInvalid NonEmptyList.fromListUnsafe(listOf(a, b, c))
    }
  }
})
