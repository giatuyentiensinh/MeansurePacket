import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class RecvTCP {
	public static void main(String[] args) throws IOException {
		ServerSocket tcpServer = new ServerSocket(Consts.TCPPORT);
		System.out.println("RecvTCP was running ...");
		int count = 0;
		while (true) {
			Socket server = tcpServer.accept();
			BufferedReader is = new BufferedReader(new InputStreamReader(
					server.getInputStream()));
			String line = is.readLine();
			System.out.println("=========================");
			System.out.println("[x] Recv: " + line);
			server.close();
			count++;
			if (count == Consts.COUNT)
				break;
		}
		tcpServer.close();
	}
}
