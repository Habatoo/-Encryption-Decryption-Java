package encryptdecrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class works with command-line arguments. The program can parse five arguments:
 * -mode, -key and -data -in -out.
 * The first argument should determine the program’s mode
 * (enc for encryption, dec for decryption).
 * The second argument is an integer key to modify the message,
 * and the third argument is a text or ciphertext to encrypt or decrypt.
 * Arguments -in and -out to specify the full name of a file to read data and to write the result.
 */
public class Main {
    static final String ENC = "enc";
    static final String DEC = "dec";
    static final String MODE = "-mode";
    static final String KEY = "-key";
    static final String DATA = "-data";
    static final String IN = "-in";
    static final String OUT = "-out";
    static final String CONSOLE = "console";
    static final String ERROR = "Error";

    public static void main(String[] args) {
        try {
            readArgs(args);
        } catch (Exception e) {
            e.printStackTrace();
            printToConsole(ERROR);
        }
    }

    /**
     * Method read command line args and write them to
     * HashMap with name of arg as a key and its parameter as a value.
     * @param args command line parameters
     * @throws IOException
     */
    public static void readArgs(String[] args) throws IOException {
        HashMap<String, String> parameters = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            if (i % 2 != 0) {
                parameters.put(args[i - 1], args[i]);
            }
        }
        final var mode = parameters.getOrDefault(MODE, ENC);
        final var key = Integer.parseInt(parameters.getOrDefault(KEY, "0"));
        final var in = parameters.getOrDefault(IN, null);
        final var data = parameters.getOrDefault(DATA, null);
        final var out = parameters.getOrDefault(OUT, CONSOLE);

        if (out.equals(CONSOLE)) {
            printToConsole(readData(data, key, mode, in));
        } else {
            writeToFile(out, readData(data, key, mode, in));
        }

    }

    /**
     * Method select how to read input data, read them and choose encrypt/decrypt mode
     * @param data input data
     * @param key encrypt/decrypt key
     * @param mode encrypt/decrypt mode
     * @param in path to read input data (optional)
     * @return string with encrypt/decrypt data
     * @throws IOException
     */
    private static String readData(String data, int key, String mode, String in) throws IOException {
        String cryptData;
        if (data != null && in != null || data != null && in == null) {
            cryptData = data;
        } else if (in != null && data == null ) {
            cryptData = readFromFile(in);
        } else {
            throw new RuntimeException();
        }
        return cryptData(cryptData, key, mode);
    }

    /**
     * Method modify data as String and encrypt or decrypt them
     * @param data input data
     * @param key encrypt/decrypt key
     * @param mode encrypt/decrypt mode
     * @return
     */
    private static String cryptData(String data, int key, String mode) {
        char[] sb = new char[data.length()];
        data = data.trim();
        System.out.println("length " + data.length());
        for (var i = 0; i < data.length(); i++) {
            var tmpChar = data.charAt(i);
            if (mode.equals(ENC)) {
                sb[i] = encrypt(tmpChar, key);
            } else {
                sb[i] = decrypt(tmpChar, key);
            }
        }
        return String.valueOf(sb);
    }

    /**
     * Method encrypt char
     * @param tmpChar char to encrypt
     * @param key shift encrypting
     * @return
     */
    private static char encrypt(char tmpChar, int key) {
        return ((char) (tmpChar + key));
    }

    /**
     * Method decrypt char
     * @param tmpChar char to decrypt
     * @param key shift decrypting
     * @return
     */
    private static char decrypt(char tmpChar, int key) {
        return ((char) (tmpChar - key));
    }

    /**
     * Method read file from input path
     * @param in path to read
     * @return data as String
     * @throws IOException
     */
    private static String readFromFile(String in) throws IOException {
        return new String(Files.readAllBytes(Paths.get(in)));
    }

    /**
     * Method write data to file for input path
     * @param out path to write data to file
     * @param data data to write
     * @throws IOException
     */
    private static void writeToFile(String out, String data) throws IOException {
        FileWriter writer = new FileWriter(out);
        writer.write(data);
        writer.flush();
        writer.close();
    }

    /**
     * Method print data to console
     * @param message data to print
     */
    private static void printToConsole(String message) {
        System.out.println(message);
    }

}
