FROM maven:3.8.1-openjdk-11 as build
COPY . /src
WORKDIR /src
RUN mvn clean package -DskipTests


FROM openjdk:11
ENV HOMEDIR=/home/boletos
WORKDIR $HOMEDIR
COPY --from=build /src/target/boleto-service.jar boleto-service.jar
CMD ["java","-jar","boleto-service.jar"]

