FROM openjdk:alpine

ENV APP_HOME /app/

COPY target/jaegerqe-controller-*.jar $APP_HOME/jaegerqe-controller.jar

WORKDIR $APP_HOME
CMD java -jar jaegerqe-controller.jar