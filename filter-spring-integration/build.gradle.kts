plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

ext {
    set("publish_name", "black-hole-data-filter-spring-integration")
    set("publish_description", "Интеграция фильтра для spring boot включающая реализации для mongodb, jpa и включение автоматического парсинга фильтра в запросах")
}

group = parent?.group!!
version = parent?.version!!

dependencies {
    testImplementation(platform("org.junit:junit-bom:${property("junitVersion")}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:${property("log4jVersion")}")
    implementation("org.apache.commons:commons-text:${property("commonsTextVersion")}")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework:spring-beans")
    implementation("jakarta.servlet:jakarta.servlet-api")
    implementation(project(":filter-backend"))
    compileOnly("org.springframework.boot:spring-boot-starter-data-mongodb")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks {
    test {
        useJUnitPlatform()
    }

    bootJar {
        enabled = false
    }

    jar {
        enabled = true
        archiveClassifier = ""
    }
}
