package com.chronos.controll;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class TesteControll implements Serializable {

    private static final long serialVersionUID = 1L;


    public void enviarMensage() {
        try {
            System.out.println("teste");

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare("hello-queue", false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", "hello-queue", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
