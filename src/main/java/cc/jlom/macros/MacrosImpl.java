package cc.jlom.macros;

public class MacrosImpl {
    public static final MacroBuffer STORE_REG_MACR0 = new MacroBuffer("save_regs", new String[]{"offset"},
            "    mov [rsp+offset], rsi\n" +
                    "    mov [rsp+offset+8], rdx\n" +
                    "    mov [rsp+offset+16], rcx\n" +
                    "    mov [rsp+offset+24], rdi\n");

    public static final MacroBuffer LOAD_REG_MACR0 = new MacroBuffer("store_regs", new String[]{"offset"},
            "    mov rsi, [rsp+offset]\n" +
                    "    mov rdx, [rsp+offset+8]\n" +
                    "    mov rcx, [rsp+offset+16]\n" +
                    "    mov rdi, [rsp+offset+24]\n");

    public static final MacroBuffer GETSTATIC_MACRO = new MacroBuffer("getstatic", new String[]{"owner", "name", "desc"},
            "    lea rsi, [owner]\n" +
                    "    lea rdx, [name]\n" +
                    "    lea rcx, [desc]\n" +
                    "    call getstatic_object_field\n");

    public static final MacroBuffer INVOKEVIRTUAL_1ARG_MACRO = new MacroBuffer("invokevirtual_1arg", new String[]{"obj", "desc", "name", "rdi_offset","arg1"},
            "    mov rsi, obj\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+248]\n" +
                    "    mov rdi, [rsp+rdi_offset]\n" +
                    "    lea rcx, [desc]\n" +
                    "    lea rdx, [name]\n" +
                    "    mov rsi, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+264]\n" +
                    "\n" +
                    "    mov rdi, [rsp+rdi_offset]\n" +
                    "\n" +
                    "    mov rsi, obj\n" +
                    "    mov rdx, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    mov rcx, arg1\n" +
                    "\n" +
                    "    call qword [rax+488]");

    public static final MacroBuffer INVOKEVIRTUAL_2ARG_MACRO = new MacroBuffer("invokevirtual_2arg", new String[]{"obj", "desc", "name", "rdi_offset","arg1", "arg2"},
            "    mov rsi, obj\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+248]\n" +
                    "    mov rdi, [rsp+rdi_offset]\n" +
                    "    lea rcx, [desc]\n" +
                    "    lea rdx, [name]\n" +
                    "    mov rsi, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+264]\n" +
                    "\n" +
                    "    mov rdi, [rsp+rdi_offset]\n" +
                    "\n" +
                    "    mov rsi, obj\n" +
                    "    mov rdx, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    mov rcx, arg1\n" +
                    "    mov r8, arg2\n" +
                    "\n" +
                    "    call qword [rax+488]");

    public static final MacroBuffer INVOKEVIRTUAL_3ARG_MACRO = new MacroBuffer("invokevirtual_3arg", new String[]{"obj", "desc", "name", "rdi_offset","arg1", "arg2", "arg3"},
            "    mov rsi, obj\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+248]\n" +
                    "    mov rdi, [rsp+rdi_offset]\n" +
                    "    lea rcx, [desc]\n" +
                    "    lea rdx, [name]\n" +
                    "    mov rsi, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+264]\n" +
                    "\n" +
                    "    mov rdi, [rsp+rdi_offset]\n" +
                    "\n" +
                    "    mov rsi, obj\n" +
                    "    mov rdx, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    mov rcx, arg1\n" +
                    "    mov r8, arg2\n" +
                    "    mov r9, arg3\n" +
                    "\n" +
                    "    call qword [rax+488]");

    public static final MacroBuffer INVOKESTATIC_1ARG_MACRO = new MacroBuffer("invokestatic_1arg", new String[]{"owner", "desc", "name", "rdi_offset", "buffer","arg1"},
            "    lea rsi, [owner]\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+48]\n" +
                    "    mov buffer, rax\n" +
                    "    mov rdi, [rsp+28]\n" +
                    "    lea rcx, [desc]\n" +
                    "    lea rdx, [name]\n" +
                    "    mov rsi, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+904]\n" +
                    "    mov rdi, [rsp+28]\n" +
                    "    mov rsi, buffer\n" +
                    "    mov rdx, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    mov rcx, arg1\n" +
                    "\n" +
                    "    call qword [rax + 1032]");

    public static final MacroBuffer INVOKESTATIC_2ARG_MACRO = new MacroBuffer("invokestatic_2arg", new String[]{"owner", "desc", "name", "rdi_offset", "buffer","arg1", "arg2"},
            "    lea rsi, [owner]\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+48]\n" +
                    "    mov buffer, rax\n" +
                    "    mov rdi, [rsp+28]\n" +
                    "    lea rcx, [desc]\n" +
                    "    lea rdx, [name]\n" +
                    "    mov rsi, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+904]\n" +
                    "    mov rdi, [rsp+28]\n" +
                    "    mov rsi, buffer\n" +
                    "    mov rdx, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    mov rcx, arg1\n" +
                    "    mov r8, arg2\n" +
                    "\n" +
                    "    call qword [rax + 1032]");



    public static final MacroBuffer INVOKESTATIC_3ARG_MACRO = new MacroBuffer("invokestatic_3arg", new String[]{"owner", "desc", "name", "rdi_offset", "buffer","arg1", "arg2", "arg3"},
            "    lea rsi, [owner]\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+48]\n" +
                    "    mov buffer, rax\n" +
                    "    mov rdi, [rsp+28]\n" +
                    "    lea rcx, [desc]\n" +
                    "    lea rdx, [name]\n" +
                    "    mov rsi, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+904]\n" +
                    "    mov rdi, [rsp+28]\n" +
                    "    mov rsi, buffer\n" +
                    "    mov rdx, rax\n" +
                    "    mov rax, [rdi]\n" +
                    "    mov rcx, arg1\n" +
                    "    mov r8, arg2\n" +
                    "    mov r9, arg3\n" +
                    "\n" +
                    "    call qword [rax + 1032]");

    public static final MacroBuffer CREATE_JSTR_MACRO = new MacroBuffer("create_jstr", new String[]{"src"},
            "    lea rsi, [src]\n" +
                    "    mov rax, [rdi]\n" +
                    "    call qword [rax+1336]\n");
}
