//
// Created by spl211 on 04/01/2021.
//

#include "../include/TaskRFS.h"
#include "../include/encoderDecoder.h"
#include "../include/connectionHandler.h"
#include <iostream>
#include <thread>
#include <string>
#include <utility>
using namespace std;


TaskRFS::TaskRFS(ConnectionHandler &con):connectionHandler(con){}
    void TaskRFS::run(){
        while(1) {
            encoderDecoder encoDeco = encoderDecoder();
            char *opCodeArray = new char[2];
            char *messageOpCodeArray = new char[2];
            connectionHandler.getBytes(opCodeArray, 2);
            short opCode = encoDeco.decodeOpCode(opCodeArray);
            connectionHandler.getBytes(messageOpCodeArray, 2);
            short messageOpCode = encoDeco.decodeMessageOpCode(messageOpCodeArray);
            string output = "";
            if (opCode == 12)
                output = output + "ACK ";
            if (opCode == 13)
                output = output + "ERROR ";
            cout << output << messageOpCode << endl;
            string frame;
            connectionHandler.getFrameAscii(frame, '\0');
            if(frame != " ")
                cout << frame << endl;
            if(output == "ACK " && messageOpCode == 4) {
                connectionHandler.setStatus(2);
                break;
            }
            else
                connectionHandler.setStatus(3);
        }
    }