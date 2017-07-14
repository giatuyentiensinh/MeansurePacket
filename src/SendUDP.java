import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author tuyenng
 * 
 *         Đọc dữ liệu từ file và đóng vào bản tin UDP rồi gửi đi theo stamptime
 *         trong file. Có chức năng mô phỏng traffic từ WSNs
 * 
 */
public class SendUDP {

	private Timer timer;
	private String msg;

	/**
	 * SendUDP constructor
	 * 
	 * @param miniSecond
	 *            nhãn thời gian gửi gói tin UDP đi
	 * @param msg
	 *            thông điệp gửi đi
	 */
	public SendUDP(long miniSecond, String msg) {
		this.msg = msg;
		this.timer = new Timer();
		this.timer.schedule(new SendTCPToRN(), miniSecond);
		System.out.println("Data will send at " + (miniSecond / 60000)
				+ " minute " + (miniSecond % 60000) / 1000 + " second");
	}

	/**
	 * Đóng dữ liệu vào bản tin UDP
	 *
	 */
	class SendTCPToRN extends TimerTask {

		@Override
		public void run() {
			try {
				DatagramSocket udpClient = new DatagramSocket();
				byte[] data = msg.getBytes();
				DatagramPacket packet = new DatagramPacket(data, data.length,
						InetAddress.getByName(Consts.HOST), Consts.UDPPORT);
				udpClient.send(packet);
				System.out.println("=========================");
				System.out.println("[x] Send: " + msg);
				udpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				timer.cancel();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String str = SendUDP.class.getResource(Consts.FILE).getFile();
		BufferedReader br = new BufferedReader(new FileReader(str));
		String line = br.readLine();
		while (line != null) {
			String[] datas = line.split(",");
			int latency = Integer.parseInt(datas[2])
					- Integer.parseInt(datas[1]);
			System.out.println("Latency: " + latency);
			new SendUDP(Long.parseLong(datas[2]), datas[0]);
			line = br.readLine();
		}
		br.close();
	}
}
