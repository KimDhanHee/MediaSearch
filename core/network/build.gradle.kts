import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  kotlin("kapt")
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("kotlinx-serialization")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.danytothemoon.core.network"
  compileSdk = 33

  defaultConfig {
    buildConfigField(
      type = "String",
      name = "KAKAO_API_KEY",
      value = gradleLocalProperties(rootDir).getProperty("kakao_api_key")
    )
    buildConfigField(
      type = "String",
      name = "KAKAO_API_BASE_URL",
      value = gradleLocalProperties(rootDir).getProperty("kakao_api_base_url")
    )
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
  implementation(libs.retrofit.core)
  implementation(libs.retrofit.kotlinx.serialization)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.datetime)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}