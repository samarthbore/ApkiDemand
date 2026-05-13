pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ApkiDemand"

include(":app")
include(":core:network")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:security")
include(":core:database")

include(":feature:auth")
include(":feature:home")
include(":feature:post-demand")
include(":feature:offers")
include(":feature:post-offer")
include(":feature:chat")
include(":feature:profile")
include(":feature:search")
include(":feature:notifications")
