package eu.techzo.fwa.user

import arrow.core.Either.Right
import eu.techzo.fwa.config.PasswordEncoder
import eu.techzo.fwa.domain.Email
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

    with(ctx) { register(username, email, password) }.shouldBeRight(userId)
  }

  "should accumulate errors if input values are not valid" {
    val ctx = RegistrationContext(
      { Right(null) },
      { Right(null) },
      { Right(userId) },
      { Right(it) }
    )

    with(ctx) { register(Username("u1"), Email("@gmail.com"), Password("123")) }
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

    with(ctx) { register(username, email, password) }
      .should { result ->
        result.mapLeft { failures -> failures.size shouldBe 2 }
      }
  }
})

private val userId = UserId()
private val username = Username("username_1")
private val email = Email("username_1@gmail.com")
private val password = Password("Change1t!")

private val user = User(userId, username, email, password)

private class RegistrationContext(
  qubu: QueryUserByUsername,
  qube: QueryUserByEmail,
  su: StoreUser,
  pe: PasswordEncoder
) : QueryUserByUsername by qubu, QueryUserByEmail by qube, StoreUser by su, PasswordEncoder by pe
