package com.mySpace.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@Configuration
public class MySpaceConfiguration {

    @Value("${server.port}")
    String portNumber;

    @Value("${server.servlet.context-path}")
    String contextPath;

    @Bean
    public String indexQrCodeUrl(){

        String imgSize="300x300";

        return "https://chart.googleapis.com/chart?chs="+imgSize+"&cht=qr&chl="+indexUrl()+"&chco=e93f64";
    }
    @Bean
    public String indexUrl(){
        String ip="";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return "http://"+ip+":"+portNumber+contextPath;
    }
}
