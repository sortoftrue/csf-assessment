package ibf2022.batch2.csf.backend.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class UnzipUtility {

    private static final int BUFFER_SIZE = 4096;

    public static List<MultipartFile> unzip(MultipartFile multipartFile) throws IOException {
        List<MultipartFile> extractedFiles = new ArrayList<>();
        ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(multipartFile.getInputStream()));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            if (!entry.isDirectory()) {
                String filename = entry.getName();
                System.out.println(filename + filename.substring(filename.lastIndexOf(".")+1));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                extractFile(zipIn, outputStream);
                // Convert the output stream to a MultipartFile
                MultipartFile extractedFile = new MockMultipartFile(
                        entry.getName(),
                        entry.getName(),
                        filename.substring(filename.lastIndexOf(".")+1),
                        new ByteArrayInputStream(outputStream.toByteArray())
                );
                extractedFiles.add(extractedFile);
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        return extractedFiles;
    }

    private static void extractFile(ZipInputStream zipIn, OutputStream outputStream) throws IOException {
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            outputStream.write(bytesIn, 0, read);
        }
        outputStream.close();
    }
}