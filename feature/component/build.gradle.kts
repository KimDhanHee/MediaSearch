plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.danytothemoon.feature.component"
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
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  implementation(libs.bundles.androidx.compose)
  implementation(libs.glide)
}