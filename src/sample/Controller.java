package sample;

import com.sun.deploy.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kotlinPackage.Decryptor;
import kotlinPackage.Encryptor;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private boolean deleteFile = false;

    private static final int KEY_LENGTH = 8;

    private File file;

    private File workingDirectory = new File("C:\\Users\\lenovo\\Desktop\\java\\STUDIA\\BiOD\\Cryptor V8 GT Pro\\src\\");

    private String key;

    private static Charset charset = Charset.forName("UTF-8");

    StringUtils stringUtils = new StringUtils();

    @FXML
    private Button buttonBrowse;

    @FXML
    private Button buttonEncrypt;

    @FXML
    private Button buttonDecrypt;

    @FXML
    private TextField textViewFile;

    @FXML
    private PasswordField textViewPassword;

    @FXML
    private Label labelInfo;


    @FXML
    private void locateFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Wybierz plik do szyfrowania");
        chooser.setInitialDirectory(workingDirectory);
        file = chooser.showOpenDialog(new Stage());
        textViewFile.setText(file.getAbsolutePath());
    }

    @FXML
    private void createInfoBox(){

        String infoMessage =
                "- Plik z tekstem jawnym zostanie usunięty po szyfrowaniu\n" +
                "- Plik z szyfrogramem pojawi się w ścieżce pliku z tekstem jawnym";
        String titleBar = "Informacje o programie";

        InfoBox.infoBox(infoMessage, titleBar);
    }

    @FXML
    private void performEncryption(){

        key = obtainKey();

        String validatedKey = validateKey(key);
        boolean isKeyValid = validatedKey.length()==KEY_LENGTH;
        boolean isFile = validateFile();


        if (isKeyValid && isFile){
            labelInfo.setText("");

            encrypt(file, validatedKey);
            if (deleteFile) file.delete();
            file = null;

            labelInfo.setTextFill(Color.GREEN);
            labelInfo.setText("Szyfrowanie przebiegło pomyślnie.");
            textViewFile.setText("");
            textViewPassword.setText("");

        }

    }

    private void encrypt(File file, String key){

        try {
            Reader reader = new InputStreamReader(new FileInputStream(file), charset);
            Writer writer = new OutputStreamWriter(new FileOutputStream(workingDirectory.getAbsolutePath() + "\\ciphertext.txt"), charset);
            List<Character> chars = new ArrayList<>();
            int tempInt;
            while ((tempInt = reader.read()) != -1) {

                char tempChar = (char)tempInt;
                chars.add(tempChar);
                if (chars.size() == 24){
                    encryptToFile(key, writer, chars);
                }
            }
            if (chars.size()!=24){
                int diff = 24-chars.size();
                for (int i = 0; i < diff; i++) {
                    chars.add(' ');
                }
                encryptToFile(key, writer, chars);
            }

            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void encryptToFile(String key, Writer writer, List<Character> chars) throws IOException {

        StringBuilder sb = new StringBuilder();
        for (Character ch: chars) {
            sb.append(ch);
        }

        String text = sb.toString();
        Encryptor encryptor = new Encryptor(text, key);
        String result = encryptor.encrypt();
        writer.write(result);
    }

    private void decryptToFile(String key, Writer writer, List<Character> chars) throws IOException {

        StringBuilder sb = new StringBuilder();
        for (Character ch: chars) {
            sb.append(ch);
        }

        String text = sb.toString();
        Decryptor decryptor = new Decryptor(text, key);
        String result = decryptor.decrypt();
        if (result.matches("\\s*$")){
            boolean tr = true;
        }
        writer.write(result);
    }


    @FXML
    private void performDecryption(){

        key = obtainKey();

        String validatedKey = validateKey(key);
        boolean isKeyValid = validatedKey.length()==KEY_LENGTH;
        boolean isFile = validateFile();


        if (isKeyValid && isFile){
            labelInfo.setText("");

            decrypt(validatedKey);
            file = null;

            labelInfo.setTextFill(Color.GREEN);
            labelInfo.setText("Deszyfrowanie przebiegło pomyślnie.");
            textViewFile.setText("");
            textViewPassword.setText("");
        }

    }

    private void decrypt(String key){


        try {
            Reader reader = new InputStreamReader(new FileInputStream(file), charset);
            Writer writer = new OutputStreamWriter(new FileOutputStream(workingDirectory + "\\decrypted.txt"), charset);

            List<Character> chars = new ArrayList<>();
            int tempInt;
            while ((tempInt = reader.read()) != -1) {

                char tempChar = (char)tempInt;
                chars.add(tempChar);
                if (chars.size() == 24){
                    decryptToFile(key, writer, chars);
                }
            }
            if (chars.size()!=24){
                int diff = 24-chars.size();
                for (int i = 0; i < diff-1; i++) {
                    chars.add((char)0);
                }
                decryptToFile(key, writer, chars);
            }

            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFile() {

        if (file == null){
            labelInfo.setTextFill(Color.RED);
            labelInfo.setText("Nie wybrano pliku.");
            return false;
        }
        else return true;
    }

    private String validateKey(String key) {

        int length = key.length();

        if (length < KEY_LENGTH){
            labelInfo.setTextFill(Color.RED);
            labelInfo.setText("Klucz powinien mieć 8 znaków.");
        }
        else if (length >= KEY_LENGTH){
            key = key.substring(0, KEY_LENGTH);

        }

        return key;
    }

    private String obtainKey() {

        String key = null;

        try{
            key = textViewPassword.getText();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return key;
    }
}
