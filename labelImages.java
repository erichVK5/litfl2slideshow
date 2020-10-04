
import java.io.*;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.imageio.ImageIO;

public class labelImages {

  public static void main(String[] args) throws Exception {
    File rawData = new File(args[0]);
    Scanner imagesAndLabels = new Scanner(rawData);
    int counter = 0;
    Boolean verbose = true;
    String previousImage = "";
    while (imagesAndLabels.hasNextLine()) {
	String[] data = imagesAndLabels.nextLine().split("\"");
	String imageFile = data[0];
	String imageLabel = data[1];
	File currentImage = new File(imageFile);
	if (imageFile.equals(previousImage)) { // avoid duplication in the list, which seems common
		if (verbose) {
			System.out.println("Skipping processing of duplicate file...");
		}
	} else if (currentImage.exists()) {
		if (verbose) {
			System.out.println("Processing : " + imageFile + "\nwith tag : " + imageLabel);
		}
		BufferedImage image = ImageIO.read(currentImage);
		int origHeight = image.getHeight();
		int origWidth = image.getWidth();
		int textHeight = 35*4;
		BufferedImage newImage = new BufferedImage(origWidth, origHeight + textHeight, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < origWidth; i++) {
			for (int j = 0; j <  origHeight; j++) {
				newImage.setRGB(i,j,image.getRGB(i,j));
			}
		}
		Graphics g = newImage.getGraphics();
		g.setFont(g.getFont().deriveFont(30f));
		int currentTextYCoord = origHeight + textHeight - 35;
		g.drawString(imageLabel, 10, currentTextYCoord);
		String newFile = currentImage.getName();
		currentTextYCoord -= 35;
		g.drawString(newFile, 10, currentTextYCoord);
		currentTextYCoord -= 35;
		g.drawString("Image downloaded from litfl.com", 10, currentTextYCoord);
		g.dispose();
		ImageIO.write(newImage, "png", new File("modified/" + newFile));
		counter++;
		if (false && counter > 300) {
			break;
		}
	} else {
		if (verbose) {
			System.out.println("File not found : " + imageFile);
		}
	}
	previousImage = imageFile;
    }

  }
}
