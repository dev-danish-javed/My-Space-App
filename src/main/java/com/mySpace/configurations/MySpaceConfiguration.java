package com.mySpace.configurations;

import com.mySpace.ui.model.response.DownloadLinkDataViewModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MySpaceConfiguration {

    @Value("${server.port}")
    String portNumber;

    @Value("${server.servlet.context-path}")
    String contextPath;

    @Bean
    public String indexQrCodeUrl(){

        String imgSize="300x300";
        System.out.println("\n\nURL "+indexUrl());
//        return "https://chart.googleapis.com/chart?chs="+imgSize+"&cht=qr&chl="+indexUrl()+"&chco=e93f64";
        return "https://quickchart.io/qr?text="+indexUrl()+"&light=f8ffe0&dark=202117&margin=0&size=500";
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
    @Bean
    public Map<String, DownloadLinkDataViewModel> getDownloadDataMapBean(){
        return new HashMap<>();
    }
}
