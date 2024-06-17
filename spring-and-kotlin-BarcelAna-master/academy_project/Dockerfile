FROM openjdk:17-alpine
COPY build/libs/BOOT-INF/lib /app/lib
COPY build/libs/META-INF /app/META-INF
COPY build/libs/BOOT-INF/classes /app
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.infinum.academy.AcademyProjectApplicationKt"]