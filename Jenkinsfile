node {

    checkout scm

    docker.withRegistry('https://registry.hub.docker.com', 'dockerHub') {

        def customImage = docker.build("milicm/logical_doc")

        /* Push the container to the custom Registry */
        customImage.push()
    }
}

