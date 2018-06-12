FROM dpatriot/docker-awscli-java8
RUN mkdir -p "/SpringPagination"
COPY target/spring-pagination-1.0.jar /SpringPagination
CMD ["java", "-jar", "/SpringPagination/spring-pagination-1.0.jar"]
EXPOSE 8080




