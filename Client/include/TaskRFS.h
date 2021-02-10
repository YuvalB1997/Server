
#ifndef ASSIGNMENT3_READFROMSOCKET_H
#define ASSIGNMENT3_READFROMSOCKET_H


#include "../include/connectionHandler.h"
#include "../include/encoderDecoder.h"
#include <iostream>
#include <thread>
#include <string>
#include <utility>

class TaskRFS {
private:
    ConnectionHandler &connectionHandler;
public:

    TaskRFS(ConnectionHandler &con);
    void run();

};


#endif //ASSIGNMENT3_READFROMSOCKET_H
