

#include "../include/TaskRFK.h"
#include "../include/connectionHandler.h"
#include "../include/encoderDecoder.h"
#include <iostream>
#include <thread>
#include <string>
#include <utility>
using namespace std;


TaskRFK::TaskRFK (ConnectionHandler &con):connectionHandler(con){}
void TaskRFK::run() {
    try {
        while (1) {
            const short bufferSize = 1024;
            char buffer[bufferSize];
            encoderDecoder encoDeco = encoderDecoder();
            cin.getline(buffer, bufferSize);
            string input(buffer);
            if (input == "LOGOUT") {
                connectionHandler.setStatus(1); // set the the disconnection status.
                cout << "Disconnected. Exiting...\n" << endl;
            }
            char *message = new char[input.length() + 10];
            int length = encoDeco.encode(input, message);
            connectionHandler.sendBytes(message, length);
            while(connectionHandler.getStatus() == 1)
                this_thread::sleep_for(chrono::milliseconds(50));
            if (connectionHandler.getStatus() == 2) {
                break;
            }
        }
    }catch(std::exception &exception){}
}


