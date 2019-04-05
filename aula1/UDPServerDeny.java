// created on 29/09/2010 at 22:33

// Servidor com black list
import java.net.*;
import java.util.ArrayList;

class UDPServerDeny {
	public static void main(String args[]) throws Exception {
		// BlackList
		ArrayList<InetAddress> blackList = new ArrayList<>();
		blackList.add(InetAddress.getByName("localhost"));
		//Create server socket
		DatagramSocket serverSocket = new DatagramSocket(9876);

		while(true) {
			byte[] receiveData = new byte[1024];
			byte[] sendData;
			//block until packet is sent by client
			DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivedPacket);
			//Get the information about the datagram of the client
			InetAddress IPAddress = receivedPacket.getAddress();
			int port = receivedPacket.getPort();
			if (!blackList.contains(IPAddress)) {
				//Get the data of the packet
				String sentence = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
				System.out.println("RECEIVED FROM CLIENT "+IPAddress.getHostAddress()+": " + sentence);
				//Change the data to capital letters
				String capitalizedSentence = sentence.toUpperCase();
				sendData = new byte[sentence.length()];
				sendData = capitalizedSentence.getBytes();
			}
			else {
				String denied = "CONNECTION DENIED";
				System.out.println("CLIENT DENIED: "+IPAddress.getHostAddress());
				sendData = new byte[denied.length()];
				sendData = denied.getBytes();
			}
			//Send back the response to the client
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}
