build:
	./gradlew clean build

report:
	./gradlew jacocoTestReport

install-dist:
	./gradlew clean installDist

start-dist:
	./build/install/app/bin/app

.PHONY: build