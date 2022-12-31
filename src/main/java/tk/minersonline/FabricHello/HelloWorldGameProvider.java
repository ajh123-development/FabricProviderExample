package tk.minersonline.FabricHello;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.game.minecraft.Slf4jLogHandler;
import net.fabricmc.loader.impl.game.patch.GameTransformer;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.metadata.BuiltinModMetadata;
import net.fabricmc.loader.impl.metadata.ModDependencyImpl;
import net.fabricmc.loader.impl.util.Arguments;
import net.fabricmc.loader.impl.util.LoaderUtil;
import net.fabricmc.loader.impl.util.log.Log;
import tk.minersonline.FabricHello.main.ClientMain;
import tk.minersonline.FabricHello.main.ServerMain;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HelloWorldGameProvider implements GameProvider {

	private EnvType envType;
	private Arguments arguments;
	private final List<Path> gameJars = new ArrayList<>(2); // env game jar and potentially common game jar

	private static final GameTransformer TRANSFORMER = new GameTransformer();

	@Override
	public String getGameId() {
		return "hello_world";
	}

	@Override
	public String getGameName() {
		return "Hello World";
	}

	@Override
	public String getRawGameVersion() {
		return "0.0.1";
	}

	@Override
	public String getNormalizedGameVersion() {
		return "0.0.1";
	}

	@Override
	public Collection<BuiltinMod> getBuiltinMods() {
		BuiltinModMetadata.Builder metadata = new BuiltinModMetadata.Builder(
				getGameId(),
				getNormalizedGameVersion())
				.setName(getGameName())
				.setDescription("Hello World program made to demonstrate the Fabric Loader")
				.setEnvironment(ModEnvironment.UNIVERSAL);

		String version = Runtime.version().toString();

		try {
			metadata.addDependency(new ModDependencyImpl(ModDependency.Kind.DEPENDS, "java", Collections.singletonList(String.format(Locale.ENGLISH, ">=%s", version))));
		} catch (VersionParsingException e) {
			throw new RuntimeException(e);
		}

		return Collections.singletonList(new BuiltinMod(gameJars, metadata.build()));
	}

	@Override
	public String getEntrypoint() {
		return "";
	}

	@Override
	public Path getLaunchDirectory() {
		if (arguments == null) {
			return Paths.get(".");
		}

		return getLaunchDirectory(arguments);
	}

	@Override
	public boolean isObfuscated() {
		return false; // generally no...
	}

	@Override
	public boolean requiresUrlClassLoader() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return System.getProperty("hello.skipHelloProvider") == null;
	}

	@Override
	public boolean locateGame(FabricLauncher launcher, String[] args) {
		this.envType = launcher.getEnvironmentType();
		this.arguments = new Arguments();
		arguments.parse(args);

		gameJars.add(Paths.get(""));
		return true;
	}

	private static Path getLaunchDirectory(Arguments argMap) {
		return Paths.get(argMap.getOrDefault("gameDir", "."));
	}

	@Override
	public void initialize(FabricLauncher launcher) {
		Log.init(new Slf4jLogHandler());
		TRANSFORMER.locateEntrypoints(launcher, gameJars);
	}

	@Override
	public Arguments getArguments() {
		return arguments;
	}

	@Override
	public String[] getLaunchArguments(boolean sanitize) {
		if (arguments == null) return new String[0];

		String[] ret = arguments.toArray();
		if (!sanitize) return ret;

		int writeIdx = 0;

		for (int i = 0; i < ret.length; i++) {
			String arg = ret[i];

			if (i + 1 < ret.length && arg.startsWith("--")) {
				i++; // skip value
			} else {
				ret[writeIdx++] = arg;
			}
		}

		if (writeIdx < ret.length) ret = Arrays.copyOf(ret, writeIdx);

		return ret;
	}

	@Override
	public GameTransformer getEntrypointTransformer() {
		return TRANSFORMER;
	}

	@Override
	public boolean canOpenErrorGui() {
		if (arguments == null || envType == EnvType.CLIENT) {
			return true;
		}

		List<String> extras = arguments.getExtraArgs();
		return !extras.contains("nogui") && !extras.contains("--nogui");
	}

	@Override
	public boolean hasAwtSupport() {
		return LoaderUtil.hasAwtSupport();
	}

	@Override
	public void unlockClassPath(FabricLauncher launcher) {}

	@Override
	public void launch(ClassLoader loader) {
		if (envType == EnvType.CLIENT) {
			ClientMain.main(arguments.toArray());
		} else {
			ServerMain.main(arguments.toArray());
		}
	}
}
