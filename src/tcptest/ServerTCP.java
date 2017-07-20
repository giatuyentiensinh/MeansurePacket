package tcptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
	public static void main(String[] args) throws IOException {
		ServerSocket tcpServer = new ServerSocket(5678);
		System.out.println("RecvTCP was running ...");
		int count = 0;
		Socket server = tcpServer.accept();
		BufferedReader is = new BufferedReader(new InputStreamReader(
				server.getInputStream()));
		while (true) {
			String line = is.readLine();
			System.out.println("=========================");
			System.out.println("[x] Recv: " + line);
			System.out.println(server.getReceiveBufferSize());
			count++;
			if (count == 144)
				break;
		}
		server.close();
		tcpServer.close();
	}
}
