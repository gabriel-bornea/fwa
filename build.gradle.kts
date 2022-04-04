import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.20"
}

allprojects {
  group = "eu.techzo.fwa"
  version = "0.0.1"

  apply {
    plugin("org.jetbrains.kotlin.jvm")
  }

  repositories {
    mavenCentral()
  }

  dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
  }

  tasks {
    withType<KotlinCompile> {
      kotlinOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
        jvmTarget = "17"
        apiVersion = "1.6"
        languageVersion = "1.6"
      }
    }

    withType<Test> {
      useJUnitPlatform()
    }
  }
}
