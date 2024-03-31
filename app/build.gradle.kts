plugins {
    id("com.android.application")
}

android {
    namespace = "algonquin.cst2335.finalprojectmobileprogramming"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "algonquin.cst2335.finalprojectmobileprogramming"
        minSdk = 27
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
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.firebase:firebase-firestore:24.11.0")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.annotation:annotation:1.7.1")
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\joesv\\AppData\\Local\\Android\\Sdk\\platforms\\android-34",
        "include" to listOf("*.aar", "*.jar"),
    )))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
}