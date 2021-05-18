build:
	mkdir -p bin; \
  	javac src/*.java -d bin/

.PHONY: small
small: build
	cd bin; \
	java Main -f 4 -g 2 -t 4 -nf 50 -fmin 30 -cfmin 0 -cfmax 10 -bfmin 2 -bfmax 20 -smax 10000 -sc 2 -min_sse 1 -ss_max 20 -ss_min 15 -kmax 500 -s_max 5

.PHONY: large
large: build
	cd bin; \
	java Main -f 8 -g 4 -t 8 -nf 50 -fmin 30 -cfmin 0 -cfmax 10 -bfmin 2 -bfmax 20 -smax 10000 -sc 15 -min_sse 2 -ss_max 20 -ss_min 15 -kmax 500 -s_max 5