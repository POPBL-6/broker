package connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/**
 * Factory class for classes that extend SocketImplementation and
 * implement the getInstance(String[]) method.
 * 
 * @author Jon Ayerdi
 */
public abstract class SocketImplementationFactory {
	/**
	 * Creates and returns a SocketImplementation object.
	 * If the package is not specified, connection will be assumed.
	 * 
	 * @param args The configuration for the SocketImplementation.
	 * @return The newly created SocketImplementation.
	 * @throws Throwable The exception thrown when instantiating the SocketImplementation.
	 */
	public static SocketImplementation getSocketImplementation(String[] args) throws Throwable {
		SocketImplementation socketImplementation = null;
		try {
			if(args[0].equals("file")) {
				return getSocketImplementation(getConfigurationFromFile(args[1]));
			}
			else {
				if(!args[0].contains(".")) {
					args[0] = "connection." + args[0];
				}
				try {
					socketImplementation = SocketImplementation.class.cast(Class.forName(args[0])
							.getMethod("getInstance", String[].class).invoke(null, new Object[] {args}));
				}catch(Exception e) {
					throw e;
				}
			}
		}catch(InvocationTargetException e) {
			throw e.getCause();
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getClass().getName()+":"+e.getMessage());
		}
		return socketImplementation;
	}
	
	/**
	 * Reads and returns the first line of the specified file.
	 * @param filename
	 * @return The configuration read from the file.
	 * @throws FileNotFoundException
	 */
	public static String[] getConfigurationFromFile(String filename) throws FileNotFoundException {
		Scanner s = new Scanner(new FileInputStream(filename));
		String config = s.nextLine();
		s.close();
		return config.split("[ ]");
	}
}
