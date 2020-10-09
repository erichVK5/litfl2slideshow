
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
    Boolean addFilenameLabel = false;
    String previousImage = "";
    String licencedLogo = "cc-by-nc-428x64.png";
    int logoHeight = 64;
    int logoWidth = 428;
    int fontHeight = 35;
    int textHeight = fontHeight * 4;
    BufferedImage logo = ImageIO.read(new File(licencedLogo));
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
		int imgWidth = origWidth;
		if (origWidth < 900) {
			imgWidth = 900;
		}
		int imgOffset = (imgWidth - origWidth)/2;
		BufferedImage newImage = new BufferedImage(imgWidth, origHeight + textHeight, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < origWidth; i++) {
			for (int j = 0; j <  origHeight; j++) {
				newImage.setRGB(i + imgOffset,j,image.getRGB(i,j));
			}
		}
		for (int i = 0; i < logoWidth; i++) {
			for (int j = 0; j <  logoHeight; j++) {
				newImage.setRGB(imgWidth - logoWidth + i, origHeight + textHeight - logoHeight + j, logo.getRGB(i,j));
			}
		}
		Graphics g = newImage.getGraphics();
		g.setFont(g.getFont().deriveFont(30f));
		int currentTextYCoord = origHeight + textHeight - (fontHeight * 5)/2;
		g.drawString(imageLabel, 10, currentTextYCoord);
		String newFile = currentImage.getName();
		if (newFile.endsWith(".jpg") || newFile.endsWith(".JPG")
 			|| newFile.endsWith(".gif") || newFile.endsWith(".GIF")) {
				newFile = newFile.substring(0,newFile.length()-4);
		} else if (newFile.endsWith(".JPEG") || newFile.endsWith(".jpeg")) {
				newFile = newFile.substring(0,newFile.length()-5);
		}
		if (addFilenameLabel) {
			currentTextYCoord += fontHeight * 2;
			g.drawString(newFile, 10, currentTextYCoord);
		}
		g.dispose();
		ImageIO.write(newImage, "png", new File("modified/" + newFile + ".png"));
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
