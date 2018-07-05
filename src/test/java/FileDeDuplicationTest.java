import com.self.technical.test.FileDeDuplication;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FileDeDuplicationTest {


    @Test
    public void getMD5Test() {
        //given
        FileDeDuplication fileDeDuplication = new FileDeDuplication();
        File originalFile = Paths.get("src/test/resources/testwithnosymlinks/file.txt").toFile();

        File copyFile = Paths.get("src/test/resources/testwithnosymlinks/subfolder1/file.txt").toFile();

        String originalMd5 = fileDeDuplication.calculateMd5(originalFile);
        String copyMd5 = fileDeDuplication.calculateMd5(copyFile);

        assertEquals(originalMd5, copyMd5);

    }

    @Test
    public void findDuplicateFiles() {
        //given
        FileDeDuplication fileDeDuplication = new FileDeDuplication();
        fileDeDuplication.findDuplicate("src/test/resources/testwithnosymlinks", false);
        assertEquals(10, fileDeDuplication.getDuplicateFiles().size());

    }

    @Test
    public void findDuplicateFilesWithCircular() {
        //given
        FileDeDuplication fileDeDuplication = new FileDeDuplication();
        fileDeDuplication.findDuplicate("src/test/resources/testwithsymlinks", false);
        assertEquals(10, fileDeDuplication.getDuplicateFiles().size());

    }

    @Test
    public void testFileWithVisitor() {
        FileDeDuplication deDuplication = new FileDeDuplication();
        deDuplication.findDuplicate("src/test/resources/testwithnosymlinks", true);
        assertEquals(10, deDuplication.getDuplicateFiles().size());
    }

    @Test
    public void testWithSymlink(){
        FileDeDuplication deDuplication = new FileDeDuplication();
        deDuplication.findDuplicate("src/test/resources/testwithsymlinks", true);
        assertEquals(10, deDuplication.getDuplicateFiles().size());
    }

    @Test
    public void testWithSymlinkCircular(){
        FileDeDuplication deDuplication = new FileDeDuplication();
        deDuplication.findDuplicate("src/test/resources/testwithsymlinks", true);
        assertEquals(10, deDuplication.getDuplicateFiles().size());
    }
}
