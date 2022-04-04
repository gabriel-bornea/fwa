package eu.techzo.fwa.user

import eu.techzo.fwa.domain.Password
import eu.techzo.fwa.domain.Password.Companion.validate
import io.kotest.assertions.arrow.core.shouldBeInvalid
import io.kotest.assertions.arrow.core.shouldBeValid
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.checkAll

class PasswordSpec : FreeSpec({

  "should successfully create a password" {
    checkAll(Arb.stringPattern("[a-z][A-Z][0-9]{10,80}")) { value ->
      Password(value).validate().shouldBeValid()
    }
  }

  "should not create a password if lowercase chars are missing" {
    checkAll(Arb.stringPattern("[A-Z][0-9]{8,99}")) { value ->
      Password(value).validate().shouldBeInvalid()
    }
  }

  "should not create a password if uppercase chars are missing" {
    checkAll(Arb.stringPattern("[a-z][0-9]{8,99}")) { value ->
      Password(value).validate().shouldBeInvalid()
    }
  }

  "should not create a password if numbers are missing" {
    checkAll(Arb.stringPattern("[A-Z][a-z]{8,99}")) { value ->
      Password(value).validate().shouldBeInvalid()
    }
  }

  "should not create a password if length is not between 5 and 100 chars" {
    checkAll(Arb.stringPattern("[a-z][A-Z][0-9]{101,150}")) { value ->
      Password(value).validate().shouldBeInvalid()
    }
  }
})
