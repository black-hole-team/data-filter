import org.jreleaser.model.Active

plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.lombok")
    id("org.jreleaser")
}

group = "team.black-hole.data"
description = "Библиотека позволяет создавать сложные критерии фильтрации, сортировки и пагинации данных через единый интерфейс. Включает лексический анализатор и парсер для обработки строковых выражений фильтрации"
version = "0.0.4"

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
            pluginManager.apply("org.jreleaser")

            java {
                withJavadocJar()
                withSourcesJar()
            }

            tasks {
                javadoc {
                    (options as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
                }
            }

            /** Директория хранения актифактов для публикации */
            val stagingDirectory = layout.buildDirectory.dir("staging-deploy");

            publishing {
                publications {
                    create<MavenPublication>("mavenJava") {
                        artifactId = project.name

                        groupId = project.group.toString()
                        version = project.version.toString()

                        from(components["java"])

                        pom {
                            packaging = "jar"

                            url.set("https://github.com/black-hole-team/data-filter")

                            name.set(project.ext.get("publish_name").toString())
                            description.set(project.ext.get("publish_description").toString())

                            licenses {
                                license {
                                    name.set("The Apache License, Version 2.0")
                                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                }
                            }

                            scm {
                                connection.set("scm:https://github.com/black-hole-team/data-filter.git")
                                developerConnection.set("scm:git@github.com:black-hole-team/data-filter.git")
                                url.set("https://github.com/black-hole-team/data-filter")
                            }

                            developers {
                                developer {
                                    id.set("AseWhy")
                                    name.set("Aleksey Plekhanov")
                                    email.set("astecoms@gmail.com")
                                }
                            }
                        }
                    }
                }
                repositories {
                    maven {
                        setUrl(stagingDirectory)
                    }
                }
            }

            jreleaser {
                gitRootSearch = true

                project {
                    inceptionYear = "2025"
                    author("@AseWhy")
                }

                release {
                    github {
                        draft = true
                        skipRelease = true
                        skipTag = true
                        sign = true
                        branch = "main"
                        branchPush = "main"
                        overwrite = true
                    }
                }

                signing {
                    active = Active.ALWAYS
                    armored = true
                    verify = true
                }

                deploy {
                    maven {
                        mavenCentral {
                            create("sonatype") {
                                active = Active.ALWAYS
                                url = "https://central.sonatype.com/api/v1/publisher"
                                stagingRepository(stagingDirectory.get().toString())
                                setAuthorization("Basic")
                                retryDelay = 60
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
