FROM openjdk:21-jdk-slim

# Chrome headless
RUN apt-get update && \
    apt-get install -y wget gnupg unzip curl && \
    curl -fsSL https://dl-ssl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google.gpg] http://dl.google.com/linux/chrome/deb/ stable main" | tee /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copia e compila o projeto
WORKDIR /app
COPY . .

RUN ./gradlew clean shadowJar --no-daemon

CMD ["java", "-jar", "build/libs/scraper-1.0-SNAPSHOT-all.jar"]
