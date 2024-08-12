# Sử dụng hình ảnh Maven chính thức để build ứng dụng
FROM maven:3.8.5-openjdk-17 AS build

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file pom.xml và các file cần thiết khác để build
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN echo "MAVEN_CONFIG=$MAVEN_CONFIG"
RUN echo "MAVEN_OPTS=$MAVEN_OPTS"
RUN echo "MAVEN_CLI_OPTS=$MAVEN_CLI_OPTS"
RUN printenv

#RUN set MAVEN_OPTS=-Dmaven.repo.local=.m2/repository
#RUN set MAVEN_CLI_OPTS=--batch-mode
#RUN set MAVEN_CONFIG = /PT/.m2
# Cấp quyền thực thi cho mvnw

RUN chmod +x ./mvnw && ENV MAVEN_CONFIG=
#RUN ./mvnw clean verify
# Tải các dependencies xuống trước để cache chúng
RUN ./mvnw dependency:go-offline -B

# Sao chép mã nguồn vào trong container
COPY src ./src

# Build ứng dụng Spring Boot
RUN ./mvnw clean package -DskipTests

# Sử dụng hình ảnh OpenJDK chính thức để chạy ứng dụng
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép jar file từ bước build trước vào container
COPY --from=build /app/target/appointmentscheduler-*.jar app.jar

# Khai báo cổng mặc định mà ứng dụng sẽ chạy
EXPOSE 8080

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]