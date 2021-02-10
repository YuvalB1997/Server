package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImp implements MessageEncoderDecoder<RSMessage> {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private byte[] opCodeMessage = new byte[2];
    short opCode = 0;
    int numOfOpCode = 0;
    int numOfZero = 0;
    int numOfBytes = 0;


    @Override
    public RSMessage decodeNextByte(byte nextByte) {
        if(numOfOpCode < 2){
            opCodeMessage[numOfOpCode]=nextByte;
            numOfOpCode++;
        }
        if(numOfOpCode==2 && opCode==0){
            opCode = (short)((opCodeMessage[0]&0xff)<<8);
            opCode+=(short) (opCodeMessage[1]&0xff);
            if(opCodeMessage[0]==1)
                opCode= (short)(opCodeMessage[0]*10+opCodeMessage[1]);
        }
        if(nextByte=='\0'){
            numOfZero++;
        }
        numOfBytes++;
        pushByte(nextByte);
        if((opCode==1 || opCode==2 || opCode==3)&&numOfZero==3) {// 2 byte opCode ,String ,1 byte \0 ,String,1 byte \0
            return returnMessage();
        }
        if(opCode==4 || opCode == 11) {//2 byte op code
            return returnMessage();
        }
        if( (opCode==5 || opCode==6 || opCode==7 || opCode==9 || opCode==10 || opCode==13)&&numOfBytes==4 ){//2 byte op code ,2 byte info
            return returnMessage();
        }
        if((opCode==8)&& numOfZero==3){//2 byte opCode , String , 1 byte \0
            return returnMessage();
        }
        return null;
    }

    @Override
    public byte[] encode(RSMessage message) {
       String[] code = message.getMessage();
       short opCode = Short.parseShort(code[0]);
       byte[] arr1 = new byte[2];
       byte[] arr2 =null;
       arr1[0] = (byte) ((opCode>>8)&0xFF);
       arr1[1] = (byte)(opCode&0xFF);
       int i = 1;
       if(code[1].length()==2 || code[1].length()==1 ){
           i++;
           short opCode2 = Short.parseShort(code[1]);
           arr2 = new byte[2];
           arr2[0] = (byte) ((opCode2>>8)&0xFF);
           arr2[1] = (byte)(opCode2&0xFF);
           arr1=merge(arr1,arr2);
       }
       for(int j = i; j<code.length;j++){
           arr1=merge(arr1,(code[j]+"\n").getBytes(StandardCharsets.UTF_8));
       }
       byte[] arr3 = new byte[1];
       arr1=merge(arr1,arr3);
       return arr1;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }


    private RSMessage returnMessage(){
        short result = opCode;
        String[] code = new String[0];

        if(result==1 || result==2 || result==3) {// 2 byte opCode ,String ,1 byte \0 ,String,1 byte \0
            System.out.println();
            code = new String[3];
            code[0] = String.valueOf(result);
            int i = 2;
            while (bytes[i] != '\0') {
                i++;
            }
            code[1]=new String(Arrays.copyOfRange(bytes,2,i), StandardCharsets.UTF_8);
            code[2]=new String(Arrays.copyOfRange(bytes,i ,bytes.length), StandardCharsets.UTF_8);
        }


        if(result==4 || result == 11){//2 byte op code
            code=new String[1];
            code[0]=String.valueOf(result);
        }


        if(result==5 || result==6 || result==7 || result==9 || result==10){//2 byte op code ,2 byte info
            code=new String[2];
            code[0]=String.valueOf(opCode);
            short result2 = (short)((bytes[2] & 0xff) << 8);
            result2 += (short)(bytes[3] & 0xff);
            code[1]=String.valueOf(result2);
        }


        if(result==8){//2 byte opCode , String , 1 byte \0
            code=new String[2];
            code[0]=String.valueOf(result);
            int i = 2;
            while (bytes[i] != '\0') {
                i++;
            }
            code[1]=new String(Arrays.copyOfRange(bytes,2,i), StandardCharsets.UTF_8);
        }
        bytes = new byte[1 << 10];
        len = 0;
        opCodeMessage = new byte[2];
        opCode = 0;
        numOfOpCode = 0;
        numOfZero = 0;
        numOfBytes = 0;
        return new RSMessage(code);
    }

    private byte[] merge(byte[] arr1 ,byte[] arr2){
        byte[] arr3 = new byte[arr1.length+arr2.length];
        System.arraycopy(arr1, 0, arr3, 0, arr1.length);
        System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);
        return arr3;
    }
}



