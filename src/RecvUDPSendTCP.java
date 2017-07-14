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

	private DatagramSocket udpServer;

	private Socket tcpClient;
	private OutputStreamWriter osw;

	private FileWriter fw;
	private BufferedWriter bw;

	private FileReader fr;
	private BufferedReader br;

	public RecvUDPSendTCP() throws IOException {
		// Init communication
		udpServer = new DatagramSocket(Consts.UDPPORT);
		System.out.println("RecvUDPSendTCP was running ...");

		tcpClient = new Socket(InetAddress.getByName(Consts.HOST),
				Consts.TCPPORT);
		osw = new OutputStreamWriter(tcpClient.getOutputStream());

		// Init file
		fw = new FileWriter(getClass().getResource(Consts.FILEOUT).getFile());
		bw = new BufferedWriter(fw);
		fr = new FileReader(getClass().getResource(Consts.FILE).getFile());
		br = new BufferedReader(fr);
	}

	/**
	 * Lưu kết quả độ trễ sử lý vào file
	 * 
	 * @param latency
	 * @throws IOException
	 */
	public void outputToFile(long latency) throws IOException {
		String line = br.readLine();
		long timestamp3 = Long.parseLong(line.split(",")[2]) + latency;
		String newline = line + "," + latency + "," + timestamp3 + "\n";
		bw.write(newline);
	}

	/**
	 * Đóng kết nối và đóng file
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		bw.close();
		fw.close();
		br.close();
		fr.close();

		osw.close();
		tcpClient.close();
		udpServer.close();
	}

	public static void main(String[] args) throws IOException {

		RecvUDPSendTCP exec = new RecvUDPSendTCP();

		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		int count = 0;
		while (true) {
			exec.udpServer.receive(packet);

			long beginTime = System.currentTimeMillis();
			byte[] data = packet.getData();
			String str = new String(data);

			exec.osw.write(str.trim() + "\n");
			long endTime = System.currentTimeMillis();
			exec.osw.flush();

			long latency = endTime - beginTime;
			System.out.println(str);
			System.out.println("Latency: " + latency);
			exec.outputToFile(latency);

			System.out.println("[x] Sending done.");

			count++;
			if (count == Consts.COUNT) {
				exec.close();
				break;
			}
		}

	}
}
