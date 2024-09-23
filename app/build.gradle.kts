plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kapt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.example.chit_chat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chit_chat"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
// Main
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.viewbinding)
    implementation(libs.kotlin.stdlib)

// Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.storage)
    implementation(libs.firestore)

// UI Tools
    implementation(libs.glide)
    implementation(libs.gesture.views)
    implementation(libs.androidx.swiperefreshlayout)

// Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

// Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

// DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

// Date Time
    implementation(libs.android.joda)

// Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}