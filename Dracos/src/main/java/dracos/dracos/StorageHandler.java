package dracos.dracos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.JSONException;
import org.json.JSONObject;

public class StorageHandler {
	private String path;
	private File file;
	private String extension;

	/**
	 * Creates a new storage handler for a path. Includes file creation if not
	 * existant at construction.
	 * 
	 * @param p
	 */
	public StorageHandler(String p) {
		path = p;
		file = new File(path);
		try {
			file.createNewFile(); // if file already exists will do nothing
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		extension = "";

		int i = path.lastIndexOf('.');
		if (i > 0) {
			extension = (path.substring(i + 1));
		}
	}

	/**
	 * Appends string to text file.
	 * 
	 * @param str
	 */
	public void appendToTextFile(String str) {
		if (!extension.equals("txt"))
			throw new java.lang.Error("Incorrect file type for this instance! Must be .txt");

		try {
			Files.write(Paths.get(path), str.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes a string to text file.
	 * 
	 * @param data
	 */
	public void saveToTextFile(String data) {
		if (!extension.equals("txt"))
			throw new java.lang.Error("Incorrect file type for this instance! Must be .txt");
		FileOutputStream oFile = null;

		try {
			oFile = new FileOutputStream(file, false);
			// get the content in bytes
			byte[] contentInBytes = data.getBytes();

			oFile.write(contentInBytes);
			oFile.flush();
			oFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null)
					oFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns everything from the file as a String.
	 * 
	 * @return
	 */
	public String getAllFromFile() {
		Path fileLocation = Paths.get(path);
		byte[] data = null;
		try {
			data = Files.readAllBytes(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(data);
	}

	/**
	 * Writes a JSON Object to the file.
	 * 
	 * @param o
	 */
	public void saveJSONData(JSONObject o) {
		if (!extension.equals("json"))
			throw new java.lang.Error("Incorrect file type for this instance! Must be JSON");

		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(file, false);
			String data = o.toString();
			byte[] contentInBytes = data.getBytes();

			oFile.write(contentInBytes);
			oFile.flush();
			oFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null)
					oFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns a JSON Object form the JSON data stored.
	 * 
	 * @return
	 */
	public JSONObject getJSONData() {
		if (!extension.equals("json"))
			throw new java.lang.Error("Incorrect file type for this instance! Must be JSON.");

		String text = getAllFromFile();
		JSONObject json = null;
		try {
			json = new JSONObject(text);
		} catch (JSONException e) {
			Logger.info("JSON Data was empty on load. Should be first time!");
		}

		return json;
	}

	/**
	 * Returns the file extension.
	 * 
	 * @return
	 */
	public String getExtension() {
		return extension;
	}
}
