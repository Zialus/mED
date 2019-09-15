package fcup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Testar {

    public static void main(String[] args) {

        System.out.println("-----------Just testing---------------");

        ArrayList<FileBuffer> lista = new ArrayList<>();  // Lista de Buffers

        File temp;
        try {
            temp = File.createTempFile("temp-file-name", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Temp file : " + temp.getAbsolutePath());

        Path path = temp.toPath();

        FileBuffer fb = new FileBuffer(path);

        try {
            fb.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lista.add(fb);

        System.out.println(lista);

        BufferView buff;
        try {
            buff = new BufferView(lista);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            buff.startTerm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}