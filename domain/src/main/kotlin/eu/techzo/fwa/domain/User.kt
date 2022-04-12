package eu.techzo.fwa.domain

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.ValidatedNel
import arrow.core.computations.either
import eu.techzo.fwa.arrow.zipMany
import eu.techzo.fwa.config.PasswordEncoder
import eu.techzo.fwa.domain.Email.Companion.validate
import eu.techzo.fwa.domain.Password.Companion.validate
import eu.techzo.fwa.domain.Username.Companion.validate

data class User(
  val id: UserId,
  val username: Username,
  val email: Email,
  val password: Password
)

// @formatter:off
fun interface QueryUserByUsername { suspend fun query(username: Username): Either<Failure, User?> }
fun interface QueryUserByEmail { suspend fun query(email: Email): Either<Failure, User?> }
fun interface StoreUser { suspend fun store(user: User): Either<Failure, UserId> }
// @formatter:on

context(QueryUserByUsername)
internal suspend fun exists(username: Username): ValidatedNel<Failure, Username> =
  either<Failure, Username> {
    val user = query(username).bind()
    ensure(user == null) { UsernameAlreadyInUseFailure(username) }
    username
  }.toValidatedNel()

context(QueryUserByEmail)
internal suspend fun exists(email: Email): ValidatedNel<Failure, Email> =
  either<Failure, Email> {
    val user = query(email).bind()
    ensure(user == null) { EmailAlreadyInUseFailure(email) }
    email
  }.toValidatedNel()

context(QueryUserByUsername, QueryUserByEmail, StoreUser, PasswordEncoder)
suspend fun register(
  notValidatedUsername: Username,
  notValidatedEmail: Email,
  notValidatedPassword: Password
): Either<NonEmptyList<Failure>, UserId> =
  either {
    val (username, email, password) = zipMany(
      notValidatedUsername.validate(),
      notValidatedEmail.validate(),
      notValidatedPassword.validate()
    ).bind()

    zipMany(exists(username), exists(email)).bind()

    val encoded = encode(password).bind()

    store(User(UserId(), username, email, encoded)).toValidatedNel().bind()
  }
