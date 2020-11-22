import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "2.0.2"
}



group = "dev.subside"
version = "1.0"

repositories {
    mavenCentral()
}


dependencies {
    val retrofitVersion = "2.9.0"

    implementation("org.slf4j:slf4j-simple:1.7.21")

    testImplementation(kotlin("test-junit"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.google.code.gson:gson:2.8.6")

    implementation(platform("software.amazon.awssdk:bom:2.15.32"))
    implementation("software.amazon.awssdk:dynamodb")

    implementation("org.twitter4j:twitter4j-core:4.0.7")
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "dev.subside.exchange.MainKt"
    }
}
