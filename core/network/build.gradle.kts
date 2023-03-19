plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("kotlinx-serialization")
}

android {
  namespace = "com.danytothemoon.core.network"
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
  implementation(libs.retrofit.core)
  implementation(libs.retrofit.kotlinx.serialization)
  implementation(libs.kotlinx.serialization)
}