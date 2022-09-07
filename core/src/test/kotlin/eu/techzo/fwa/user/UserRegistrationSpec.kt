package eu.techzo.fwa.user

import arrow.core.Either.Right
import arrow.core.Nel
import arrow.core.continuations.either
import eu.techzo.fwa.config.PasswordEncoder
import eu.techzo.fwa.domain.Email
import eu.techzo.fwa.domain.Failure
import eu.techzo.fwa.domain.Password
import eu.techzo.fwa.domain.QueryUserByEmail
import eu.techzo.fwa.domain.QueryUserByUsername
import eu.techzo.fwa.domain.StoreUser
import eu.techzo.fwa.domain.User
import eu.techzo.fwa.domain.UserId
import eu.techzo.fwa.domain.Username
import eu.techzo.fwa.domain.register
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class UserRegistrationSpec : FreeSpec({

  "should register a user for valid input values" {
    val ctx = RegistrationContext(
      { Right(null) },
      { Right(null) },
      { Right(userId) },
      { Right(it) }
    )

    with(ctx) { register(USERNAME, EMAIL, PASSWORD) }.shouldBeRight(userId)
  }

  "should accumulate errors if input values are not valid" {
    val ctx = RegistrationContext(
      { Right(null) },
      { Right(null) },
      { Right(userId) },
      { Right(it) }
    )

    with(ctx) { register("u1", "@gmail.com", "123") }
      .should { result ->
        result.mapLeft { failures -> failures.size shouldBe 3 }
      }
  }

  "should accumulate errors if username and email are already in use" {
    val ctx = RegistrationContext(
      { Right(user) },
      { Right(user) },
      { Right(userId) },
      { Right(it) }
    )

    with(ctx) { register(USERNAME, EMAIL, PASSWORD) }
      .should { result ->
        result.mapLeft { failures -> failures.size shouldBe 2 }
      }
  }
})

private val userId = UserId()
private const val USERNAME = "username_1"
private const val EMAIL = "username_1@gmail.com"
private const val PASSWORD = "Change1t!"

private val user = either.eager<Nel<Failure>, User> {
  User(userId, Username.from(USERNAME).bind(), Email.from(EMAIL).bind(), Password.from(PASSWORD).bind())
}.orNull()

private class RegistrationContext(
  qubu: QueryUserByUsername,
  qube: QueryUserByEmail,
  su: StoreUser,
  pe: PasswordEncoder
) : QueryUserByUsername by qubu, QueryUserByEmail by qube, StoreUser by su, PasswordEncoder by pe
