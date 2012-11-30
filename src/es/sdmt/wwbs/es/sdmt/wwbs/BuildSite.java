package es.sdmt.wwbs;

import java.util.ArrayList;
import java.util.List;

public class BuildSite {
	
	public BuildSite() {}
	
	// Load links stored in files
	public List<String> listloadLinks(String country) {
		
		List<String> itemLinks = new ArrayList<String>();
		ReadWriteTextFileJDK7 text = new ReadWriteTextFileJDK7();
	    
	    //treat as a small file
	    List<String> lines = text.readSmallTextFile(FILE_NAME);
	    log(lines);
		
		
		return null;
		
	}
	
	private List<String> readSmallTextFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
	    return Files.readAllLines(path, ENCODING);
	  }

}
