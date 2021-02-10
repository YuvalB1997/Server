

#ifndef ASSIGNMENT3_ENCODERDECODER_H
#define ASSIGNMENT3_ENCODERDECODER_H
#include <boost/lexical_cast.hpp>

#include <iostream>

class encoderDecoder {

public:
    encoderDecoder();
    std::string decodeString(char* code);
    int encode(std::string message,char* code);
    short decodeOpCode(char* nextByte);
    short decodeMessageOpCode(char* nextByte);
    std::string convertToString(char* a,int size);

private:
    char codeArray[1024];
    int len;
    short opCode1;
    short opCode2;

};


#endif //ASSIGNMENT3_ENCODERDECODER_H
