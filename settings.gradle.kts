@file:Suppress("UnstableApiUsage")

import java.net.URI


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI("https://android-sdk.is.com/")
        }
        maven {
            url = uri("https://repository.jboss.org/maven2")
        }
    }
}

rootProject.name = "CrawlappsDemo"
include(":app")
