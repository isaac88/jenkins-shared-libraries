def call(String project, gox = false) {
    dockerLogin()
    sh "docker image push isaac88/${project}:latest"
    sh "docker image push isaac88/${project}:${currentBuild.displayName}"
    sh "docker image push isaac88/${project}-docs:latest"
    sh "docker image push isaac88/${project}-docs:${currentBuild.displayName}"
    dockerLogout()
}
