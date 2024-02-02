package io.github.mitarashi87.othello;

import java.util.ResourceBundle;

public class Config {
	public static final String fileName = "application";
	public static final ResourceBundle bundle = ResourceBundle.getBundle(fileName);
	public static final String stdInEncode = bundle.getString("stdInEncode");
	public static final String host = bundle.getString("host");
	public static final Integer port = Integer.parseInt(bundle.getString("port"));

}
