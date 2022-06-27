plugins {
    id(BuildPlugins.kotlin)
    kotlin(BuildPlugins.kapt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    //retrofit client
    implementation(Libraries.retrofit)
    implementation(Libraries.moshiConverter)
    implementation(Libraries.moshi)
    kapt(Libraries.moshiCodegen)
}