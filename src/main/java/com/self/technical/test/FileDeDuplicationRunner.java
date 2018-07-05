package com.self.technical.test;

public class FileDeDuplicationRunner {


    public static void main(String[] args) {

        FileDeDuplication fileDeDuplication = new FileDeDuplication();
        if (args.length < 2) {
            System.out.println("USAGE: java -jar build/libs/pythian-1.0-SNAPSHOT.jar <dirToBeSearched> <Boolean:toFollowLinksAreNot> ");
            System.exit(1);
        }

        String dirToSearch = args[0];
        Boolean followLinks = Boolean.valueOf(args[1]);

        fileDeDuplication.findDuplicate(dirToSearch, followLinks);

        System.out.println("Files grouped by duplicate content");

        fileDeDuplication.getDuplicateFiles().forEach((s, files) -> {
                    System.out.println("*********");
                    files.forEach(file -> System.out.println(file.getAbsolutePath()));
                    System.out.println("*********");
                }
        );

    }
}
