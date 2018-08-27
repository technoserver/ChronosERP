package com.chronos.controll;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named
@ViewScoped
public class TesteControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private String teste;

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


    public void setarValoresCEP() {
        Map<String, String> requestParamMap = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String cep = requestParamMap.get("cep");
        String estado = requestParamMap.get("estado");
        String cidade = requestParamMap.get("cidade");
        String bairro = requestParamMap.get("bairro");
        String endereco = requestParamMap.get("endereco");

        System.out.println("cep = " + cep);
        System.out.println("estado = " + estado);
        System.out.println("cidade = " + cidade);
        System.out.println("bairro = " + bairro);
        System.out.println("endereco = " + endereco);


    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }
}
