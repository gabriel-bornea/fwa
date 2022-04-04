package eu.techzo.fwa.domain

sealed class Failure(open val message: String) {
  override fun toString(): String = "Failure(message=$message)"
}

class DatabaseFailure(override val message: String) : Failure(message)

class InvalidUsernameFailure(username: Username) : Failure("Invalid [$username] value.")
class UsernameAlreadyInUseFailure(username: Username) : Failure("[$username] is already in use.")

class InvalidEmailFailure(email: Email) : Failure("Invalid [$email] value.")
class EmailAlreadyInUseFailure(email: Email) : Failure("[$email] is already in use.")

object InvalidPasswordFailure : Failure("Invalid password value.")
