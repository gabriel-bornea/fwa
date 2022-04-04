package eu.techzo.fwa.domain

import arrow.core.ValidatedNel
import arrow.core.invalidNel
import arrow.core.valid
import java.util.UUID
import java.util.regex.Pattern

@JvmInline value class UserId(val value: String = UUID.randomUUID().toString())

@JvmInline value class Username(val value: String) {
  companion object {
    private val pattern = Pattern.compile("^[a-z0-9._-]{6,30}\$")

    fun Username.validate(): ValidatedNel<Failure, Username> =
      if (pattern.matcher(value.lowercase()).matches()) Username(value.lowercase()).valid()
      else InvalidUsernameFailure(this).invalidNel()
  }
}

@JvmInline value class Email(val value: String) {
  companion object {
    private val pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}\$")

    fun Email.validate(): ValidatedNel<Failure, Email> =
      if (pattern.matcher(value).matches()) Email(value.lowercase()).valid()
      else InvalidEmailFailure(this).invalidNel()
  }
}

@JvmInline value class Password(val value: String) {
  companion object {
    private val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,100}\$")

    fun Password.validate(): ValidatedNel<Failure, Password> =
      if (pattern.matcher(value).matches()) Password(value).valid()
      else InvalidPasswordFailure.invalidNel()
  }

  override fun toString(): String = "Password(value=[PROTECTED])"
}
