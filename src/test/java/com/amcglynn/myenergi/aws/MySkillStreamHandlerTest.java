package com.amcglynn.myenergi.aws;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * I forgot to register some intent handlers before and wasted time figuring out what's wrong. The purpose of this test
 * is to prevent that from happening again.
 */
class MySkillStreamHandlerTest {

    @Test
    void testAllIntentHandlersAreRegistered() throws Exception {
        var filesInPackage = getClassNamesInPackage("com.amcglynn.myenergi.aws.intenthandlers")
                .stream().filter(file -> !file.endsWith("Test")).collect(Collectors.toList());
        assertThat(filesInPackage).describedAs("Make sure to register your new RequestHandler in " +
                "MySkillStreamHandler and then change this count manually in this test once it is done").hasSize(12);
    }

    public List<String> getClassNamesInPackage(String packageName) throws IOException {
        var fileList = new ArrayList<String>();
        var classLoader = Thread.currentThread().getContextClassLoader();
        var path = packageName.replace('.', '/');
        var resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            File dir = new File(resources.nextElement().getFile());
            assertThat(dir).isNotNull().isDirectory();
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    try {
                        fileList.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)).getName());
                    } catch (ClassNotFoundException e) {
                        // Ignore classes that cannot be loaded
                    }
                }
            }
        }
        return fileList;
    }
}
