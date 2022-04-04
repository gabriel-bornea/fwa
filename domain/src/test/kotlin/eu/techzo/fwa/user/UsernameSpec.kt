package eu.techzo.fwa.user

import eu.techzo.fwa.domain.Username
import eu.techzo.fwa.domain.Username.Companion.validate
import io.kotest.assertions.arrow.core.shouldBeInvalid
import io.kotest.assertions.arrow.core.shouldBeValid
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.checkAll

class UsernameSpec : FreeSpec({

  "should create a valid username" {
    checkAll(Arb.stringPattern("[a-z0-9._-]{6,30}")) { value ->
      Username(value).validate().shouldBeValid()
    }
  }

  "should not create a username if not allowed chars are used" {
    checkAll(Arb.stringPattern("[#/*()]{6,30}")) { value ->
      Username(value).validate().shouldBeInvalid()
    }
  }

  "should not create a username if length is invalid" {
    checkAll(Arb.stringPattern("[a-z0-9._-]{31,100}")) { value ->
      Username(value).validate().shouldBeInvalid()
    }
  }

})
