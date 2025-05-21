group = parent?.group!!
version = parent?.version!!

ext {
    set("publish_name", "black-hole-data-filter-backend")
    set("publish_description", "Бэкенд фильтрации для Black Hole Filter")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:${property("junitVersion")}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-api:${property("log4jVersion")}")
    implementation("org.apache.logging.log4j:log4j-core:${property("log4jVersion")}")
    implementation("org.apache.commons:commons-text:${property("commonsTextVersion")}")
}

tasks {
    test {
        useJUnitPlatform()
    }
}