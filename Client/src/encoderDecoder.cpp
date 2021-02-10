

#include <vector>
#include "../include/encoderDecoder.h"
#include <bits/stdc++.h>
using namespace std;


encoderDecoder::encoderDecoder(): codeArray(),len(0),opCode1(0),opCode2(0){}

std::string encoderDecoder::decodeString(char* code){
    int code_size = sizeof(code) / sizeof(char);
    string s_code = convertToString(code,code_size);
    return s_code;
}

string encoderDecoder::convertToString(char *a, int size) {
    string s ="";
    for(int i=0;i<size;i=i+1)
        s = s + a[i];
    return s;
}

short encoderDecoder::decodeOpCode(char *nextByte) {
    opCode1 = (short)((nextByte[0]&0xff)<<8);
    opCode1+=(short)(nextByte[1]&0xff);
    return opCode1;
}

short encoderDecoder::decodeMessageOpCode(char *nextByte) {
    opCode2 = (short)((nextByte[0]&0xff)<<8);
    opCode2+=(short)(nextByte[1]&0xff);
    return opCode2;
}

int encoderDecoder::encode(std::string message,char* code) {
    int count = 2;
    vector<string> codeMessage;
    string delimiter = " ";
    size_t pos = 0;
    string token;
    pos = message.find(delimiter);
    token = message.substr(0, pos);
    codeMessage.push_back(token);
    message.erase(0, pos + delimiter.length());
    codeMessage.push_back(message);
    short opCode = 0;
    if(codeMessage[0]=="ADMINREG")
        opCode=1;
    if(codeMessage[0]=="STUDENTREG")
        opCode=2;
    if(codeMessage[0]=="LOGIN")
        opCode=3;
    if(codeMessage[0]=="LOGOUT")
        opCode=4;
    if(codeMessage[0]=="COURSEREG")
        opCode=5;
    if(codeMessage[0]=="KDAMCHECK")
        opCode=6;
    if(codeMessage[0]=="COURSESTAT")
        opCode=7;
    if(codeMessage[0]=="STUDENTSTAT")
        opCode=8;
    if(codeMessage[0]=="ISREGISTERED")
        opCode=9;
    if(codeMessage[0]=="UNREGISTER")
        opCode=10;
    if(codeMessage[0]=="MYCOURSES")
        opCode=11;
    code[0]=opCode/10;
    code[1]=opCode%10;
    if(opCode==1 || opCode==2 || opCode==3) {// 2 byte opCode ,String ,1 byte \0 ,String,1 byte \0
        std::replace(codeMessage[1].begin(),codeMessage[1].end(),' ','\0');
        const char *bytes1 = (codeMessage[1]).c_str();
        int j=0;
        for(int i = 0 ;i<(signed)sizeof(bytes1)+100 && j<2;i=i+1) {
          if(bytes1[i]=='\0')
              j=j+1;
          code[count] = bytes1[i];
          count = count+1;
        }
        return count ;
    }
    if(opCode==4 || opCode == 11){//2 byte op code
        return count;
    }
    if(opCode==5 || opCode==6 || opCode==7 || opCode==9 || opCode==10){//2 byte op code ,2 byte info
        short info = boost::lexical_cast<short>(codeMessage[1]);
        code[2] = ((info>>8)&0xFF);
        code[3] = (info & 0xFF);
        return 4;
    }
    if(opCode==8){//2 byte opCode , String , 1 byte \0
        const char *bytes1 =(codeMessage[1]+'\0').c_str();
        int j=0;
        for(int i = 0 ;i<(signed)sizeof(bytes1)+100 && j<2 ;i=i+1) {
            if(bytes1[i]=='\0')
                j=j+1;
            code[count] = bytes1[i];
            count = count+1;
        }
        return count;
    }
    return count;
}
