package com.mySpace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.awt.*;
import java.io.IOException;
import java.net.*;

@SpringBootApplication
public class MySpaceApplication {

	@Autowired
	private static Environment env;

	public static void main(String[] args) {

		try{
			ConfigurableApplicationContext ctx=SpringApplication.run(MySpaceApplication.class, args);
			openHomePage(ctx);
		}
		catch(Exception e){
			String ip="";
			try(final DatagramSocket socket = new DatagramSocket()){
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				ip = socket.getLocalAddress().getHostAddress();
			} catch (UnknownHostException | SocketException exception) {
				exception.printStackTrace();
			}

			String url="http://"+ip+":8585/mySpace";
			try {
				Runtime runtime=Runtime.getRuntime();
				if(false)
					runtime.exec("rundll32 url.dll,FileProtocolHandler "+url );

			} catch (IOException ioException) {
				System.out.println("some issue");
			}
		}

	}

	private static void openHomePage(ConfigurableApplicationContext ctx){
		Runtime runtime=Runtime.getRuntime();
		String ip="";
		try(final DatagramSocket socket = new DatagramSocket()){
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ip = socket.getLocalAddress().getHostAddress();
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		String contextPath = ctx.getEnvironment().getProperty("server.servlet.context-path");
		String portNumber = ctx.getEnvironment().getProperty("server.port");

		String url="http://"+ip+":"+portNumber+contextPath;
		try {
			runtime.exec("rundll32 url.dll,FileProtocolHandler "+url );
		} catch (IOException e) {
			System.out.println("some issue");
		}
	}

}
