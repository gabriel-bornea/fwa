package eu.techzo.fwa.user

import eu.techzo.fwa.domain.Email
import io.kotest.assertions.arrow.core.shouldBeInvalid
import io.kotest.assertions.arrow.core.shouldBeValid
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData

class EmailSpec : FunSpec({

  context("Should not create an email") {
    withData(
      nameFn = { "Should not create an email with value <$it>" },
      "",
      "user@gmail",
      "user.gmail.com",
      "john doe@gmail",
      "johndoe.@gmail.com",
      "john@yahoo.com.",
      "@yahoo.com"
    ) { value ->
      Email.from(value).shouldBeInvalid()
    }
  }

  context("Should create an email") {
    withData(
      nameFn = { "Should create email with value <$it>" },
      "john@gmail.com",
      "john.doe@yahoo.com",
      "john@techzo.eu"
    ) { value ->
      Email.from(value).shouldBeValid()
    }
  }
})
