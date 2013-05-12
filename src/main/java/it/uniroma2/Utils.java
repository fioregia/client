package it.uniroma2;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utils {

	public static String getIP() {

		// Replace eth0 with your interface name
		NetworkInterface i = null;
		try {
			i = NetworkInterface.getByName("eth0");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (i != null) {

			Enumeration<InetAddress> iplist = i.getInetAddresses();

			InetAddress addr = null;

			while (iplist.hasMoreElements()) {
				InetAddress ad = iplist.nextElement();
				byte bs[] = ad.getAddress();
				if (bs.length == 4 && bs[0] != 127) {
					addr = ad;
					// You could also display the host name here, to
					// see the whole list, and remove the break.
					break;
				}
			}
			System.out.println(addr.getCanonicalHostName());
			System.out.println(addr.getHostAddress());

			return addr.getHostAddress().replace('.', '-');
		} else
			return "unknownHostAddress";
	}

}
