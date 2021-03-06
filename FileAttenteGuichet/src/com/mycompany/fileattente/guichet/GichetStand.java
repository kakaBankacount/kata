package com.mycompany.fileattente.guichet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GichetStand {
	
	private static boolean readingRunning = true;

	public static void main(String[] params) {

		System.out.println("=======================");
		System.out.println("     Guichet stand     ");
		System.out.println("=======================");

		if (params == null || params.length < 1) {
			System.out.println("Expeted parameter: Service");
		}
		String service = params[0];
		
		System.out.println("Service: " + service);
		
		Socket socket;
		BufferedReader in;
		PrintWriter out;
		try {
			socket = new Socket(InetAddress.getLocalHost(), 2009);
			System.out.println("Demande de connexion");

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			
			out.println(service);
			out.flush();
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					Scanner sc = new Scanner(System.in);
					while (true) {
						System.out.println("Votre message :");
						String message = sc.nextLine();

						if (message != null && message.toUpperCase().equals("END")) {
							break;
						}
						out.println(message);
						out.flush();
					}
					if (sc != null) {
						sc.close();
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					GichetStand.readingRunning = false;
				}

			}).start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (GichetStand.readingRunning) {
						try {
							String message = in.readLine();
							System.out.println("From server: " + message);

						} catch (IOException e) {
							e.printStackTrace();
							if (in != null) {
								try {
									in.close();
									break;
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
				}

			}).start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
