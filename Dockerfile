# syntax=docker/dockerfile:1

# ---- Build stage ----
# Java 8 matches the app's old Jetty 7.6 / JSP 2.1 stack.
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /app

# Cache dependencies first.
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# Build: `package` triggers maven-dependency-plugin to populate target/dependency.
COPY src ./src
RUN mvn -q -B clean package

# The JSP engine compiles .jsp files at runtime with the Eclipse compiler (ecj).
# The bundled ecj-3.5.1 (2009) can't read Java 8 bytecode, so JSP compilation
# fails with "type java.io.X cannot be resolved". Swap in a modern ecj that
# understands Java 8 class files.
RUN mvn -q -B org.apache.maven.plugins:maven-dependency-plugin:2.8:copy \
      -Dartifact=org.eclipse.jdt.core.compiler:ecj:4.6.1 \
      -DoutputDirectory=/app/target/dependency && \
    rm -f /app/target/dependency/ecj-3.5.1.jar

# ---- Runtime stage ----
FROM eclipse-temurin:8-jre
WORKDIR /app

# App reads its webapp resources from src/main/webapp at runtime (see Main.java).
COPY --from=build /app/target/classes    ./target/classes
COPY --from=build /app/target/dependency ./target/dependency
COPY --from=build /app/src/main/webapp   ./src/main/webapp

# Render (and Heroku) inject PORT; Main defaults to 8080 when unset.
ENV PORT=8080
EXPOSE 8080

# Same launch command as the Procfile. The colon-joined classpath keeps the
# shell from glob-expanding target/dependency/* so the JVM handles the wildcard.
CMD ["sh", "-c", "exec java $JAVA_OPTS -cp target/classes:target/dependency/* Main"]
