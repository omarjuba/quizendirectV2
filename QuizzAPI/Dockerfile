FROM maven as apibuild
COPY . /home/

WORKDIR /home

RUN mvn clean install spring-boot:repackage

FROM openjdk
COPY --from=apibuild /home/target/QuizzAPI-0.0.1-SNAPSHOT.jar /home/
EXPOSE 20020
CMD ["java", "-jar", "/home/QuizzAPI-0.0.1-SNAPSHOT.jar"]
