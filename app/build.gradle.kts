plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version "2.1.0"
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.urbanmessenger"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.urbanmessenger"
        minSdk = 28
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
    buildFeatures {
        viewBinding = true
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


    // AndroidX Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // MVVM
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    // RecyclerView
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)

    // Hilt
    implementation(libs.hilt.android.v244)
    implementation(libs.firebase.database)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.messaging)
    ksp(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Gson
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Glide (for images)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Lottie (for animations)
    implementation("com.airbnb.android:lottie:6.4.0")

    // TextAutoSize
    implementation("me.grantland:autofittextview:0.2.1")

    // Parcelize
    implementation(libs.kotlin.stdlib.jdk7)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Default dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //adding circle image view
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Firebase
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("com.google.firebase:firebase-bom:33.6.0")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-storage")

    //SupaBase
    implementation("io.github.jan-tennert.supabase:realtime-kt:3.0.3")
    implementation("io.github.jan-tennert.supabase:storage-kt:3.0.3")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.0.3")
    implementation("io.github.jan-tennert.supabase:serializer-jackson:3.0.3")
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.3"))
    implementation("io.ktor:ktor-client-cio:3.0.2")


}