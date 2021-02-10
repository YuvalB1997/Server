//
// Created by spl211 on 03/01/2021.
//

#ifndef ASSIGNMENT3_TASKRFK_H
#define ASSIGNMENT3_TASKRFK_H
#include "../include/connectionHandler.h"
#include "../include/encoderDecoder.h"
#include <iostream>
#include <thread>
#include <string>
#include <utility>

class TaskRFK{

private:

ConnectionHandler &connectionHandler;
public:

    TaskRFK(ConnectionHandler &con);
    void run();

};

#endif //ASSIGNMENT3_TASKRFK_H
