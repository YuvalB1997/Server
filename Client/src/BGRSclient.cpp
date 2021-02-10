//
// Created by spl211 on 01/01/2021.
//

#include <iostream>
#include "../include/connectionHandler.h"
#include "../include/TaskRFS.h"
#include "../include/TaskRFK.h"

using namespace std;

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

   ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    TaskRFS readFromServer(connectionHandler);
    TaskRFK readFromKeyboard(connectionHandler);

    thread socketThread(&TaskRFS::run, &readFromServer);
    thread keyboardThread(&TaskRFK::run, &readFromKeyboard);

    socketThread.join();
    keyboardThread.join();
    return 0;
}