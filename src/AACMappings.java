import AssociativeArray.AssociativeArray;
import AssociativeArray.KeyNotFoundException;
import AssociativeArray.NullKeyException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Kevin Tang
 *
 */
public class AACMappings implements AACPage {

	private String categoryName;
  private AACCategory homeScreen;
  private AACCategory category;
  private AssociativeArray<String, AACCategory> allCategoryArray;
  private AssociativeArray<String, String> referenceArray;
	
	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) throws Exception {
    File file = new File(filename);
    Scanner scanner = new Scanner(file);
    String input = scanner.nextLine();
		this.homeScreen = new AACCategory("homeScreen");
		this.referenceArray = new AssociativeArray<String, String>();
    this.allCategoryArray = new AssociativeArray<String, AACCategory>();

    while (input != null) {
      if (input.startsWith(">")) {
        String[] elements = input.substring(1).trim().split(" ", 2);
        String imgLoc = elements[0];
        String textToSpeak = elements[1];
        this.category.addItem(imgLoc, textToSpeak);
        allCategoryArray.set(this.categoryName, this.category);
    } else {
        String[] elements = input.trim().split(" ", 2);
        String imgLoc = elements[0];
        String currCat = elements[1];
        this.homeScreen.addItem(imgLoc, currCat);
        this.category = new AACCategory(currCat);
        this.allCategoryArray.set(currCat, this.category);

				this.referenceArray.set(imgLoc, currCat);
        this.categoryName = currCat;
    }

    if (scanner.hasNextLine()){
      input = scanner.nextLine();
    } else {
      input = null;
    }
  }
    scanner.close();
    this.categoryName = "";
    this.category = this.homeScreen;
	}
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws KeyNotFoundException 
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) throws KeyNotFoundException {
		if (this.categoryName.equals("")) {
      this.categoryName = this.referenceArray.get(imageLoc);
      this.category = this.allCategoryArray.get(this.categoryName);
      return this.categoryName;
    }
    return this.category.select(imageLoc);
	}
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		if (this.categoryName.equals("")) {
      return this.homeScreen.getImageLocs();
    } else {
    return this.category.getImageLocs();
		}
	}
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		this.categoryName = "";
    this.category = this.homeScreen;
	}
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) throws IOException {
    FileWriter writer = new FileWriter(filename);
    for (int i = 0; i < this.referenceArray.size(); i++) {
			String exPath = this.referenceArray.getElement(i).getKey();
      String exName = this.referenceArray.getElement(i).getVal();
      writer.write(exPath + " " + exName + "\n");
			try{
      for (int j = 0; j < this.allCategoryArray.get(exName).getImageArray().size(); j++) {
				String inPath = this.allCategoryArray.get(exName).getImageArray().getElement(j).getKey();
        String inName = this.allCategoryArray.get(exName).getImageArray().getElement(j).getVal();
				writer.write(">" + inPath + " " + inName + "\n");
			}
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		}
    }
    writer.close();
	}
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 * @throws KeyNotFoundException 
	 * @throws NullKeyException 
	 */
	public void addItem(String imageLoc, String text) throws NullKeyException, KeyNotFoundException {
		if (this.categoryName.equals("")){
			this.homeScreen.addItem(imageLoc, text);
      this.referenceArray.set(imageLoc, text);
      this.category = new AACCategory(text);
      this.allCategoryArray.set(text, this.category);
    } else {
      this.category.addItem(imageLoc, text);
      allCategoryArray.set(this.categoryName, this.category);
    }
	}


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
		return this.categoryName;
	}


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.referenceArray.hasKey(imageLoc);
	}
}
