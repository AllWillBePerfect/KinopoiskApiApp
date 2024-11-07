import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hiltAndroid)
}

android {
    namespace = "com.example.kinopoiskapiapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kinopoiskapiapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    signingConfigs {
        create("release") {
            val keystoreProp = Properties()
            val keystorePropFile = file("keystore/keystoreConfig.properties")

            keystorePropFile.inputStream().use { keystoreProp.load(it) }
            storeFile = file(keystoreProp["storeFile"] as String)
            storePassword = keystoreProp["storePassword"] as String
            keyAlias = keystoreProp["keyAlias"] as String
            keyPassword = keystoreProp["keyPassword"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.4")
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.keyboardvisibilityevent)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.preference.ktx)


    implementation(libs.rxjava2)
    implementation(libs.rxkotlin2)
    implementation(libs.rxandroid)

    implementation(libs.rxbinding)
//    implementation (libs.rxbinding.kotlin)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.rxjava2)
    implementation(libs.androidx.hilt.work)
    annotationProcessor(libs.androidx.hilt.compiler)

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.rxjava2)

    implementation(libs.gson)


    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.shimmer)

    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation("net.zetetic:android-database-sqlcipher:4.5.0")


    testImplementation(libs.mockito.core)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.rxjava2)
    testImplementation(libs.rxkotlin2)
    testImplementation(libs.rxandroid)
    testImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation(libs.junit)

//    testImplementation("androidx.test.ext:junit-ktx:1.1.4")
//    implementation("androidx.test.ext:junit-ktx:1.1.4")
//    testImplementation("androidx.test:runner:1.4.0")
//    implementation("androidx.test:runner:1.4.0")



}