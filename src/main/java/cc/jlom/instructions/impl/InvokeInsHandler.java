package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import cc.jlom.macros.MacroBuffer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.ArrayList;
import java.util.Arrays;

import static cc.jlom.analyzer.Analyzer.ABI_CALL_CONV;
import static cc.jlom.macros.MacrosImpl.*;
import static java.lang.Math.abs;
import static org.objectweb.asm.Opcodes.*;

public class InvokeInsHandler extends InstructionHandler {


    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var opcode = inst.getOpcode();
        var invoke_inst = (MethodInsnNode) inst;

        var buffer = new StringBuilder();
        var params = Type.getArgumentTypes(invoke_inst.desc);
        var params_size = params.length;
        var ret_type = Type.getReturnType(invoke_inst.desc);
        var func_name = invoke_inst.name;
        var func_desc = invoke_inst.desc;
        var func_owner = invoke_inst.owner;
        emulator.set_call_true();
        emulator.add_to_data("S" + abs(func_name.hashCode()), func_name);
        emulator.add_to_data("S" + abs(func_desc.hashCode()), func_desc);

        emulator.include_macro(STORE_REG_MACR0);
        emulator.include_macro(LOAD_REG_MACR0);

        MacroBuffer used_macro;


        switch (opcode) {
            case INVOKEVIRTUAL: {
                var param_list = new ArrayList<Emulator.Register>();
                for (int i = 0; i < params.length; i++) {
                    param_list.add(emulator.stack.pop());
                }

                switch (params_size) {
                    case 1 -> used_macro = INVOKEVIRTUAL_1ARG_MACRO;
                    case 2 -> used_macro = INVOKEVIRTUAL_2ARG_MACRO;
                    case 3 -> used_macro = INVOKEVIRTUAL_3ARG_MACRO;
                    default ->  throw new RuntimeException("Unexcepted args count " + params_size);

                }
                emulator.include_macro(used_macro);

                var rev_params = param_list.reversed();
                var out = emulator.stack.pop();

                // call store macro
                buffer.append(String.format("%s %d\n", STORE_REG_MACR0.name(), emulator.locals_size));

                buffer.append(String.format("%s %s, S%s, S%s, %d, ",
                        used_macro.name(),
                        out.name_8byte(),
                        abs(func_desc.hashCode()),
                        abs(func_name.hashCode()),
                        emulator.locals_size + 24));

                for (int i = 0; i < rev_params.size(); i++) {
                    var arg = rev_params.get(i);
                    buffer.append(arg.name_8byte()).append(i == rev_params.size() - 1 ? "" : ", ");
                    emulator.push_reg_to_reg_pool(arg);
                }
                buffer.append("\n");

                // call load macro
                buffer.append(String.format("%s %d\n", LOAD_REG_MACR0.name(), emulator.locals_size));

                if (ret_type != Type.VOID_TYPE)
                    throw new RuntimeException("NOT VOID TYPE");
//                    buffer.append(String.format("mov %s, rax\n", emulator.pop_reg_from_reg_pool().name_8byte()));


                emulator.push_reg_to_reg_pool(out);

                return buffer.toString();
            }
            case INVOKESTATIC: {
                emulator.add_to_data("S" + abs(func_owner.hashCode()), func_owner);

                var param_list = new ArrayList<Emulator.Register>();
                for (int i = 0; i < params.length; i++) {
                    param_list.add(emulator.stack.pop());
                }

                var rev_params = param_list.reversed();

                switch (params_size) {
                    case 1 -> used_macro = INVOKESTATIC_1ARG_MACRO;
                    case 2 -> used_macro = INVOKESTATIC_2ARG_MACRO;
                    case 3 -> used_macro = INVOKESTATIC_3ARG_MACRO;
                    default ->  throw new RuntimeException("Unexcepted args count " + params_size);

                }
                emulator.include_macro(used_macro);

                // call store macro
                buffer.append(String.format("%s %d\n", STORE_REG_MACR0.name(), emulator.locals_size));

                var buffer_reg = emulator.pop_reg_from_reg_pool();

                buffer.append(String.format("%s S%s, S%s, S%s, %d, %s, ",
                        used_macro.name(),
                        abs(func_owner.hashCode()),
                        abs(func_desc.hashCode()),
                        abs(func_name.hashCode()),
                        emulator.locals_size + 24,
                        buffer_reg.name_8byte()));

                emulator.push_reg_to_reg_pool(buffer_reg);

                for (int i = 0; i < rev_params.size(); i++) {
                    var arg = rev_params.get(i);
                    buffer.append(arg.name_8byte()).append(i == rev_params.size() - 1 ? "" : ", ");
                    emulator.push_reg_to_reg_pool(arg);
                }
                buffer.append("\n");

                // call load macro
                buffer.append(String.format("%s %d\n", LOAD_REG_MACR0.name(), emulator.locals_size));

                var ret_reg = emulator.pop_reg_from_reg_pool();
                buffer.append(String.format("mov %s, rax\n", ret_reg.name_8byte()));
                emulator.stack.push(ret_reg);

                return buffer.toString();
            }
            default:
                throw new RuntimeException("Please implement me <3 " + opcode + " opcode");
        }
    }
}
