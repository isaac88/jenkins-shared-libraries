def call(project, domain, extraValues = "") {
    chartName = "${project}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}"
    tagBeta = "${currentBuild.displayName}-${env.BRANCH_NAME}"
    addr = "${project}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}.${domain}"

    sh """helm repo add \
        chartmuseum http://chartmuseum-chartmuseum:8080"""

    sh """helm repo update"""

    sh """helm upgrade \
        ${chartName.toLowerCase()} \
        chartmuseum/${project} -i \
        --namespace default \
        --set image.tag=${tagBeta} \
        --set ingress.host=${addr.toLowerCase()} \
        ${extraValues}"""
}