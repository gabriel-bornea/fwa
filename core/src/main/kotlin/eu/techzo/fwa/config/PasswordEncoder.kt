package eu.techzo.fwa.config

import arrow.core.Either
import arrow.core.NonEmptyList
import eu.techzo.fwa.domain.Failure
import eu.techzo.fwa.domain.Password

fun interface PasswordEncoder {
  suspend fun encode(raw: Password): Either<NonEmptyList<Failure>, Password>
}
