FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY src ./src
RUN mkdir out && javac -encoding UTF-8 -d out $(find src -name "*.java")

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/out ./out
ENV DATA_DIR=/app/data \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8
RUN mkdir -p /app/data
VOLUME ["/app/data"]
CMD ["java", "-Dfile.encoding=UTF-8", "-cp", "out", "com.eam.biblioteca.Main"]