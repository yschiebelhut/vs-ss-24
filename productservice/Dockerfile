# ---------- Build Stage ------------
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build

# Install dependencies in a separate step, to be cacheable
COPY ./pom.xml /build
COPY ./mvnw /build
COPY ./.mvn/ /build/.mvn/
RUN ./mvnw dependency:go-offline

# Make sourcecode available in build container
COPY . /build
# Run Maven Install
# TODO: For some reason --offline does not work, seems we have dependencies that are pulled in during builld :/
RUN ./mvnw install

# Move resulting jars into /build/result
RUN cd target && ls


# ---------- Run Stage --------------
FROM eclipse-temurin:17-jdk-alpine
# VOLUME /tmp

# Copy over executables from Build Stage
COPY --from=build /build/target/productservice-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /build/target/classes/ /app/

ENTRYPOINT ["java","-jar","/app/app.jar"]
