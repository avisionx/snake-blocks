package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveManager {
	
	public static void save(Serializable data, String fileName) throws Exception {
		try(ObjectOutputStream out = new ObjectOutputStream( Files.newOutputStream(Paths.get(fileName)) )) {
			out.writeObject(data);
		}
	}

	public static Object load(String fileName) throws Exception {
		try (ObjectInputStream in = new ObjectInputStream( Files.newInputStream(Paths.get(fileName)) )) {
			return in.readObject();
	    }
	}

}
