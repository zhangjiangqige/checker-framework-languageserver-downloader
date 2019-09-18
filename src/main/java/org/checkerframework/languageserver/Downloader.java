package org.checkerframework.languageserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class Downloader {
    public static File downloadLanguageServer(File folder) throws IOException {
        return download(getLanguageServerURL(), folder);
    }

    public static File downloadCheckerFramework(File folder) throws IOException {
        String cfzip = download(getCheckerFrameworkURL(), folder).getAbsolutePath();
        new ZipFile(cfzip).extractAll(folder.getAbsolutePath());
        return Paths.get(FilenameUtils.getFullPath(cfzip), FilenameUtils.getBaseName(cfzip)).toFile();
    }

    public static URL getLanguageServerURL() throws IOException {
        return getLatestGithubRelease("zhangjiangqige", "checker-framework-languageserver");
    }

    public static URL getCheckerFrameworkURL() throws IOException {
        return getLatestGithubRelease("typetools", "checker-framework");
    }

    private static URL getLatestGithubRelease(String author, String repo) throws IOException {
        String json = IOUtils.toString(new URL("https://api.github.com/repos/" + author + "/" + repo + "/releases/latest"), StandardCharsets.UTF_8.name());
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(json);
        JsonArray assets = root.getAsJsonObject().getAsJsonArray("assets");
        return new URL(assets.get(0).getAsJsonObject().get("browser_download_url").getAsString());
    }

    private static File download(URL url, File folder) throws IOException {
        Path dest = Paths.get(folder.getAbsolutePath(), FilenameUtils.getName(url.getPath()));
        File ret = dest.toFile();
        System.out.printf("Downloading from %s to %s\n", url.toString(), ret.toString());
        FileUtils.copyURLToFile(url, ret);
        return ret;
    }
}
