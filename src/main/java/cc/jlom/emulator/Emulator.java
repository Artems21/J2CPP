package cc.jlom.emulator;

import cc.jlom.macros.MacroBuffer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Emulator {
    private final Stack<Register> reg_pool;
    public final Stack<Register> stack;
    public final ArrayList<Register> used_regs;
    public final ArrayList<Data> data_section;
    public final HashSet<MacroBuffer> macros;

    public final ArrayList<Local> locals;
    public int locals_size = 0;
    public boolean is_call_inside;

    public Emulator() {
        reg_pool = new Stack<>();
        reg_pool.push(new Register("r15"));
        reg_pool.push(new Register("r14"));
        reg_pool.push(new Register("r13"));
        reg_pool.push(new Register("r12"));
        reg_pool.push(new Register("bx"));

        stack = new Stack<>();
        used_regs = new ArrayList<>();
        data_section = new ArrayList<>();
        locals = new ArrayList<>();
        macros = new HashSet<>();
        is_call_inside = false;
    }

    public Register pop_reg_from_reg_pool() {
        var reg = reg_pool.pop();
        if (!used_regs.contains(reg))
            used_regs.add(reg);
        return reg;
    }

    public void set_call_true() {
        is_call_inside = true;
    }

    public void push_reg_to_reg_pool(Register reg) {
        reg_pool.push(reg);
    }

    public void add_to_data(String name, String data) {
        if (!contains_in_data(name))
            data_section.add(new Data(name, data));
    }

    public boolean contains_in_data(String name) {
        for (var data : data_section) {
            if (data.name.equals(name)) {
                return  true;
            }
        }
        return false;
    }

    public void add_to_locals(int id, int offset, int size) {
        locals.add(new Local(id, offset, size));
    }

    public Local find_in_locals(int id) {
        for (var local : locals) {
            if (local.id == id)
                return local;
        }
        throw new RuntimeException("Not found local in locals with id " + id);
    }

    public void include_macro(MacroBuffer macro){
        macros.add(macro);
    }

    public record Register(String name) {

        @Override
        public String name() {
            if (name.startsWith("r"))
                return name + "d";
            return "e" + name;
        }

        public String name_8byte() {
            if (name.startsWith("r"))
                return name;
            return "r" + name;
        }
    }

    public record Data(String name, String data) {
        @Override
        public String name() {
            return name;
        }

        public String data() {
            return data;
        }
    }

    public record Local(int id, int offset, int size) {
        @Override
        public int id() {
            return id;
        }

        public int offset() {
            return offset;
        }
        public int size() {
            return size;
        }

    }
}
