plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.uah.petfeedstation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.uah.petfeedstation"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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

    buildFeatures {
        viewBinding = true
    }

        packaging {
            resources {
                excludes.add("META-INF/INDEX.LIST")
                excludes.add("META-INF/*.SF")
                excludes.add("META-INF/*.DSA")
                excludes.add("META-INF/*.RSA")
                excludes.add("META-INF/io.netty.versions.properties")

            }
        }


}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.mpandroidchart)
    implementation(libs.org.eclipse.paho.client.mqttv3)
    implementation(libs.org.eclipse.paho.android.service)
    implementation(libs.localbroadcastmanager)
    implementation (libs.hivemq.mqtt.client)
}