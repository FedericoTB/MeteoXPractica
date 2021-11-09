package ioutils;

import pojos.CityMeans;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MarkDownGenerator {
    public static void generateMarkdownOfCityMeans(List<CityMeans> means, String uri) throws IOException {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < means.size(); i++) {
            buffer.append(means.get(i).toString());
        }
        System.out.println(buffer);
        if (!Files.exists(Path.of(uri))) {
            Files.createFile(Path.of(uri));
        }
        Files.writeString(Path.of(uri), buffer, Charset.forName("windows-1252"));
    }
}
