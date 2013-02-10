package it.uniroma2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WorkloadAnalisys {

	private final String home = "/Users/pandriani/Desktop/imgs";
	
	
	public File[] list(){
		File homeDir = new File(home);
		return homeDir.listFiles();
	} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WorkloadAnalisys workloadAnalisys = new WorkloadAnalisys();
		File[] fileList = workloadAnalisys.list();
		double count = 0;
		double totalSize = 0;
		for (File file : fileList) {
			//Delete not jpg
			if(!file.getName().endsWith(".jpg"))
				file.delete();
			//Delete unreadable file
			try {
				BufferedImage cat = ImageIO.read(file);
				if(cat==null)
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
		System.out.println("Avg Size: " + totalSize/count);

	}

}
