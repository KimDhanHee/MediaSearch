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
rootProject.name = "SearchMedia"
include(":app")
include(":core:network")
include(":core:designsystem")
include(":feature:search")
include(":feature:interests")
include(":core:data")
