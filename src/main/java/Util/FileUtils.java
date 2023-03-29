package Util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileUtils {
    public static List<String> readUsers(String file) throws IOException {
        FileReader fileReader = new FileReader(file);
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

    public static String readToken(String file) throws IOException {
        FileReader fileReader = new FileReader(file);
        String token;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            token = bufferedReader.readLine();
            fileReader.close();
        }

        return token;
    }

    public static void saveData(String path, String fileName, Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File f = new File(path);
        if (!f.exists()){
            f.mkdir();
        }
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //FileWriter file = new FileWriter(path+"\\" + fileName + ".json");
        OutputStreamWriter file = new OutputStreamWriter(new FileOutputStream(path+"\\" + fileName + ".json"), StandardCharsets.UTF_8);



        file.write(mapper.writeValueAsString(obj));
        file.flush();
        file.close();
    }

    public static boolean checkValidFile(String path, String uid) throws IOException {
        String url = path+"/"+uid+".json";
        File f = new File(url);
        if (f.exists()){
            Path file = Paths.get(url);
            BasicFileAttributes attr =
                    Files.readAttributes(file, BasicFileAttributes.class);
            long seconds = attr.lastModifiedTime().to(TimeUnit.MILLISECONDS);
            Date date = new Date(seconds);
            Calendar now = Calendar.getInstance();
            long diff = now.getTime().getTime() - date.getTime();//as given
            return TimeUnit.MILLISECONDS.toMinutes(diff)>31;
        }
        return true;
    }
}
