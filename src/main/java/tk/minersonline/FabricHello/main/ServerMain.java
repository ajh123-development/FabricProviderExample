package tk.minersonline.FabricHello.main;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.minersonline.FabricHello.providers.MessageProvider;

import java.io.File;

public class ServerMain {
	private static final Logger logger = LoggerFactory.getLogger("HelloWorld");

	public void run(File runDir) {
		logger.info("Server Started");
		FabricLoaderImpl.INSTANCE.freeze();
		FabricLoaderImpl.INSTANCE.prepareModInit(runDir.toPath(), this);
		EntrypointUtils.invoke("main", ModInitializer.class, ModInitializer::onInitialize);
		EntrypointUtils.invoke("server", ClientModInitializer.class, ClientModInitializer::onInitializeClient);

		for (String str: MessageProvider.print()) {
			logger.info(str);
		}
	}

	public static void main(String[] args) {
		ServerMain main = new ServerMain();
		main.run(new File("."));
	}
}
