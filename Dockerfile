FROM openjdk:11
ENV HOMEDIR=/home/boletos
WORKDIR $HOMEDIR
ADD target/boleto-service.jar boleto-service.jar
CMD ["java","-jar","boleto-service.jar"]

