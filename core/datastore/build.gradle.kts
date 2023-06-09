plugins {
  kotlin("kapt")
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("kotlinx-serialization")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.danytothemoon.core.datastore"
  compileSdk = 33

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(libs.kotlinx.datetime)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}