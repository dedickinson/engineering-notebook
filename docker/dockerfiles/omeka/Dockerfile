FROM php:5.6-apache

MAINTAINER duncan@dickinson.name

ENV APP_NAME Omeka
ENV APP_VERSION 2.3.1
ENV APP_DIST http://omeka.org/files/omeka-2.3.1.zip

USER root

RUN apt-get update \
    && apt-get -y install imagemagick curl unzip \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

RUN docker-php-ext-install mysqli exif

WORKDIR /var/www/html

# See: http://omeka.org/codex/Install_on_Ubuntu_using_Terminal
RUN mkdir -p /tmp/omeka \
    && curl -o /tmp/omeka.zip $APP_DIST \
    && unzip /tmp/omeka.zip -d /tmp/omeka/ \
    && rm -rf /tmp/omeka.zip \
    && mv /tmp/omeka/`ls /tmp/omeka/`/* /var/www/html \
    && mv /tmp/omeka/`ls /tmp/omeka/`/.htaccess /var/www/html/ \
    && chown -R root.www-data /var/www/html \
    && find /var/www/html -type d | xargs chmod 775 \
    && find /var/www/html -type f | xargs chmod 664 \
    && find files -type d | xargs chmod 777 \
    && find files -type f | xargs chmod 666

RUN { \
    echo '[database]'; \
    echo 'host     = "omeka-db"'; \
    echo 'username = "omeka"'; \
    echo 'password = "omeka"'; \
    echo 'dbname   = "omeka"'; \
    echo 'prefix   = "omeka_"'; \
    echo 'charset  = "utf8"'; \
    echo 'port     = "3306"'; \
	} > /var/www/html/db.ini

COPY 000-default.conf /etc/apache2/sites-available/000-default.conf
COPY config.ini /var/www/html/application/config/config.ini
# WARNING!
#COPY info.php /var/www/html/

RUN a2enmod rewrite
