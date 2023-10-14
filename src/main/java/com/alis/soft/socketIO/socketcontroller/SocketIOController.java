package com.alis.soft.socketIO.socketcontroller;

import com.alis.soft.socketIO.data.Message;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SocketIOController {

    @Autowired
    private SocketIOServer socketServer;

    Faker faker;
    ChuckNorris chuckNorris;

    SocketIOController(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        /**
         * Here we create only one event listener
         * but we can create any number of listener
         * messageSendToUser is socket end point after socket connection user have to send message payload on messageSendToUser event
         */
        log.info("ouvindo evento messageSendToUser");
        this.socketServer.addEventListener("messageSendToUser", Message.class, onSendMessage);

        faker = new Faker();

        chuckNorris = faker.chuckNorris();

        log.info(chuckNorris.fact());
    }


    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
            log.info("Perform operation on user connect in controller");
        }
    };


    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            log.info("Perform operation on user disconnect in controller");
            log.info(chuckNorris.fact());
        }
    };

    public DataListener<Message> onSendMessage = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {

            log.info("Dados recebidos");
            log.info("==============");
            /**
             * Sending message to target user
             * target user should subscribe the socket event with his/her name.
             * Send the same payload to user
             */

            log.info(message.getSenderName()+" user send message to user "+message.getTargetUserName()+
                    " and message is "+message.getMessage());


            socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(),client, message);
            socketServer.getBroadcastOperations().sendEvent

            socketServer.getBroadcastOperations().sendEvent("teste123",client, "Apenas uma ping qualquer :) ");
            /**
             * After sending message to target user we can send acknowledge to sender
             */
            acknowledge.sendAckData("Mensagem enviada ao usuario alvo com sucesso!!");

            log.info(chuckNorris.fact());

        }
    };

}
