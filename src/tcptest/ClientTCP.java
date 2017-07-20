package tcptest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientTCP implements Runnable {

	private Socket tcpClient;
	private OutputStreamWriter os;
	private Integer count;
	private List<String> list;

	public ClientTCP(int count) {
		try {
			this.tcpClient = new Socket(InetAddress.getByName("localhost"),
					5678);
			this.tcpClient.setKeepAlive(true);
			this.os = new OutputStreamWriter(tcpClient.getOutputStream());

			list = new ArrayList<String>();
			String file = getClass().getResource(
					"simulation_01_10node_5chs_60s.csv").getFile();
			BufferedReader br = new BufferedReader(new FileReader(file));
			// BufferedReader br = new BufferedReader(
			// new FileReader(
			// "/home/tuyenng/Documents/eclipseTesttmp/MeansurePacket/bin/assets/simulation_01_10node_5chs_60s.csv"));
			String line = br.readLine();
			while (line != null) {
				String[] datas = line.split(",");
				list.add(datas[0]);
				line = br.readLine();
			}
			br.close();
			this.count = list.size();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		ClientTCP client = new ClientTCP(Integer.parseInt("144"));
		client.run();
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < count; i++) {
				os.write(list.get(i));
				os.flush();
				System.out.println("Buffer size: "
						+ tcpClient.getSendBufferSize());
				System.out.println("Data size: " + list.get(i).length() + " , "
						+ list.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				tcpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
