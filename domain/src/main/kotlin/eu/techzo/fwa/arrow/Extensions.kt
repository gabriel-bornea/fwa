package eu.techzo.fwa.arrow

import arrow.core.Either
import arrow.core.ValidatedNel
import arrow.core.flatMap
import arrow.core.rightIfNull
import arrow.core.zip
import arrow.typeclasses.Semigroup

inline fun <E, B> Either<E, B?>.leftIfNotNull(f: () -> E): Either<E, Nothing?> =
  flatMap { it.rightIfNull { f() } }

fun <E, A, B> zipMany(
  f: ValidatedNel<E, A>,
  g: ValidatedNel<E, B>
): ValidatedNel<E, Pair<A, B>> =
  f.zip(Semigroup.nonEmptyList(), g) { a, b -> Pair(a, b) }

fun <E, A, B, C> zipMany(
  f: ValidatedNel<E, A>,
  g: ValidatedNel<E, B>,
  h: ValidatedNel<E, C>
): ValidatedNel<E, Triple<A, B, C>> =
  f.zip(Semigroup.nonEmptyList(), g, h) { a, b, c -> Triple(a, b, c) }
