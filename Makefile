#Makefile

SONAR := org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
PROJECT_KEY := task-management-backend
PROJECT_NAME := 'task-management-backend'
TOKEN := 'sqp_6be54a25dee253ade2cf1354eb63e24a8f61f18c'
# A default target that runs when you type "make" without specifying a target
default: build test


format:
	 @mvn spotless:apply


analyze:
	 @mvn -B verify $(SONAR)  -Dsonar.host.url=http://localhost:9000 -Dsonar.token=$(TOKEN) -Dsonar.projectKey=$(PROJECT_KEY) -Dsonar.projectName=$(PROJECT_NAME)

build:
	 @mvn clean install

start-dev:
	 @mvn clean test
	 @mvn spring-boot:run

test:
	 @mvn spotless:check
	 @mvn clean test jacoco:report

start-databases:
	 @docker compose up -d --build

clean:
	 @mvn clean

install:
	 @mvn install

pre-commit:
	 @pre-commit run --all-files
clean-docker:
	 @docker compose down --remove-orphans --volumes
deploy:
	 @echo "Deployment completed successfully."

