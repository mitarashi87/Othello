package io.github.mitarashi87.othello;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Config {
	public static final ResourceBundle defaultBundle = ResourceBundle.getBundle("application");
	public static final String fileName = "othello";
	public static final ResourceBundle bundle = generateAndGetOuterBundle();

	public static final String stdInEncode = bundle.getString("stdInEncode");
	public static final String host = bundle.getString("host");
	public static final Integer port = Integer.parseInt(bundle.getString("port"));

	private static ResourceBundle generateAndGetOuterBundle() {
		final var outerConfigPathRaw = "%s.properties".formatted(fileName);
		final var outerConfigPath = Paths.get(outerConfigPathRaw);

		if (!Files.exists(outerConfigPath)) {
			List<String> lines = new ArrayList<>();
			Enumeration<String> keys = defaultBundle.getKeys();
			for (String key : (Iterable<String>) keys::asIterator) {
				String value = defaultBundle.getString(key);
				lines.add("%s=%s".formatted(key, value));
			}

			try {
				Files.write(outerConfigPath, lines);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		try {
			Path dir = outerConfigPath.toAbsolutePath().getParent();
			URLClassLoader urlLoder = new URLClassLoader(new URL[] {dir.toFile().toURI().toURL()});
			return ResourceBundle.getBundle(fileName, Locale.getDefault(), urlLoder);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
