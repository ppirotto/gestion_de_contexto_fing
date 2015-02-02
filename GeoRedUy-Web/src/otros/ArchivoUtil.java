package otros;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.core.util.Base64;

public class ArchivoUtil {

	public static int TAMANO_MAXIMO_ARCHIVO = 1024 * 1024 * 5; // 5mb

	public static String base64encode(File archivo) throws IOException {
		FileInputStream f = new FileInputStream(archivo);
		byte[] bytes = Base64.encode(IOUtils.toByteArray(f));
		f.close();
		return new String(bytes);
	}

	public static String base64encode(byte[] bytes) throws IOException {
		return new String(Base64.encode(bytes));
	}

	public static String base64decode(String archivoCodificado) {
		return new String(Base64.decode(archivoCodificado));
	}

	/**
	 * 
	 * @param ruta
	 * @param contenido
	 * @return true si no hubo errores
	 */
	public static boolean escribirArchivo(String ruta, String contenido) {
		File archivo = new File(ruta);
		FileOutputStream escritor;
		try {
			escritor = new FileOutputStream(archivo);
			escritor.write(contenido.getBytes());
			escritor.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param archivo
	 * @param contenido
	 * @return true si no hubo errores
	 */
	public static boolean escribirArchivo(File archivo, String contenido) {
		FileOutputStream escritor;
		try {
			escritor = new FileOutputStream(archivo);
			escritor.write(contenido.getBytes());
			escritor.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// save to somewhere
	public static void escribirArchivo(byte[] content, String filename)
			throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}

	// save to somewhere
	public static void escribirArchivo(byte[] content, File archivo)
			throws IOException {

		if (!archivo.exists()) {
			archivo.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(archivo);

		fop.write(content);
		fop.flush();
		fop.close();

	}

	public static File archivoTemporal() throws IOException {

		return File.createTempFile(new Long(new Date().getTime()).toString(),
				null);
	}

}
