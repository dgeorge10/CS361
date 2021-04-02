clean:
	rm -Rfc bin/*

build:
	javac src/Main.java

one: build
	java Main -SIZE 1000 -THRESHOLD 500 -XLO -2.4 -XHI 1.5 -YLO -2.4 -YHI 1.5 -NUMTHREADS 1000

two: build
	java Main -SIZE 2000 -THRESHOLD 1000 -XLO -2 -XHI -1.4 -YLO -0.2 -YHI 0.4 -NUMTHREADS 1000

three: build
	java Main -NUMTHREADS 2 -SIZE 4000 -THRESHOLD 10000 -XLO -1.61750 -XHI -0.017080 -YLO -1.61745 -YHI -0.017030

