package common.OSUtils;

import common.Log.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dimitar on 10/10/2015.
 */
public class FileSystem {

    public static void deletePath(String Path) throws IOException {
        try {
            File file = new File(Path);
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                file.delete();
            }
            Log.info("Delete " + Path);
        } catch (Exception e) {
            String errorMessage = "Faield to delete " + Path;
            Log.fatal(errorMessage);
            throw new IOException(errorMessage);
        }
    }

    public static void makeDir(String Path) {
        File file = new File(Path);

        if (file.exists()) {
            Log.debug("Path " + Path + " already exists.");
        } else {
            file.mkdirs();
            Log.debug("Path  " + Path + " created.");
        }
    }
}