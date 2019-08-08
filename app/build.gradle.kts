import org.gradle.kotlin.dsl.debugImplementation

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.android.extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.1")
    defaultConfig {
        applicationId = "fargo.appsample"
        minSdkVersion(19)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    androidExtensions {
        isExperimental = true
    }
}

dependencies {
    // Kotlin:
    implementation(Lib.kotlinStd)
    implementation(Lib.kotlinCoroutines)

    // Android:
    implementation(Lib.appcompat)
    implementation(Lib.material)
    implementation(Lib.recyclerView)
    implementation(Lib.lifecycle)
    implementation(Lib.lifecycleKtx)

    // Logging:
    implementation(Lib.timber)

    // View DSL:
    implementation(Lib.viewDsl)
    implementation(Lib.viewDslMaterial)
    implementation(Lib.viewDslRecyclerView)
    implementation(Lib.appCtx)
    debugImplementation(DevLib.viewDslIDEPreview)

    // Navigation:
    implementation(Lib.cicerone)

    // DI:
    implementation(Lib.toothpick)
    kapt(Lib.toothpickCompiler)

    // Network:
    implementation(Lib.okHttp)
    implementation(Lib.okHttpLogging)
    implementation(Lib.glide)
    implementation(Lib.retrofit)
    implementation(Lib.moshi)
    implementation(Lib.moshiRetrofit)

    // LeakCanary:
    debugImplementation(DevLib.leakCanary)

    // DebugDrawer:
    debugImplementation(Lib.debugDrawer)
    releaseImplementation(Lib.debugDrawerNoOp)
    implementation(Lib.debugDrawerBase)
    implementation(Lib.debugDrawerCommons)
    implementation(Lib.debugDrawerNetworkQuality)

    // Testing:
    testImplementation(Lib.junit)
    androidTestImplementation(Lib.androidTestCore)
    androidTestImplementation(Lib.androidTestRunner)
    androidTestImplementation(Lib.androidTestJunit)
    androidTestImplementation(Lib.espresso)
    androidTestImplementation(Lib.espressoIdlingRes)
    androidTestImplementation(Lib.okHttpIdlingRes)
}
