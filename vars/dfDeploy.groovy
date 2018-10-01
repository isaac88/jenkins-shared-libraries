def call(String project, String serviceName, String docsServiceName) {
    sh "docker service update --image isaac88/${project}:${currentBuild.displayName} ${serviceName}"
    script {
        if (docsServiceName != "") {
            sh "docker service update --image isaac88/${project}-docs:${currentBuild.displayName} ${docsServiceName}"
        }
    }
}