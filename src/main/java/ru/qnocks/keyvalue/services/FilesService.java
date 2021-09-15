package ru.qnocks.keyvalue.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class FilesService {
    @Value("${files.dump.path}")
    private String dumpPath;

    @Value("${files.dump.filename}")
    private String dumpFilename;

    public String dumpData(String data) {
        String filename = dumpPath + dumpFilename;
        File file = createFile(dumpPath, filename);
        PrintWriter pw;
        try {
            pw = new PrintWriter(file);
            pw.write(data);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String loadDump() {
        String filename = dumpPath + dumpFilename;
        StringBuilder sb = new StringBuilder();
        FileReader fr;
        try {
            fr = new FileReader(filename);
            int c;
            while ((c = fr.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private File createFile(String dir, String filename) {
        File directory = new File(dir);
        if (!directory.exists()) directory.mkdir();
        return new File(filename);
    }
}
