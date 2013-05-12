package it.uniroma2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Workload {

	private String homeDir;
	private File[] list;
	private int count = 0;
	private Random r = new Random(1001); // fix this


	public Workload(String homeDir) {
		this.homeDir = homeDir;
		preAnalysis(list());
		this.list = list();
	}
	
	public File pick(int index){
		return list[index];
	}
	
	public File randomPick(){
		int index = r.nextInt(count);
		return pick(index);
	}

	private File[] list() {
		File homeDirFile = new File(homeDir);
		return homeDirFile.listFiles(new FilenameFilter() {

			public boolean accept(File arg0, String name) {
				return name.endsWith(".jpg");
			}
		});
	}

	private void preAnalysis(File[] fileList) {
		//double count = 0;
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Workload workload = new Workload("/Users/pandriani/Desktop/imgs");
		
		for(int i = 0; i< 20; i++){
			System.out.println(workload.randomPick().getName());
		}
		

	}

}
