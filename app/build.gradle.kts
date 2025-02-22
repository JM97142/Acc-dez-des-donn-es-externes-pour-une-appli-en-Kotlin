plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.dagger.hilt.android")
  kotlin("kapt")
}

android {
  namespace = "com.aura"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.aura"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    viewBinding = true
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.8.0")
  implementation("androidx.annotation:annotation:1.6.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
  implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")
  implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")
  implementation("androidx.recyclerview:recyclerview:1.3.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

  // Testing Dependencies
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  testImplementation("androidx.arch.core:core-testing:2.2.0")
  testImplementation("org.mockito:mockito-core:4.+")
  testImplementation("io.mockk:mockk:1.13.16")

  // Dagger Hilt Dependency Injection
  implementation("com.google.dagger:hilt-android:2.50")
  kapt("com.google.dagger:hilt-android-compiler:2.50")

  // Moshi JSON Library
  implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

  // Retrofit for Network Requests
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
  implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

  implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
  implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
  testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
}
kapt {
  correctErrorTypes = true
}