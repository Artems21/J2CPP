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

    public static final MacroBuffer INVOKEVIRTUAL_MACRO = new MacroBuffer("invokevirtual", new String[]{"obj", "desc", "name", "rdi_offset","args_count", "[args]"},
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
                    "    if args_count eq 1\n" +
                    "        match first, args \\{\n" +
                    "            mov rcx, first\n" +
                    "        \\}\n" +
                    "    else if args_count eq 2\n" +
                    "        match a,b, args \\{\n" +
                    "            mov rcx, a\n" +
                    "            mov r8,  b\n" +
                    "        \\}\n" +
                    "    else if args_count eq 3\n" +
                    "        match a,b,c, args \\{\n" +
                    "            mov rcx, a\n" +
                    "            mov r8,  b\n" +
                    "            mov r9,  c\n" +
                    "        \\}\n" +
                    "    end if\n" +
                    "\n" +
                    "    call qword [rax+488]\n");
}
