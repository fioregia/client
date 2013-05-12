package it.uniroma2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WorkloadAnalisys {

	private String homeDir;

	public WorkloadAnalisys(String homeDir) {
		this.homeDir = homeDir;
	}

	public File[] list() {
		File homeDirFile = new File(homeDir);
		return homeDirFile.listFiles(new FilenameFilter() {

			public boolean accept(File arg0, String name) {
				return name.endsWith(".jpg");
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WorkloadAnalisys workloadAnalisys = new WorkloadAnalisys("XX");
		File[] fileList = workloadAnalisys.list();
		double count = 0;
		double totalSize = 0;
		for (File file : fileList) {
			// Delete not jpg
			if (!file.getName().endsWith(".jpg"))
				file.delete();
			// Delete unreadable file
			try {
				BufferedImage cat = ImageIO.read(file);
				if (cat == null)
					file.delete();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			double filesize = file.length();
			double filesizeInKB = filesize / 1024;
			count++;
			totalSize += filesizeInKB;
			System.out.println(filesizeInKB + " KB - " + file.getName());
		}
		System.out.println("Count: " + count);
		System.out.println("Avg Size: " + totalSize / count);

	}

}
