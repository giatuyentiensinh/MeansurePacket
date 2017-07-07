import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class RecvUDPSendTCP {
	public static void main(String[] args) throws IOException {
		DatagramSocket udpServer = new DatagramSocket(Consts.UDPPORT);
		System.out.println("RecvUDPSendTCP was running ...");

		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(RecvUDPSendTCP.class
					.getResource(Consts.FILEOUT).getFile());
		} catch (NullPointerException e) {
			System.err.println("Find '" + Consts.FILEOUT + "' not found");
			System.err.println("\u001B[1m You need create a file '"
					+ Consts.FILEOUT + "' to completion program");
			System.exit(0);
		}
		bw = new BufferedWriter(fw);
		FileReader fr = new FileReader(RecvUDPSendTCP.class.getResource(
				Consts.FILE).getFile());
		BufferedReader br = new BufferedReader(fr);

		int count = 0;
		while (true) {
			long beginTime = System.currentTimeMillis();
			udpServer.receive(packet);
			byte[] data = packet.getData();
			String str = new String(data);

			Socket tcpClient = new Socket(InetAddress.getByName(Consts.HOST),
					Consts.TCPPORT);
			OutputStreamWriter os = new OutputStreamWriter(
					tcpClient.getOutputStream());
			long endTime = System.currentTimeMillis();
			os.write(str);

			long latency = endTime - beginTime;
			System.out.println(str);
			System.out.println("Latency: " + latency);
			String line = br.readLine();
			long timestamp3 = Long.parseLong(line.split(",")[2]) + latency;
			String newline = line + "," + latency + "," + timestamp3 + "\n";
			bw.write(newline);
			os.flush();
			System.out.println("Sending done.");
			tcpClient.close();

			count++;
			if (count == Consts.COUNT) {
				br.close();
				fr.close();
				bw.close();
				fw.close();
				break;
			}
		}
		udpServer.close();
	}
}
