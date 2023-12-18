plugins {
    id("com.android.application")
}

android {


    namespace = "com.example.myapplication"
    compileSdk = 33


    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.code.gson:gson:2.8.9") // 或者是最新版本

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.qiniu:qiniu-java-sdk:7.2.0")
    implementation("com.qiniu:qiniu-android-sdk:8.6.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0") // 请替换为最新版本
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    /*    implementation(files("libs\\mysql-connector-j-8.2.0.jar"))*/
    /*  implementation(files("libs\\mysql-connector-java-5.1.28-bin.jar"))*/
    /*implementation(files("libs\\mysql-connector-java-5.1.49-bin.jar"))*/
    /* implementation(files("libs\\mysql-connector-java-8.0.11.jar"))*/
//    implementation(files("libs\\mysql-connector-java-6.0.6-bin.jar"))
        implementation(files("libs\\mysql-connector-java-5.1.8-bin.jar"))
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    /*  implementation(files("libs\\mysql-connector-java-8.0.20.jar"))*/
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}