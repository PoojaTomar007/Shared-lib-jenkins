package org.paspao.sharedlib

import groovy.transform.PackageScope


class JenkinsBuilder {

    private final def script
    private final String projectName
    private final String branchName
    private String agentName
    private String gradleVersion
    private String jdkVersion

    JenkinsBuilder(def script, String projectName, String branchName) {
        this.script = script
        this.projectName = projectName
        this.branchName = branchName
    }

    JenkinsBuilder withGradleVersion(String gradleVersion) {
        this.gradleVersion = gradleVersion
        return this
    }

    JenkinsBuilder withJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion
        return this
    }


    void execute() {
        pipeline {
            node {
                try {
                    gradle()
                    jdk()
                    if(gradleVersion) {
                        stage('Build Gradle') {
                            ansiColor {

                                GradleBuild()

                            }
                        }
                    }
                } finally {
                    script.chuckNorris()
                }
            }


        }
    }


    @PackageScope
    def checkout() {
        script.checkout(script.scm)
    }
}

    @PackageScope
    def gradle() {
        if (gradleVersion) {
            script.tool(type: 'gradle', name: gradleVersion)
        }
    }

    @PackageScope
    def gradleBuild()
    {
        if (gradleVersion) {
            script.withGradle(
                    // gradle installation declared in the Jenkins "Global Tool Configuration"
                    gradle: mavenVersion, jdk: jdkVersion) {

                // Run the gradle build
                script.sh "gradle build"

            }
        }
    }


    @PackageScope
    def jdk() {
        if (jdkVersion) {
            script.tool(type: 'jdk', name: jdkVersion)
        }
    }


}
