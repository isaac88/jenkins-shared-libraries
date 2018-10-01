def call(String project) {
    sh "docker image build -t isaac88/${project} ."
    sh "docker image build -t isaac88/${project}-test -f Dockerfile.test ."
    sh "docker image build -t isaac88/${project}-docs -f Dockerfile.docs ."
    sh "docker tag isaac88/${project} isaac88/${project}:${currentBuild.displayName}"
    sh "docker tag isaac88/${project}-docs isaac88/${project}-docs:${currentBuild.displayName}"
    sh "docker tag isaac88/${project} isaac88/${project}:beta"
    dockerLogin()
    sh "docker image push isaac88/${project}:beta"
    sh "docker image push isaac88/${project}-test"
    dockerLogout()
}