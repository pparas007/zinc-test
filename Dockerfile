FROM openjdk:8
EXPOSE 8080
ADD target/zinc-test-docker.war zinc-test-docker.war
ENTRYPOINT ["java","-jar","/zinc-test-docker.war"]