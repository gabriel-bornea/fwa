package eu.techzo.fwa.domain


sealed interface Failure

sealed interface InternalFailure : Failure {
  data class DatabaseFailure(val message: String) : InternalFailure
}

sealed interface UserFailure : Failure {
  data class InvalidUsernameFailure(val username: String?) : UserFailure
  data class UsernameAlreadyInUseFailure(val username: Username) : UserFailure
  data class InvalidEmailFailure(val email: String?) : UserFailure
  data class EmailAlreadyInUseFailure(val email: Email) : UserFailure
}

sealed interface SecurityFailure : Failure {
  data class InvalidPasswordFailure(val message: String) : SecurityFailure
}
