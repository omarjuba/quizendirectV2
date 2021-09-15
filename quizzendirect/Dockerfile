FROM maven as frontapp
COPY . /home/

WORKDIR /home

RUN mvn clean install spring-boot:repackage


FROM openjdk
COPY --from=frontapp /home/target/QED-0.0.1-SNAPSHOT.jar /home/
EXPOSE 8081
CMD ["java", "-jar", "/home/QED-0.0.1-SNAPSHOT.jar"]
