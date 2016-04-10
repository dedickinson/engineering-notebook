FROM dedickinson/base-java-git

MAINTAINER duncan@dickinson.name

COPY builder.sh ./

ENTRYPOINT ["./builder.sh"]

VOLUME $APP_HOME/repo
