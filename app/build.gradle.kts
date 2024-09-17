val implementation: Unit = Unit

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.tp1_emergencias_repo"
    compileSdk = 34

    //lo agrege
    buildFeatures.viewBinding= true

    defaultConfig {
        applicationId = "com.example.tp1_emergencias_repo"
        minSdk = 24
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    androidTestImplementation(libs.androidx.ui.test.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //lo agrege
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    //para camara
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.guava) // Usa la última versión estable

    //para mapa
    //implementation(kotlin("com.google.android.gms:play-services-maps:17.0.0"))
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    //para el chat
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation(kotlin("script-runtime"))



}