#Makefile

SONAR := org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
PROJECT_KEY := task-management-backend
PROJECT_NAME := 'task-management-backend'
TOKEN := 'sqa_09ecc5e35ce2d2421bb0968eff682cdc56aad0c1'
# A default target that runs when you type "make" without specifying a target
default: build test


format:
	 @mvn spotless:apply


analyze:
	 @mvn clean verify sonar:sonar \
      -Dsonar.projectKey=task-management-backend \
      -Dsonar.projectName='task-management-backend' \
      -Dsonar.host.url=http://localhost:9000 \
      -Dsonar.token=sqa_09ecc5e35ce2d2421bb0968eff682cdc56aad0c1
build:
	 @mvn clean install

start-dev:
	 @mvn spotless:apply
	 @mvn clean test
	 @mvn spring-boot:run

test:
	 @mvn spotless:check
	 @mvn clean test jacoco:report

clean:
	 @mvn clean

install:
	 @mvn install

pre-commit:
	 @pre-commit run --all-files

docker-clean:
	 @docker compose down --remove-orphans --volumes
docker-start:
	 @docker compose up -d --build
docker-stop:
	 @docker compose stop

docker-sonar-start:
	 @docker compose -f docker-compose-sonarqube.yaml up -d --build

docker-sonar-stop:
	 @docker compose -f docker-compose-sonarqube.yaml stop

docker-build:
	 @docker build .
deploy:
	 @echo "Deployment completed successfully."

