package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * SaveManager class handles the saving and loading of the contents from file
 */
public class SaveManager {

	/**
	 * saves a serializable data to the given file.
	 * @param data - Serializable data to be saved
	 * @param fileName - Name of the file in which it is to be saved
	 * @throws Exception
	 */
	public static void save(Serializable data, String fileName) throws Exception {
		try(ObjectOutputStream out = new ObjectOutputStream( Files.newOutputStream(Paths.get(fileName)) )) {
			out.writeObject(data);
		}
	}

	/**
	 * loads data from a given file location
	 * @param fileName - name of the file with data to be deserialize.
	 * @return - returns the deserialize object
	 * @throws Exception
	 */
	public static Object load(String fileName) throws Exception {
		try (ObjectInputStream in = new ObjectInputStream( Files.newInputStream(Paths.get(fileName)) )) {
			return in.readObject();
	    }
	}

}
