package es.sdmt.wwbs;


public class Item {
	
	private String ASIN;
	private String title;
	private String author;
	
	public Item() {}

	public String getTitle() {

	    return title;
    }

	public void setTitle(String title) {

	    this.title = title;
    }

	public String getAuthor() {

	    return author;
    }

	public void setAuthor(String author) {

	    this.author = author;
    }

	public String getASIN() {

	    return ASIN;
    }

	public void setASIN(String aSIN) {

	    ASIN = aSIN;
    }

}
