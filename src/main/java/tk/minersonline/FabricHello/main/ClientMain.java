package tk.minersonline.FabricHello.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.minersonline.FabricHello.providers.MessageProvider;

public class ClientMain {
	private static final Logger logger = LoggerFactory.getLogger("HelloWorld");

	public static void main(String[] args) {
		logger.info("Client Started");
		for (String str: MessageProvider.print()) {
			logger.info(str);
		}
	}
}
