package com.hb.mcfdebugger;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import static com.hb.mcfdebugger.McfDebugger.LOG;

public class wsServer extends WebSocketServer {

    public wsServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        LOG("websocket Server start at port:"+port);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake clientHandshake) {
        LOG("new connection ===" + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        LOG("you have a new message: "+ message);
        //向客户端发送消息
        wsCommandParser.DebugHandler(message);
        //conn.send(message);
    }

    @Override
    public void onError(WebSocket conn, Exception e) {
        //e.printStackTrace();
        if( conn != null ) {
            //some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
    LOG("Ws Started!");
    }

    public static wsServer startWs(int port) throws UnknownHostException {
        wsServer s = new wsServer(port);
        s.start();
        return s;
    }

    public void sendObj(MsgObj msgObj){
            Gson gson=new Gson();
            String backMsg=gson.toJson(msgObj);
            this.broadcast(backMsg);

    }
    public static class MsgObj{
        public String msgType;
        public Object bodyObj;
        public MsgObj(String msgType,Object bodyObj){
            this.msgType=msgType;
            this.bodyObj=bodyObj;
        }
    }
}