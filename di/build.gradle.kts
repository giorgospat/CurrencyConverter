plugins {
    id(BuildPlugins.androidLibrary)
    kotlin(BuildPlugins.android)
    kotlin(BuildPlugins.kapt)
}

android {
    compileSdk = SdkVersions.compileSdk

    defaultConfig {
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    flavorDimensions.add("environment")
    productFlavors {
        create("flavorTest") {
            buildConfigField("boolean", "SERVICE_CONFIG_LIVE", "false")
            dimension = "environment"
        }
        create("flavorLive") {
            buildConfigField("boolean", "SERVICE_CONFIG_LIVE", "true")
            dimension = "environment"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isJniDebuggable = false
            isMinifyEnabled = true
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
    implementation(project(":network"))
    implementation(project(":data"))
    implementation(project(":domain"))

    //coroutines
    implementation(Libraries.coroutines)

    //di
    implementation(Libraries.hilt)
    kapt(Libraries.hiltCompiler)

    //network
    implementation(Libraries.retrofit)
    implementation(Libraries.okhttpLogging)
    implementation(Libraries.moshi)

}