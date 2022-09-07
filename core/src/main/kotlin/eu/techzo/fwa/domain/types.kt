package eu.techzo.fwa.domain

import arrow.core.ValidatedNel
import arrow.core.invalidNel
import arrow.core.valid
import java.util.*
import java.util.regex.Pattern

@JvmInline value class UserId(val value: String = UUID.randomUUID().toString())

@JvmInline value class Username private constructor(private val value: String) {
  companion object {
    private val pattern = Pattern.compile("^[a-z0-9._-]{6,30}\$")

    fun from(value: String?): ValidatedNel<Failure, Username> =
      if (value != null && pattern.matcher(value.lowercase()).matches()) Username(value.lowercase()).valid()
      else UserFailure.InvalidUsernameFailure(value).invalidNel()
  }
}

@JvmInline value class Email private constructor(private val value: String) {
  companion object {
    private val pattern = Pattern
      .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}\$")

    fun from(value: String?): ValidatedNel<Failure, Email> =
      if (value != null && pattern.matcher(value).matches()) Email(value.lowercase()).valid()
      else UserFailure.InvalidEmailFailure(value).invalidNel()
  }
}

@JvmInline value class Password private constructor(private val value: String) {
  companion object {
    private val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,100}\$")

    fun from(value: String?): ValidatedNel<Failure, Password> =
      if (value != null && pattern.matcher(value).matches()) Password(value).valid()
      else SecurityFailure.InvalidPasswordFailure("Invalid password").invalidNel()
  }

  override fun toString(): String = "Password(value=[PROTECTED])"
}
