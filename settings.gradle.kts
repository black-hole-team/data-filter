rootProject.name = "black-hole-filter"

pluginManagement {
    plugins {
        id("io.spring.dependency-management") version "1.1.5"
        id("org.springframework.boot") version "3.3.1"
        id("io.freefair.lombok") version "8.6"
        id("io.codearte.nexus-staging") version "0.30.0"
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // Вызываем ошибку при установке репозиториев в самом проекте
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Список репозиториев
    repositories {
        mavenCentral()
    }
}

// Проекты и подпроекты
include(":backend", ":spring")