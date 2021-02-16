package com.hb.mcfdebugger;

import java.io.IOException;
import java.net.UnknownHostException;

import static com.hb.mcfdebugger.McfDebugger.LOG;

public class DebugThread extends Thread {
    public static Boolean nextStep = false;
    public static wsServer wsserver;

    @Override
    public void run() {
        super.run();
        LOG("debugThreadStarted!");


    }
    public void restartWsServer(int port ,int timeOut){
        try {
            this.stopWsServer();
            wsserver = wsServer.startWs(port);
            wsserver.setConnectionLostTimeout(timeOut);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void stopWsServer(){
        if(wsserver!=null){
            try {
                wsserver.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public static void sendObjMsgToDebugger(Object obj, String msgType) {
        if(wsserver!=null){
            wsServer.MsgObj msgObj = new wsServer.MsgObj(msgType, obj);
            wsserver.sendObj(msgObj);
        }
    }
}
