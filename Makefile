# Define the default target
default: test

start-dev:
	mvn spring-boot:run

test:
	mvn clean test jacoco:report

start-database:
	docker compose up -d --build

clean:
	mvn clean

install:
	mvn install

clean-docker:
	docker compose down --remove-orphans --volumes