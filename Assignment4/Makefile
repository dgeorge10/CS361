build:
	mkdir -p bin; \
	javac src/*.java -d bin/

.PHONY:
small: build
	cd bin; \
	java Main -r 3 -c 3 -d 3

.PHONY: large
large: build
	cd bin; \
	java Main -r 12 -c 12 -d 12