package com.github.splatte.android

import com.android.builder.BuilderConstants

import org.gradle.api.Plugin
import org.gradle.api.Project

class AppIconOverlayPlugin implements Plugin<Project> {
    private static final String TASK_NAME = "overlayicon"

    void apply(Project project) {
        def log = project.logger

        project.android.applicationVariants.all { variant ->
            /* skip release builds */
            if(variant.buildType.name.equals(BuilderConstants.RELEASE)) {
                log.debug("Skipping build type: ${variant.buildType.name}")
                return;
            }

            /* set up overlay task */
            def overlayTask = project.task(type:OverlayTask, "${TASK_NAME}${variant.buildType.name}") {
                manifestFile = variant.processManifest.manifestOutputFile
                resourcesPath = variant.mergeResources.outputDir
            }

            /* hook overlay task into android build chain */
            variant.processResources.dependsOn overlayTask
        }
    }
}
