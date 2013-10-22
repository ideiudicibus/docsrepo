package docsrepo;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class TestFileDirCreation {

	@Test
	public void testFileDirCreation() {
		
		
		String destination=File.separatorChar+"test"+File.separatorChar+"webdav"+File.separatorChar+"test"+File.separatorChar +"test"+File.separatorChar+"test"+File.separatorChar;
		File theDir = new File(destination);
		if(!theDir.exists()) theDir.mkdirs();
		
		theDir.delete();
		
	}

}
