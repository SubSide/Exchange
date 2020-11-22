ARG VERSION=8u151

FROM openjdk:${VERSION}-jdk AS build
COPY . /src
WORKDIR /src
RUN ./gradlew --no-daemon shadowJar

FROM openjdk:${VERSION}-jre
COPY --from=build /src/build/libs/Exchange-1.0.jar /bin/exchange/exchange.jar
WORKDIR /bin/exchange

CMD ["java", "-jar", "exchange.jar"]
