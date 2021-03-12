FROM ghcr.io/graalvm/graalvm-ce:latest

RUN gu install native-image
RUN native-image --help

RUN java -version

ADD ./ ./
RUN ./mvnw -pl getting-started -e clean package

RUN native-image -jar getting-started/target/getting-started-1.0-SNAPSHOT.jar getting-started

ENTRYPOINT java -jar getting-started/target/getting-started-1.0-SNAPSHOT.jar