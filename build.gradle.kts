plugins {
    id("java")
}

group = "dev.springochyo"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
//  Project dependencies
    implementation("com.google.code.gson:gson:2.14.0")
    implementation("net.objecthunter:exp4j:0.4.8")

//  Tests
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}