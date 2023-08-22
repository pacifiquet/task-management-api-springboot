# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR taskmanagement/app

# Copy only the POM file first to take advantage of Docker layer caching
COPY pom.xml .

# Download and cache the Maven dependencies
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


# Stage 2: Create the production image
FROM eclipse-temurin:17-jre-alpine

VOLUME /tmp

ARG DEPENDENCY=taskmanagement/app/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /taskmanagement/app/lib

COPY --from=build ${DEPENDENCY}/META-INF /taskmanagement/app/META-INF

COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /taskmanagement/app

RUN addgroup -S demo && adduser -S demo -G demo

USER demo
EXPOSE 8080
ENTRYPOINT ["java","-cp","/taskmanagement/app:/taskmanagement/app/lib/*","com.taskhero.Main"]
