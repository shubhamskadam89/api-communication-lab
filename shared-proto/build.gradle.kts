plugins {
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(libs.protobuf.java)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    implementation(libs.tomcat.annotations.api)
}
