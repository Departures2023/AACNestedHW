import AssociativeArray.AssociativeArray;
import AssociativeArray.KeyNotFoundException;
import AssociativeArray.NullKeyException;
import java.util.NoSuchElementException;

/**
 * Represents the mappings for a single category of items that should be displayed
 * 
 * @author Catie Baker & Kevin Tang
 *
 */
public class AACCategory implements AACPage {

	private String name;
	private AssociativeArray<String, String> imageArray;

	/**
	 * Creates a new empty category with the given name
	 * 
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.name = name;
		this.imageArray = new AssociativeArray<String, String>();
	}

	/**
	 * Adds the image location, text pairing to the category
	 * 
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
			this.imageArray.set(imageLoc, text);
		} catch (NullKeyException | KeyNotFoundException e) {
		}
	}

	/**
	 * Returns an array of all the images in the category
	 * 
	 * @return the array of image locations; if there are no images, it should return an empty array
	 */
	public String[] getImageLocs() {
		String[] images = new String[this.imageArray.size()];
		for (int i = 0; i < this.imageArray.size(); i++) {
			images[i] = this.imageArray.getElement(i).getKey();
		}
		return images;
	}

	/**
	 * Returns the name of the category
	 * 
	 * @return the name of the category
	 */
	public AssociativeArray<String, String> getImageArray() {
		return this.imageArray;
	}

	/**
	 * Returns the name of the category
	 * 
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.name;
	}

	/**
	 * Returns the text associated with the given image in this category
	 * 
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current category
	 */
	public String select(String imageLoc) throws KeyNotFoundException {
		if (this.hasImage(imageLoc)) {
			return this.imageArray.get(imageLoc);
		}
		throw new KeyNotFoundException();
	}

	/**
	 * Determines if the provided images is stored in the category
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.imageArray.hasKey(imageLoc);
	}
}
