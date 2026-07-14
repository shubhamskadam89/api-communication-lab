plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
}

dependencies {
    implementation(project(":shared-proto"))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.graphql)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.grpc.client.spring.boot)

    testImplementation(libs.spring.boot.starter.test)
}
