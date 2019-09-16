package org.checkerframework.languageserver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Downloader {
    public static File downloadLanguageServer(File folder) throws IOException {
        return download(getLanguageServerURL(), folder);
    }

    public static File downloadCheckerFramework(File folder) throws IOException {
        String cfzip = download(getCheckerFrameworkURL(), folder).getAbsolutePath();
        new ZipFile(cfzip).extractAll(folder.getAbsolutePath());
        return Paths.get(FilenameUtils.getFullPath(cfzip), FilenameUtils.getBaseName(cfzip)).toFile();
    }

    public static URL getLanguageServerURL() {
        // TODO
        try {
            return new URL("https://github.com/zhangjiangqige/checker-framework-languageserver/releases/latest/download/checker-framework-languageserver-all-0.0.1.jar");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static URL getCheckerFrameworkURL() {
        // TODO
        try {
            return new URL("https://github.com/typetools/checker-framework/releases/latest/download/checker-framework-2.11.0.zip");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static File download(URL url, File folder) throws IOException {
        Path dest = Paths.get(folder.getAbsolutePath(), FilenameUtils.getName(url.getPath()));
        File ret = dest.toFile();
        System.out.printf("Downloading from %s to %s\n", url.toString(), ret.toString());
        FileUtils.copyURLToFile(url, ret);
        return ret;
    }
}
