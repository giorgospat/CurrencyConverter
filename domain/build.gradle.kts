plugins {
    id(BuildPlugins.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    implementation(project(":network"))
    implementation(project(":data"))

    implementation(Libraries.coroutines)

    //test libraries
    testImplementation(TestLibraries.junit)
    testImplementation(TestLibraries.mockitoKotlin)
}