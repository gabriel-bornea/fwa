plugins {
  kotlin("jvm")
}

dependencies {
  implementation(libs.arrow)
  testImplementation(libs.bundles.kotest)
}
