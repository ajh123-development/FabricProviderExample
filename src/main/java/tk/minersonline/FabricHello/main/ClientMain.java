package tk.minersonline.FabricHello.main;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.minersonline.FabricHello.providers.MessageProvider;

import java.io.File;

public class ClientMain {
	private static final Logger logger = LoggerFactory.getLogger("HelloWorld");

	public ClientMain() {
	}

	public void run(File runDir) {
		logger.info("Client Started");
		FabricLoaderImpl.INSTANCE.freeze();
		FabricLoaderImpl.INSTANCE.prepareModInit(runDir.toPath(), this);
		EntrypointUtils.invoke("main", ModInitializer.class, ModInitializer::onInitialize);
		EntrypointUtils.invoke("client", ClientModInitializer.class, ClientModInitializer::onInitializeClient);

		for (String str: MessageProvider.print()) {
			logger.info(str);
		}
	}

	public static void main(String[] args) {
		ClientMain main = new ClientMain();
		main.run(new File("."));
	}
}
