CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude -lpthread -lboost_thread -lboost_system
LDFLAGS:=-lboost_system -lpthread -lboost_thread

all: BGRSclient
	g++ -o bin/BGRSclient bin/connectionHandler.o bin/BGRSclient.o bin/TaskRFK.o bin/TaskRFS.o bin/encoderDecoder.o $(LDFLAGS)

BGRSclient: bin/connectionHandler.o bin/BGRSclient.o bin/TaskRFK.o bin/TaskRFS.o bin/encoderDecoder.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BGRSclient.o: src/BGRSclient.cpp
	g++ $(CFLAGS) -o bin/BGRSclient.o src/BGRSclient.cpp

bin/TaskRFK.o: src/TaskRFK.cpp
	g++ $(CFLAGS) -o bin/TaskRFK.o src/TaskRFK.cpp

bin/TaskRFS.o: src/TaskRFS.cpp
	g++ $(CFLAGS) -o bin/TaskRFS.o src/TaskRFS.cpp

bin/encoderDecoder.o: src/encoderDecoder.cpp
	g++ $(CFLAGS) -o bin/encoderDecoder.o src/encoderDecoder.cpp

.PHONY: clean
clean:
	rm -f bin/*
