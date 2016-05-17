# Basic CentOS 7 image with Python 3.4
# providing a simple web server

# We base this image on a pinned OS version
FROM centos:7.2.1511

# The MAINTAINER is useful for published images
MAINTAINER duncan@dickinson.name

# Install Python 3.4
RUN yum install -y epel-release
RUN yum install -y python34

# I add a system user (webserver) and a /www directory for static hosting
RUN adduser --system --no-create-home webserver && \
    mkdir /www && \
    chown webserver.webserver /www

# We'll listen on the standard web port (80) and containers can map this
EXPOSE 8000

# We'll now stop running as a privilege user and move to the one we just created
USER webserver

# Setting the working dir and copy in the basic web page
WORKDIR /www
COPY index.html /www/

# Start up the basic python web server
CMD ["python3.4", "-I", "-m", "http.server", "8000"]
