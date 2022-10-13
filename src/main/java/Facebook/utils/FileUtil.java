package Facebook.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<String> readIds() throws IOException {
        FileReader fileReader = new FileReader("ids.txt");
        List<String> ids = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = "";

            while ((line=bufferedReader.readLine())!=null){
                String privateKey = line.trim();
                if (line.startsWith("0x")){
                    privateKey=line.subSequence(2, privateKey.length()).toString();
                }
                ids.add(privateKey);
            }
        }
        return ids;
    }
}
