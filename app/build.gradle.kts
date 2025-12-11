plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //id("kotlin-kapt")
    alias(libs.plugins.navigation.safeargs.kotlin)
}

android {
    namespace = "com.shambhu.myapplication"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.shambhu.myapplication"
        minSdk = 29
        targetSdk = 36
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    implementation(libs.json)
    implementation(libs.gson)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // https://mvnrepository.com/artifact/io.insert-koin/koin-android
    implementation(libs.koin.android)
    // ViewModel and LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.navigation.fragment.ktx.v277)
    implementation(libs.androidx.navigation.ui.ktx.v277)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}