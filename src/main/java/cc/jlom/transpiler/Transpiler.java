package cc.jlom.transpiler;

import cc.jlom.analyzer.Analyzer;
import cc.jlom.generator.Generator;
import cc.jlom.logger.CustomLogger;
import cc.jlom.wrappers.ClassWrapper;
import cc.jlom.wrappers.DataWrapper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Transpiler {
    private String input_path;
    private String output_path;

    private final List<DataWrapper> data_wrappers;
    private final List<ClassWrapper> class_wrapper;


    public Transpiler(String path) {
        this.input_path = path;
        this.output_path = path.replace(".jar", "-out.jar");
        this.data_wrappers = new ArrayList<>();
        this.class_wrapper = new ArrayList<>();
        CustomLogger.create_logger(true);
    }

    public void process() {
        read_jar();
        process_jar();
        save_jar();
    }

    void read_jar() {
        try (var in = new ZipInputStream(new FileInputStream(input_path))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                byte[] data = in.readAllBytes();
                if (entry.getName().endsWith(".class")) {
                    var reader = new ClassReader(data);
                    var node = new ClassNode();
                    reader.accept(node, ClassReader.EXPAND_FRAMES);
                    class_wrapper.add(new ClassWrapper(entry.getName(), node, data));
                } else {
                    data_wrappers.add(new DataWrapper(entry.getName(), data));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Jar file not found " + e);
        }
    }

    void process_jar() {
        var analyzer = new Analyzer();
        var generator = new Generator(input_path.replace(".jar", ".asm"));
        for (var wrapper : class_wrapper) {
           analyzer.process(wrapper, generator);
        }
        try {
            generator.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var wrapper : class_wrapper) {
            var node = wrapper.node();

            var writer = new ClassWriter(ClassWriter.COMPUTE_MAXS) {
                @Override
                protected String getCommonSuperClass(String type1, String type2) {
                    return "java/lang/Object";
                }
            };
            node.accept(writer);
            wrapper.replace_data(writer.toByteArray());
        }
    }

    void save_jar() {
        try (var out = new ZipOutputStream(new FileOutputStream(output_path))) {
            for (var res : data_wrappers) {
                out.putNextEntry(new ZipEntry(res.name()));
                out.write(res.data());
            }

            for (var wrapper : class_wrapper) {
                byte[] classBytes = wrapper.data();

                String outputName = wrapper.name();
                out.putNextEntry(new ZipEntry(outputName));
                out.write(classBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cant create output file " + e);
        }
    }
}
