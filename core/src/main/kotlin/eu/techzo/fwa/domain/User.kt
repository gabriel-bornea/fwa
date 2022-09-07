package eu.techzo.fwa.domain

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.Validated
import arrow.core.ValidatedNel
import arrow.core.continuations.either
import eu.techzo.fwa.arrow.zipMany
import eu.techzo.fwa.config.PasswordEncoder

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

internal suspend fun <R> R.exists(
  username: Username
): ValidatedNel<Failure, Username> where R : QueryUserByUsername = either<Failure, Username> {
  val user = query(username).bind()
  ensure(user == null) { UserFailure.UsernameAlreadyInUseFailure(username) }
  username
}.toValidatedNel()

internal suspend fun <R> R.exists(
  email: Email
): ValidatedNel<Failure, Email> where R : QueryUserByEmail = either<Failure, Email> {
  val user = query(email).bind()
  ensure(user == null) { UserFailure.EmailAlreadyInUseFailure(email) }
  email
}.toValidatedNel()

suspend fun <R> R.register(
  rawUsername: String?,
  rawEmail: String?,
  rawPassword: String?
): Either<NonEmptyList<Failure>, UserId> where R : QueryUserByUsername,
                                               R : QueryUserByEmail,
                                               R : StoreUser,
                                               R : PasswordEncoder =
  either {
    val (username, email, password) =
      Validated.zipMany(Username.from(rawUsername), Email.from(rawEmail), Password.from(rawPassword)).bind()
    Validated.zipMany(exists(username), exists(email)).bind()

    val encoded = encode(password).bind()

    store(User(UserId(), username, email, encoded)).toValidatedNel().bind()
  }
