package Facebook.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<String> readUsers() throws IOException {
        FileReader fileReader = new FileReader("ids.txt");
        List<String> users = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = "";

            while ((line=bufferedReader.readLine())!=null){
                String user = line.trim();
                users.add(user);
            }
        }
        return users;
    }
}
