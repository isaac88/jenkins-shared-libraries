def call(project, domain, extraValues = "") {
    withCredentials([usernamePassword(credentialsId: "chartmuseum", usernameVariable: "USER", passwordVariable: "PASS")]) {
        chartName = "${project}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}"
        tagBeta = "${currentBuild.displayName}-${env.BRANCH_NAME}"
        addr = "${project}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}.${domain}"

        sh 'echo $USER'
        sh 'echo $PASS'

        sh """helm repo add \
        --username $USER \
        --password $PASS \
        chartmuseum http://chartmuseum-chartmuseum:8080"""

        sh """helm repo update"""

        sh """helm upgrade \
        ${chartName.toLowerCase()} \
        chartmuseum/${project} -i \
        --namespace ${project}-build \
        --set image.tag=${tagBeta} \
        --set ingress.host=${addr.toLowerCase()} \
        ${extraValues}"""
    }
}