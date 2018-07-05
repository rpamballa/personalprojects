package com.self.technical.test;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public final class FileDeDuplication {

    private static Logger LOGGER = Logger.getLogger(FileDeDuplication.class.getName());

    private List<Path> leafNodes = new ArrayList<>();
    private Map<String, List<File>> duplicateFiles = new ConcurrentHashMap<>();

    private void getCandidateFiles(String dir) {
        Path dirToSearch = Paths.get(dir);
        if (Files.isDirectory(dirToSearch, NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirToSearch)) {
                for (Path path : stream) {
                    if (Files.isDirectory(path)) {
                        getCandidateFiles(path.toAbsolutePath().toString());
                    } else {
                        leafNodes.add(path.toAbsolutePath());
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, " Error accessing dir  " + dir, e);
            }
        } else {
            System.out.println(" Given path does not represent a valid folder name. " + dir);
        }
    }

    private void getCandidateFilesWithVistor(String dir) {
        DuplicateFileVisitor fileVisitor = new DuplicateFileVisitor();
        try {
            Files.walkFileTree(Paths.get(dir), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, fileVisitor);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, " Error accessing dir  " + dir, e);
        }
        leafNodes.addAll(fileVisitor.getLeafNodes());


    }

    private void sortFilesByDuplicateContent() {
        leafNodes.forEach(path -> {
            String md5 = calculateMd5(path.toFile());
            if (duplicateFiles.containsKey(md5)) {
                duplicateFiles.get(md5).add(path.toFile());
            } else {
                List<File> files = new ArrayList<>();
                files.add(path.toFile());
                duplicateFiles.put(md5, files);
            }
        });
    }

    public void findDuplicate(String dir, Boolean withTreeWalker) {
        if (withTreeWalker) {
            getCandidateFilesWithVistor(dir);
        } else {
            getCandidateFiles(dir);
        }
        sortFilesByDuplicateContent();
    }

    public String calculateMd5(File file) {
        String rv = "";
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");

            int numRead;
            do {
                numRead = inputStream.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }

            } while (numRead != -1);
            rv = new String(complete.digest());
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, " File Not Found " + file, e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, " Error accessing File  " + file, e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, " MD5 Not found ", e);
        }
        return rv;
    }

    public Map<String, List<File>> getDuplicateFiles() {
        return duplicateFiles;
    }
}
