#Makefile

# A default target that runs when you type "make" without specifying a target
default: build test


format:
	 @mvn spotless:apply

build:
	 @mvn clean install

start-dev:
	 @mvn spotless:check
	 @mvn spring-boot:run

test:
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

