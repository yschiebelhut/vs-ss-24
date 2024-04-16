# ---------- Build Stage ------------
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build

# Install dependencies in a separate step, to be cacheable
COPY ./pom.xml /build
COPY ./mvnw /build
COPY ./.mvn/ /build/.mvn/
RUN ./mvnw verify clean --fail-never # yes, this will always fail :/

# Make sourcecode available in build container
COPY . /build
# Run Maven Install
RUN ./mvnw install

# Move resulting jars into /build/result
RUN cd target && ls
# RUN mkdir -p build/result && (cd build/result; jar -xf ../*.jar)


# ---------- Run Stage --------------
FROM eclipse-temurin:17-jdk-alpine
# VOLUME /tmp

# Copy over executables from Build Stage
COPY --from=build /build/target/categoryservice-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /build/target/classes/ /app/
# COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
# COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-jar","/app/app.jar"]
