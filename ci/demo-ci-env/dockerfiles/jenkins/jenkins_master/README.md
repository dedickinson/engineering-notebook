# Jenkins 2 container

    docker run -d -p 8080:8080 -v $HOME/var/jenkins:/var/jenkins_home --name jenkins-master demo_ci/jenkins

To check the log (and see the access code):

    docker logs -f jenkins-master
