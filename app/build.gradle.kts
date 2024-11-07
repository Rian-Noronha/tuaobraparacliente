plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.rn.tuaobraparacliente"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rn.tuaobraparacliente"
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
        dataBinding = true
    }
}

val bom_version = "32.8.1"
val auth_version = "22.1.2"
val services_version = "4.3.15"
val app_compat_version = "1.6.1"
val material_version = "1.11.0"
val constraint_layout_version = "2.1.4"
val junit_version = "4.13.2"
val test_junit_version = "1.1.5"
val espresso_core_version = "3.5.1"
val firestore_version = "24.8.1"
val lifecycle_version = "2.5.1"
val storage_version = "20.2.1"
val crashlytics_version = "18.4.3"
val navigation_version = "2.8.1"
val navigation_ui_version = "2.8.1"
val retrofit_version = "2.9.0"
val gson_version = "2.9.0"
val core_ktx_version = "1.13.1"

dependencies {
    implementation("androidx.core:core-ktx:$core_ktx_version")
    implementation("androidx.appcompat:appcompat:$app_compat_version")
    implementation("com.google.android.material:material:$material_version")
    implementation("androidx.constraintlayout:constraintlayout:$constraint_layout_version")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_ui_version")
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$gson_version")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.8.3")

    implementation(platform("com.google.firebase:firebase-bom:$bom_version"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx:$auth_version")
    implementation("com.google.firebase:firebase-firestore-ktx:$firestore_version")
    implementation("com.google.firebase:firebase-storage-ktx:$storage_version")
    implementation("com.google.firebase:firebase-crashlytics-ktx:$crashlytics_version")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    testImplementation("junit:junit:$junit_version")
    androidTestImplementation("androidx.test.ext:junit:$test_junit_version")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso_core_version")
}
