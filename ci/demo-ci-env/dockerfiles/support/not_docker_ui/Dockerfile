FROM node:5

# Borrowed from https://github.com/docker-library/docker/blob/744110cf7268354ae30928e76f392c7864d1afea/1.10/Dockerfile
ENV DOCKER_BUCKET get.docker.com
ENV DOCKER_VERSION 1.10.3
ENV DOCKER_SHA256 d0df512afa109006a450f41873634951e19ddabf8c7bd419caeb5a526032d86d

RUN curl -fSL "https://${DOCKER_BUCKET}/builds/Linux/x86_64/docker-$DOCKER_VERSION" -o /usr/local/bin/docker \
	&& echo "${DOCKER_SHA256}  /usr/local/bin/docker" | sha256sum -c - \
	&& chmod +x /usr/local/bin/docker

RUN git clone https://github.com/crosbymichael/not-dockers-ui.git /app

RUN cd /app \
    && npm install \
    && npm install -g grunt-cli

COPY build.sh /build.sh

VOLUME ["/var/run/docker.sock"]

WORKDIR /app
#ENTRYPOINT ["/build.sh"]
