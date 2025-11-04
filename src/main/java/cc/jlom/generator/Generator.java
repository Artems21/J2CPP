package cc.jlom.generator;

import cc.jlom.macros.MacroBuffer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Generator {
    private final HashSet<String> data_section;
    private final HashSet<MacroBuffer> macros;
    private final ArrayList<MethodBuffer> methods;
    private final File output;
    private final FileWriter writer;

    public Generator(String output_file_path) {
        this.data_section = new HashSet<>();
        this.methods = new ArrayList<>();
        this.output = new File(output_file_path);
        this.macros = new HashSet<>();
        try {
            this.writer = new FileWriter(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MethodBuffer gen_method(String name) {
        var method = new MethodBuffer(name);
        this.methods.add(method);
        return method;
    }

    public void add_to_data(String data) {
        data_section.add(data);
    }

    public void add_to_macros(MacroBuffer macro) {
        macros.add(macro);
    }

    public void finish() throws IOException {
        include_call_methods();
        write_title();
        write_macros();
        write_methods();
        write_data_section();
        writer.close();
    }

    private void include_call_methods() {
        var get_static_method = gen_method("getstatic_object_field");
        get_static_method.append("\n    push rbp\n" +
                "    push rbx\n" +
                "    sub rsp, 24\n" +
                "\n" +
                "    push rdx\n" +
                "    push rcx\n" +
                "\n" +
                "    mov rax, [rdi] ; env\n" +
                "    mov rbx, rdi\n" +
                "    call qword [rax+48]\n" +
                "\n" +
                "    pop rcx\n" +
                "    mov rdi, rbx\n" +
                "    pop rdx\n" +
                "    mov rbp, rax\n" +
                "    mov rax, [rbx] ; env*\n" +
                "    mov rsi, rbp ; cls\n" +
                "    ; rdi env, rsi cls, rdx name, rcx desc\n" +
                "    call qword [rax+1152]\n" +
                "    ;; fid\n" +
                "\n" +
                "\n" +
                "    mov rsi, rbp ; cls\n" +
                "    mov rdi, rbx ; env\n" +
                "    mov rdx, rax ; fid\n" +
                "    mov rax, [rbx] ; env*\n" +
                "    ; rdi env, rsi cls, rdx fid\n" +
                "    mov rax, [rax+1160] ; rax out\n" +
                "\n" +
                "    add rsp, 24\n" +
                "    pop rbx\n" +
                "    pop rbp\n" +
                "    jmp rax\n");
    }

    private void write_title() throws IOException {
        writer.write("format ELF64\n");
        for (var method : methods) {
            writer.write("public " + method.name + "\n");
        }
        writer.write("\nsection '.text' executable align 16\n\n");
        writer.flush();
    }

    private void write_macros() throws IOException {
        for (var macro : macros) {
            writer.write(String.format("macro %s ", macro.name()));
            var args = macro.args();
            for (int i = 0; i < args.length; i++) {
                var arg = args[i];
                writer.write(String.format("%s%s", arg, i == args.length-1 ? " ":", "));
            }
            writer.write("{\n");
            writer.write(macro.code());
            writer.write("}\n");
        }
    }

    private void write_methods() throws IOException {
        for (var method : methods) {
            writer.write("\n" + method.name + ":\n");
            for (var inst : method.code) {
                writer.write(inst + "\n");
            }
        }
        writer.flush();
    }

    private void write_data_section() throws IOException {
        writer.write("\nsection '.data' writeable\n");
        for (var data : data_section) {
            writer.write(data + "\n");
        }
        writer.flush();
    }

    public static class MethodBuffer {
        private final String name;
        private final ArrayList<String> code;

        public MethodBuffer(String name) {
            this.name = name;
            this.code = new ArrayList<>();
        }

        public void append(String string) {
            code.add(string);
        }
    }
}
