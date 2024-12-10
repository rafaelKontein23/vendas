plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.google.services)

}

android {
    namespace = "br.com.visaogrupo.tudofarmarep"
    compileSdk = 34
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
    defaultConfig {
        applicationId = "br.com.visaogrupo.tudofarmarep"
        minSdk = 26
        targetSdk = 34
        versionCode = 60
        versionName = "3.0.38"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.maskededittext)

    //jetPack
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    //retrofit(Api)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.play.services.location)

    // chucker
    debugImplementation(libs.chuckerDebug)
    releaseImplementation(libs.chuckerRelease)

    // camera
    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)
    implementation(libs.camerax.extensions)
    // biometria
    implementation(libs.androidx.biometric)

    // imagens
    implementation(libs.glide)
    kapt(libs.glideCompiler)   // Compiler do Glide com kapt
    implementation(libs.touchimageview)


    //excel
    implementation(libs.poi)
    implementation(libs.poi.ooxml)

    //grafico
    implementation(libs.mpandroidchart)

    //imagens
    implementation(libs.picasso)
    implementation(libs.photoview)
    implementation(libs.blurry)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
