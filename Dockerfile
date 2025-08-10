FROM eclipse-temurin:17-jre-alpine

# Create app directory
WORKDIR /app

# Copy the JAR file
COPY target/orders-management-system-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Environment variables
ENV SPRING_PROFILES_ACTIVE=production
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=logistica_pedidos

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
