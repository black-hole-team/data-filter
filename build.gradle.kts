plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.lombok")
}

group = "black.hole.filter"
version = "0.0.7"

ext {
    set("junitVersion", "5.10.0")
    set("log4jVersion", "2.23.1")
    set("commonsTextVersion", "1.12.0");
}

subprojects {
    pluginManager.apply("java-library")
    pluginManager.apply("io.freefair.lombok")

    afterEvaluate {
        if (project.ext.has("publish_name") || project.ext.has("publish_description")) {
            pluginManager.apply("maven-publish")

            java {
                withJavadocJar()
                withSourcesJar()
            }

            tasks {
                javadoc {
                    (options as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
                }
            }

            publishing {
                repositories {
                    maven {
                        url = uri("https://nexus.black-hole.team/repository/maven-releases/")

                        credentials {
                            username = project.properties["bhmNexusUsername"].toString()
                            password = project.properties["bhmNexusPassword"].toString()
                        }
                    }
                }

                publications {
                    create<MavenPublication>("mavenJava") {
                        artifactId = project.name

                        groupId = project.group.toString()
                        version = project.version.toString()

                        from(components["java"])

                        pom {
                            packaging = "jar"

                            url.set("https://gitlab.com/black-hole-team/black-hole-market/black-hole-filter")

                            name.set(project.ext.get("publish_name").toString())
                            description.set(project.ext.get("publish_description").toString())

                            licenses {
                                license {
                                    name.set("The Apache License, Version 2.0")
                                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                }
                            }

                            scm {
                                connection.set("scm:https://gitlab.com/black-hole-team/black-hole-market/black-hole-filter.git")
                                developerConnection.set("scm:git@gitlab.com:black-hole-team/black-hole-market/black-hole-filter.git")
                                url.set("https://gitlab.com/black-hole-team/black-hole-market/black-hole-filter")
                            }

                            developers {
                                developer {
                                    id.set("astecom")
                                    name.set("Aleksey Plekhanov")
                                    email.set("astecoms@gmail.com")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}