package imageManipulations;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logger.BaseLogger;
import main.Database;
import user.User;
import user.UserType;

/**
 * ImageMatrix is used to represent images.
 * 
 * @author osman.yasal
 *
 */
public class ImageMatrix implements Serializable {

	private String id;
	private int[][] img;

	private int width;
	private int height;

	/**
	 * Builds up ImageMatrix from BufferedImage
	 * 
	 * @param BufferedImage image
	 * @see BufferedImage
	 */
	public ImageMatrix(BufferedImage image) {
		this.id = UUID.randomUUID().toString();
		this.img = convertImageToPixelArray(image);
		this.width = img.length;
		this.height = img[0].length;
	}

	public ImageMatrix(int width, int height) {
		this.id = UUID.randomUUID().toString();
		this.img = generateEmptyImageArray(width, height);
		this.width = img.length;
		this.height = img[0].length;

	}

	private int[][] generateEmptyImageArray(int width, int height) {
		return new int[width][height];
	}

	private int[][] convertImageToPixelArray(BufferedImage image) {
		if (image == null) {
            JOptionPane.showMessageDialog(null, "This is not an image!", "Error", JOptionPane.ERROR_MESSAGE);
            BaseLogger.error().log("User tried to filter a file that is not an image!");


	        throw new IllegalArgumentException("Image cannot be null");

	    }
		int[][] im = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				im[i][j] = image.getRGB(i, j);
			}
		return im;
	}
	/**
	 * This method applies a blur filter to the image.
	 * 
	 * @return A new ImageMatrix that is a blurred version of the original.
	 * The offset is calculated based on the blurAmount. With offset = blurAmount,
	 *  if you increase the blurAmount, you increase the offset, thereby increasing
	 *   the size of the pixel window the blur is calculated over. For instance, 
	 *   if blurAmount is 1, offset will also be 1, and you'll look at a window of 3x3 pixels
	 *    around the current pixel (from -1 to +1 in both x and y directions). If blurAmount is 2,
	 *     offset will be 2, and you'll look at a window of 5x5 pixels (from -2 to +2).
	 *      And so on. Therefore, blurAmount indirectly determines the "window size" or 
	 *      kernel size of the blur operation. "reference"
	 */
	public ImageMatrix applyBlurFilter(int blurRange) {
	    int[][] modifiedImageArray = generateEmptyImageArray(width, height);
	    
	    int padding = blurRange;

	    for (int a = 0; a < width; a++) {
	        for (int b = 0; b < height; b++) {
	            int redSum = 0;
	            int greenSum = 0;
	            int blueSum = 0;
	            int pixelCount = 0;
	            
	            for (int horizontalShift = -padding; horizontalShift <= padding; horizontalShift++) {
	                for (int verticalShift = -padding; verticalShift <= padding; verticalShift++) {
	                    int shiftedX = a + horizontalShift;
	                    int shiftedY = b + verticalShift;
	                    
	                    if (shiftedX >= 0 && shiftedX < width && shiftedY >= 0 && shiftedY < height) {
	                        redSum += getRed(shiftedX, shiftedY);
	                        greenSum += getGreen(shiftedX, shiftedY);
	                        blueSum += getBlue(shiftedX, shiftedY);
	                        pixelCount++;
	                    }
	                }
	            }
	            
	            int meanRed = redSum / pixelCount;
	            int meanGreen = greenSum / pixelCount;
	            int meanBlue = blueSum / pixelCount;
	            
	            modifiedImageArray[a][b] = convertRGB(meanRed, meanGreen, meanBlue);
	        }
	    }
	    
	    ImageMatrix modifiedImageMatrix = new ImageMatrix(width, height);
	    modifiedImageMatrix.img = modifiedImageArray;  
	    
	    return modifiedImageMatrix;
	}
	/**
	 * This method applies a sharpen filter to the image. Which basically makes the details,
	 * more "detail", caution, making the sharpen higher than required will make the image 
	 * worse!
	 * 
	 * @return A new ImageMatrix that is a sharpened version of the original.
	 */
	public ImageMatrix applySharpenFilter(int blurExtent) {
	    ImageMatrix blurredImageMatrix = this.applyBlurFilter(blurExtent);
	    int[][] updatedImageArray = generateEmptyImageArray(width, height);
	    
	    for (int i = 0; i < width; i++) {
	        for (int j = 0; j < height; j++) {
	            int primaryRed = getRed(i, j);
	            int primaryGreen = getGreen(i, j);
	            int primaryBlue = getBlue(i, j);
	            
	            int blurredRed = blurredImageMatrix.getRed(i, j);
	            int blurredGreen = blurredImageMatrix.getGreen(i, j);
	            int blurredBlue = blurredImageMatrix.getBlue(i, j);
	            
	            int redDetail = primaryRed - blurredRed;
	            int greenDetail = primaryGreen - blurredGreen;
	            int blueDetail = primaryBlue - blurredBlue;
	            
	            int sharpenedRed = primaryRed + redDetail;
	            int sharpenedGreen = primaryGreen + greenDetail;
	            int sharpenedBlue = primaryBlue + blueDetail;
	            
	            // Keeping color values within range 0 - 255
	            sharpenedRed = Math.min(255, Math.max(0, sharpenedRed));
	            sharpenedGreen = Math.min(255, Math.max(0, sharpenedGreen));
	            sharpenedBlue = Math.min(255, Math.max(0, sharpenedBlue));
	            
	            updatedImageArray[i][j] = convertRGB(sharpenedRed, sharpenedGreen, sharpenedBlue);
	        }
	    }
	    
	    ImageMatrix updatedImageMatrix = new ImageMatrix(width, height);
	    updatedImageMatrix.img = updatedImageArray;  
	    
	    return updatedImageMatrix;
	}
	/**
	 * This method applies a grayscale filter to the image.
	 * 
	 * @param modifier A value between 0 and 1 that determines the intensity of the grayscale filter.
	 * @return A new ImageMatrix that is a grayscale version of the original.
	 */
	public ImageMatrix applyGrayscaleFilter(double modifier) {
	    int[][] updatedImageArray = generateEmptyImageArray(width, height);

	    for (int i = 0; i < width; i++) {
	        for (int j = 0; j < height; j++) {
	            Color pixelColor = new Color(this.img[i][j]);
	            int primaryRed = pixelColor.getRed();
	            int primaryGreen = pixelColor.getGreen();
	            int primaryBlue = pixelColor.getBlue();

	            double grayTone = 0.2126 * primaryRed + 0.7152 * primaryGreen + 0.0722 * primaryBlue;

	            int updatedRed = clampColorValue((int)(primaryRed * (1 - modifier) + grayTone * modifier));
	            int updatedGreen = clampColorValue((int)(primaryGreen * (1 - modifier) + grayTone * modifier));
	            int updatedBlue = clampColorValue((int)(primaryBlue * (1 - modifier) + grayTone * modifier));

	            updatedImageArray[i][j] = new Color(updatedRed, updatedGreen, updatedBlue).getRGB();
	        }
	    }

	    ImageMatrix updatedImageMatrix = new ImageMatrix(width, height);
	    updatedImageMatrix.img = updatedImageArray;  

	    return updatedImageMatrix;
	}

	private int clampColorValue(int colorValue) {
	    return Math.min(255, Math.max(0, colorValue));
	}
	/**
	 * This method applies an edge detection filter to the image.
	 * 
	 * @return A new ImageMatrix that highlights the edges of the original image.
	 */
	public ImageMatrix applyEdgeDetectionFilter() {
	    ImageMatrix grayscaleBlurMatrix = this.applyGrayscaleFilter(1.0).applyBlurFilter(1);
	    
	    int[][] revisedImgMatrix = generateEmptyImageArray(width, height);

	    int[][] sobelOperatorX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
	    int[][] sobelOperatorY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

	    IntStream.range(1, width - 1).forEach(x -> {
	        IntStream.range(1, height - 1).forEach(y -> {
	            int gradX = 0, gradY = 0;
	            
	            for (int dx = -1; dx <= 1; dx++) {
	                for (int dy = -1; dy <= 1; dy++) {
	                    int nx = x + dx, ny = y + dy;
	                    int grayscaleVal = grayscaleBlurMatrix.getRed(nx, ny);
	                    
	                    gradX += grayscaleVal * sobelOperatorX[dx+1][dy+1];
	                    gradY += grayscaleVal * sobelOperatorY[dx+1][dy+1];
	                }
	            }
	            
	            int gradientMagnitude = (int) Math.hypot(gradX, gradY);
	            gradientMagnitude = Math.max(0, Math.min(255, gradientMagnitude));
	            revisedImgMatrix[x][y] = convertRGB(gradientMagnitude, gradientMagnitude, gradientMagnitude);
	        });
	    });

	    ImageMatrix updatedImgMatrix = new ImageMatrix(width, height);
	    updatedImgMatrix.img = revisedImgMatrix;
	    return updatedImgMatrix;
	}
	/**
	 * This method adjusts the contrast of the image.
	 * 
	 * @param  A value that determines the amount of Brightness to add to the image. Positive values increase contrast, while negative values decrease contrast.
	 * @return A new ImageMatrix with the Brightness adjusted.
	 */
	public ImageMatrix applyBrightnessFilter(double modifier) {
	    int[][] newImgArray = generateEmptyImageArray(width, height);
	    
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            int originalRed = getRed(x, y);
	            int originalGreen = getGreen(x, y);
	            int originalBlue = getBlue(x, y);
	            
	            // Increase the RGB values by the modifier and clamp between 0 and 255
	            int newRed = Math.min(255, (int) (originalRed + 255 * modifier));
	            int newGreen = Math.min(255, (int) (originalGreen + 255 * modifier));
	            int newBlue = Math.min(255, (int) (originalBlue + 255 * modifier));
	            
	            newImgArray[x][y] = convertRGB(newRed, newGreen, newBlue);
	        }
	    }

	    ImageMatrix newImgMatrix = new ImageMatrix(width, height);
	    newImgMatrix.img = newImgArray;  

	    return newImgMatrix;
	}
	/**
	 * This method adjusts the contrast of the image.
	 * 
	 * @param contrast A value that determines the amount of contrast to add to the image. Positive values increase contrast, while negative values decrease contrast.
	 * @return A new ImageMatrix with the contrast adjusted.
	 */
	public ImageMatrix applyContrastFilter(double contrastLevel) {
	    int[][] modifiedImageMatrix = generateEmptyImageArray(width, height);
	    double contrastAdjustment = (259.0 * (contrastLevel + 255.0)) / (255.0 * (259.0 - contrastLevel));

	    for (int col = 0; col < width; col++) {
	        for (int row = 0; row < height; row++) {
	            int initialRed = getRed(col, row);
	            int initialGreen = getGreen(col, row);
	            int initialBlue = getBlue(col, row);

	            modifiedImageMatrix[col][row] = convertRGB(
	                adjustContrastForColor(initialRed, contrastAdjustment),
	                adjustContrastForColor(initialGreen, contrastAdjustment),
	                adjustContrastForColor(initialBlue, contrastAdjustment)
	            );
	        }
	    }

	    ImageMatrix contrastAdjustedMatrix = new ImageMatrix(width, height);
	    contrastAdjustedMatrix.img = modifiedImageMatrix;  

	    return contrastAdjustedMatrix;
	}

	private int adjustContrastForColor(int colorValue, double contrastAdjustment) {
	    int adjustedColorValue = (int) (contrastAdjustment * (colorValue - 128) + 128);
	    return Math.max(0, Math.min(255, adjustedColorValue));
	}

	public BufferedImage getBufferedImage() {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				bufferedImage.setRGB(i, j, img[i][j]);
			}
		}
		return bufferedImage;
	}

	public String getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getRGB(int x, int y) {
		return img[x][y];
	}

	public int setRGB(int x, int y, int rgb) {
		img[x][y] = rgb;
		return rgb;
	}

	public int getRed(int x, int y) {
		return (img[x][y] >> 16) & 0xFF;
	}

	public int getGreen(int x, int y) {
		return (img[x][y] >> 8) & 0xFF;
	}

	public int getBlue(int x, int y) {
		return img[x][y] & 0xFF;
	}

	public static int convertRGB(int red, int green, int blue) {
		return (red << 16 | green << 8 | blue);
	}
}
