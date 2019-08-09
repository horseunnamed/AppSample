object Ver {
    const val kotlin = "1.3.41"
    const val okHttp = "4.0.1"
    const val retrofit = "2.6.0"
    const val moshi = "1.8.0"
    const val splitties = "3.0.0-alpha06"
    const val toothpick = "2.1.0"
    const val debugDrawer = "0.8.0"
}

object Lib {
    // Kotlin:
    const val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Ver.kotlin}"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-M2"

    // Android:
    const val appcompat = "androidx.appcompat:appcompat:1.0.2"
    const val material = "com.google.android.material:material:1.1.0-alpha08"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0-alpha05"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:2.0.0"
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-alpha01"
    const val fragment = "androidx.fragment:fragment:1.2.0-alpha02"
    const val fragmentTesting = "androidx.fragment:fragment-testing:1.2.0-alpha02"

    // Logging:
    const val timber = "com.jakewharton.timber:timber:4.7.1"

    // Splitties view DSL
    const val appCtx = "com.louiscad.splitties:splitties-appctx:${Ver.splitties}"
    const val viewDsl = "com.louiscad.splitties:splitties-views-dsl:${Ver.splitties}"
    const val viewDslMaterial = "com.louiscad.splitties:splitties-views-dsl-material:${Ver.splitties}"
    const val viewDslRecyclerView = "com.louiscad.splitties:splitties-views-dsl-recyclerview:${Ver.splitties}"

    // Navigation:
    const val cicerone = "ru.terrakok.cicerone:cicerone:5.0.0"

    // DI:
    const val toothpick = "com.github.stephanenicolas.toothpick:toothpick-runtime:${Ver.toothpick}"
    const val toothpickCompiler = "com.github.stephanenicolas.toothpick:toothpick-compiler:${Ver.toothpick}"

    // Network:
    const val okHttp = "com.squareup.okhttp3:okhttp:${Ver.okHttp}"
    const val okHttpLogging = "com.squareup.okhttp3:logging-interceptor:${Ver.okHttp}"
    const val glide = "com.github.bumptech.glide:glide:4.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Ver.retrofit}"
    const val moshi = "com.squareup.moshi:moshi:${Ver.moshi}"
    const val moshiRetrofit = "com.squareup.retrofit2:converter-moshi:${Ver.retrofit}"

    // Debug drawer:
    const val debugDrawer = "io.palaima.debugdrawer:debugdrawer:${Ver.debugDrawer}"
    const val debugDrawerNoOp = "io.palaima.debugdrawer:debugdrawer-no-op:${Ver.debugDrawer}"
    const val debugDrawerBase = "io.palaima.debugdrawer:debugdrawer-base:${Ver.debugDrawer}"
    const val debugDrawerCommons = "io.palaima.debugdrawer:debugdrawer-commons:${Ver.debugDrawer}"
    const val debugDrawerNetworkQuality = "io.palaima.debugdrawer:debugdrawer-network-quality:${Ver.debugDrawer}"

    // Testing:
    const val junit = "junit:junit:4.12"
    const val androidTestCore = "androidx.test:core:1.2.0"
    const val androidTestRunner = "androidx.test:runner:1.1.0"
    const val androidTestJunit = "androidx.test.ext:junit:1.1.0"
    const val mockito = "org.mockito:mockito-android:3.0.0"
    const val espresso = "androidx.test.espresso:espresso-core:3.1.0"
    const val espressoIdlingRes = "androidx.test.espresso:espresso-idling-resource:3.2.0"
    const val okHttpIdlingRes = "com.jakewharton.espresso:okhttp3-idling-resource:1.0.0"
}

object DevLib {
    const val viewDslIDEPreview = "com.louiscad.splitties:splitties-views-dsl-ide-preview:${Ver.splitties}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.0-alpha-3"
}
