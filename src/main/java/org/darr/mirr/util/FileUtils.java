package org.darr.mirr.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.util.function.FunctionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

    public static List<String> resolveToFileList(String path) {
        List<String> filePathList;
        if (path.endsWith("*") || Paths.get(path).toFile().isDirectory()) {
            if (path.endsWith("*")) {
                path = path.substring(0, path.length() - 1);
            }
            var fileList = Paths.get(path).toFile().listFiles();
            if (fileList == null || fileList.length == 0) {
                throw new IllegalStateException("There is no files in the directory '" + path + "'");
            }
            filePathList = Stream
                    .of(fileList)
                    .filter(file -> !file.isDirectory())
                    .map(File::getAbsolutePath)
                    .toList();
        } else {
            filePathList = List.of(path);
        }

        log.debug("Resolve path to file list : path={}, file_list={}", path, filePathList);
        return filePathList;
    }

    public static Path getParentDirectoryPath(String path) {
        return Paths.get(path).getParent();
    }

    /**
     * Read file with different content.
     *
     * @param path path to read.
     * @return file content.
     */
    public static String readContent(String path) {
        return readContent(List.of(path))
                .values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no content for file by path " + path));
    }

    /**
     * Read all files in list with different charset and return result as map<FileName, FileContent>.
     *
     * @param filePathList file path list.
     * @return dictionary as map<FileName, FileContent>.
     */
    public static Map<String, String> readContent(List<String> filePathList) {
        return filePathList
                .stream()
                .map(Paths::get)
                .map(path -> {
                    try {
                        return readContent(path, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        // in case of file saved at windows-1251 charset
                        if (e instanceof MalformedInputException) {
                            try {
                                return readContent(path, Charset.forName("windows-1251"));
                            } catch (IOException ioe) {
                                log.debug("Read content with charset 'windows-1251' is failed.");
                                // try to recover but failed. Throw original error.
                            }
                        }
                        throw new RuntimeException(e);
                    }
                })
                .reduce(new HashMap<String, String>(), FunctionUtil.concatMap());
    }

    public static Map<String, String> readContentAndFilter(List<String> filePathList, BiPredicate<String, String> filter) {
        return FileUtils
                .readContent(filePathList)
                .entrySet()
                .stream()
                .filter(entry -> filter.test(entry.getKey(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Path createOutputDirectory(Path baseDirectory, String directoryName) throws IOException {
        var directoryOutput = baseDirectory.resolve(directoryName);
        if (Files.notExists(directoryOutput)) {
            log.info("Create output directory");
            Files.createDirectory(directoryOutput);
        }
        return directoryOutput;
    }

    public static List<String> readFileAsLineList(Path filePath, Predicate<String> filter) throws IOException {
        Objects.requireNonNull(filePath, "File path must be non-null value");
        Objects.requireNonNull(filter, "Lines filter must be non-null value");

        try (var reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            return reader
                    .lines()
                    .filter(filter)
                    .toList();
        }
    }

    public static List<String> readFileAsLineList(Path filePath) throws IOException {
        return FileUtils.readFileAsLineList(filePath, any -> true);
    }

    private static Map<String, String> readContent(Path path, Charset charset) throws IOException {
        var fileContent = Files.readString(path, charset);
        var fileName = path.toFile().getName();
        return Map.of(fileName, fileContent);
    }
}
