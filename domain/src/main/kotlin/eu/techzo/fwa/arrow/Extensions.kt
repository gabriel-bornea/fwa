package eu.techzo.fwa.arrow

import arrow.core.ValidatedNel
import arrow.core.zip
import arrow.typeclasses.Semigroup

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
