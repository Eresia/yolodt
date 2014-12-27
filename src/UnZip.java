import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {

	List<String> fileList;
	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param outputFolder
	 *            zip file output folder
	 */
	public void unZipIt(String zipFile, String outputFolder) {
		byte[] buffer = new byte[1024];
		try {
			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				if (ze.getName().equals("content.xml")) { //Search for content.xml, and unzip it.
					FileOutputStream fos;
					File newFile = new File(outputFolder + File.separator + ze.getName());
					System.out.println("file unzip : " + newFile.getAbsoluteFile());
					fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) { //Write.
						fos.write(buffer, 0, len);
					}
				fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}