
To run the container:

    docker run -ti --rm -p 2222:22 demo_ci/jenkins_agent

Then access:

    ssh -p 2222 jenkins@$(docker-machine ip)
    
## Limitations

The image isn't secure.
