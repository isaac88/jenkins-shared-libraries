def call(String project) {
    withCredentials([usernamePassword(
        credentialsId: "github-token-2",
        usernameVariable: "GITHUB_USER",
        passwordVariable: "GITHUB_TOKEN"
    )]) {
        script {
            def msg = sh(returnStdout: true, script: "git log --format=%B -1").trim()
            if (msg.contains("[release]")) {
                msg = msg.replace("[release]", "")
                def lines = msg.split("\n")
                def releaseTitle = ""
                def releaseMsg = ""
                for (i = 0; i <lines.length; i++) {
                    if (i == 0) {
                        releaseTitle = lines[i]
                    } else {
                        releaseMsg = lines[i] + "\n"
                    }
                }
                sh "git tag -a ${currentBuild.displayName} -m '${releaseMsg}'"
                sh "git push https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/${GITHUB_USER}/${project}.git --tags"
                def cmd = """docker container run --rm \
                    -e GITHUB_TOKEN=${GITHUB_TOKEN} \
                    -v \${PWD}:/src -w /src \
                    isaac88/github-release"""
                sh """${cmd} github-release release --user docker-flow \
                    --repo ${project} --tag ${currentBuild.displayName} \
                    --name '${releaseTitle}' --description '${releaseMsg}'"""
                files = findFiles(glob: "${project}_*")
                for (def file : files) {
                    sh """${cmd} github-release upload \
                        --user docker-flow --repo ${project} \
                        --tag ${currentBuild.displayName} \
                        --name '${file.name}' \
                        --file ${file.name}"""
                }
            }
        }
    }
}
