package com.self.technical.test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DuplicateFileVisitor extends SimpleFileVisitor<Path> {

    private static Logger LOGGER = Logger.getLogger(DuplicateFileVisitor.class.getName());

    private List<Path> leafNodes = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (Files.isSymbolicLink(file) && Files.isRegularFile(file)) {
            leafNodes.add(file.toAbsolutePath());
        } else if (Files.isRegularFile(file)) {
            leafNodes.add(file);
        }
        return super.visitFile(file, attrs);

    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        if (exc instanceof FileSystemLoopException) {
            LOGGER.log(Level.WARNING, " Circular Reference detected Skipping Sub_tree" , exc);
            return FileVisitResult.SKIP_SUBTREE;
        }
        return super.visitFileFailed(file, exc);
    }

    public List<Path> getLeafNodes() {
        return this.leafNodes;
    }
}
