package tk.minersonline.FabricHello;

import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogHandler;
import net.fabricmc.loader.impl.util.log.LogLevel;

public class StringLogger implements LogHandler {
	@Override
	public void log(long time, LogLevel level, LogCategory category, String msg, Throwable exc, boolean fromReplay, boolean wasSuppressed) {
		if (!wasSuppressed) {
			System.out.print("["+time+"]");
			System.out.print("["+level.name()+"/"+category.name+"] ");
			System.out.println(msg);
			exc.printStackTrace();
		}
	}

	@Override
	public boolean shouldLog(LogLevel level, LogCategory category) {
		return true;
	}

	@Override
	public void close() {

	}
}
