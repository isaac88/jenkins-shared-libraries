def call(project, domain, extraValues = "") {
    def usernameLocal, passwordLocal
    withCredentials([usernamePassword(credentialsId: "chartmuseum", usernameVariable: "USER", passwordVariable: "PASS")]) {
        chartName = "${project}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}"
        tagBeta = "${currentBuild.displayName}-${env.BRANCH_NAME}"
        addr = "${project}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}.${domain}"

        usernameLocal = env.USER
        passwordLocal = env.PASS

        sh """helm version"""

        sh """helm repo add \
        chartmuseum http://${USER}:${PASS}@chartmuseum-chartmuseum.default.svc:8080"""

        sh """helm repo update"""

        sh """helm upgrade \
        ${chartName.toLowerCase()} \
        chartmuseum/${project} -i \
        --namespace ${project}-build \
        --set image.tag=${tagBeta} \
        --set ingress.host=${addr.toLowerCase()} \
        ${extraValues}"""
    }
    echo "echo step (out of block) - vars: ${usernameLocal} - ${passwordLocal}"
}