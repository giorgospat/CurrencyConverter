plugins {
    id(BuildPlugins.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    implementation(project(":network"))
    implementation(Libraries.retrofit)

    //test libraries
    testImplementation(TestLibraries.junit)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.coroutines)

}