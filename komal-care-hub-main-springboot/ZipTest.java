import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.io.File;
import java.io.FileInputStream;

public class ZipTest {
    public static void main(String[] args) throws Exception {
        File file = new File("G:/komal-care-hub/komal-care-hub-main-springboot/src/main/resources/data/indian_medicine_data.zip");
        try (InputStream is = new FileInputStream(file);
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry = zis.getNextEntry();
            if (entry == null) {
                System.out.println("ENTRY IS NULL");
            } else {
                System.out.println("ENTRY FOUND: " + entry.getName());
            }
        }
    }
}
