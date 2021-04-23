build:
	mkdir -p bin; \
	javac src/*.java -d bin/

.PHONY: square 
square: build
	cd bin; \
	java -Xmx2g Main -FILE square-static.png -SIZE 9000 -THRESHOLD 100 -XLO -2.4 -XHI 1.5 -YLO -2.4 -YHI 1.5 -NUMTHREADS 8

.PHONY: dave
dave: build
	cd bin; \
	java -Xmx2g Main -FILE dave-static.png -SIZE 5000 -THRESHOLD 1000 -XLO -1.6749731562408687 -XHI -1.6749248283369988 -YLO -0.013026075709070781 -YHI -0.0129656380086610 -NUMTHREADS 8

.PHONY: benchmark
benchmark: build
	cd bin; \
	java -Xmx2g Main -FILE benchmark-static.png -SIZE 5000 -THRESHOLD 1000 -XLO -1 -XHI -0.85 -YLO -0.6 -YHI -0.55 -NUMTHREADS 8
