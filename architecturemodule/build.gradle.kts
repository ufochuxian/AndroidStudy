plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.transsion.architecturemodule"
    compileSdk = 33

    defaultConfig {
        targetSdk = 33
        minSdk = 25

    }

    viewBinding.isEnabled = true
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-viewmodel-android:2.8.7")
    // Fragment Library
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    // Activity Library (for OnBackPressedCallback)
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.code.gson:gson:2.10.1")
}