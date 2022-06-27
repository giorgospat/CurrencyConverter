plugins {
    id(BuildPlugins.androidApplication)
    kotlin(BuildPlugins.android)
    kotlin(BuildPlugins.kapt)
    id(BuildPlugins.hilt)
}

android {
    compileSdk = SdkVersions.compileSdk

    defaultConfig {
        applicationId = "com.patronas.currencyconverter"
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "com.patronas.currencyconverter.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
            isShrinkResources = false
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/*"
        }
    }

}

dependencies {

    //core & ui
    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation(Libraries.composeUi)
    implementation(Libraries.composeMaterial)
    implementation(Libraries.composeUiPreview)
    implementation(Libraries.runtimeLifecycleKtx)
    implementation(Libraries.composeActivity)

    //image loading
    implementation(Libraries.coil)

    //logger
    implementation(Libraries.timber)

    //dependency injection
    implementation(Libraries.hilt)
    kapt(Libraries.hiltCompiler)

    //network
    implementation(Libraries.retrofit)
    implementation(Libraries.okhttpLogging)
    implementation(Libraries.moshi)
    kapt(Libraries.moshiCodegen)

    //test libraries
    testImplementation(TestLibraries.junit)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.coroutines)
    testImplementation(TestLibraries.archCore)

}