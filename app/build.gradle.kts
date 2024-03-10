plugins {
    id("com.android.application")
}

android {
    namespace = "com.nassimlnd.flixhub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nassimlnd.flixhub"
        minSdk = 30
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    val media3Version = "1.2.1"
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.media3:media3-datasource:$media3Version")
    implementation("androidx.media3:media3-decoder:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")
    implementation("androidx.media3:media3-container:$media3Version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version") {
        exclude("androidx.media3", "media3-exoplayer")
    }
    implementation("androidx.media3:media3-exoplayer-hls:$media3Version") {
        exclude("androidx.media3", "media3-exoplayer")
    }
    implementation("androidx.media3:media3-exoplayer-smoothstreaming:$media3Version") {
        exclude("androidx.media3", "media3-exoplayer")
    }
    implementation("androidx.media3:media3-exoplayer-rtsp:$media3Version") {
        exclude("androidx.media3", "media3-exoplayer")
    }
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("lib-*.aar"))))
}