rootProject.name = "data-filter"

pluginManagement {
    plugins {
        id("io.spring.dependency-management") version "1.1.5"
        id("org.springframework.boot") version "3.3.1"
        id("io.freefair.lombok") version "8.6"
        id("org.jreleaser") version "1.18.0"
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // Вызываем ошибку при установке репозиториев в самом проекте
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Список репозиториев
    repositories {
        // Центральная репа
        mavenCentral()
        // Локальная репа
        mavenLocal()
    }
}

// Проекты и подпроекты
include(":filter-backend", ":filter-spring-integration")
