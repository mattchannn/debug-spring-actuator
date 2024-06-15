FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
ADD target/pilot-product-papi-1.0.0-RELEASE.jar .
ADD entry.sh .
EXPOSE 8080

ENTRYPOINT ["/app/entry.sh"]