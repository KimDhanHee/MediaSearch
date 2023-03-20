plugins {
  kotlin("kapt")
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.danytothemoon.core.data"
  compileSdk = 33

  defaultConfig {
    minSdk = 26
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(project(":core:network"))

  implementation(libs.kotlinx.datetime)
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}