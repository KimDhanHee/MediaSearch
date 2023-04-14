plugins {
  kotlin("kapt")
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.danytothemoon.feature.search"
  compileSdk = 33

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
  }
}

dependencies {
  implementation(project(":core:data"))
  implementation(project(":core:designsystem"))

  implementation(project(":feature:component"))

  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)
  implementation(libs.bundles.androidx.compose)
  implementation(libs.androidx.compose.viewmodel)
  implementation(libs.hilt.android)
  implementation(libs.hilt.compose.navigation)
  kapt(libs.hilt.compiler)
  implementation(libs.kotlinx.datetime)
}