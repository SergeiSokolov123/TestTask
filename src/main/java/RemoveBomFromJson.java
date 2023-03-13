import java.io.*;

public class RemoveBomFromJson {
    public static void main(String[] args) {
        try {
            String filePath = "tickets.json";
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] data = new byte[(int) new File(filePath).length()];
            fileInputStream.read(data);
            fileInputStream.close();
            String jsonContent = new String(data, "UTF-8");
            if (jsonContent.startsWith("\uFEFF")) {
                jsonContent = jsonContent.substring(1);
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                fileOutputStream.write(jsonContent.getBytes("UTF-8"));
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
