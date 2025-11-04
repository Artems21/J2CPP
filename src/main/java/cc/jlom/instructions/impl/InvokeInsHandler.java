package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
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
        var ret_type = Type.getReturnType(invoke_inst.desc);
        var func_name = invoke_inst.name;
        var func_desc = invoke_inst.desc;
        var func_owner = invoke_inst.owner;
        emulator.set_call_true();
        emulator.add_to_data("S" + abs(func_name.hashCode()), func_name);
        emulator.add_to_data("S" + abs(func_desc.hashCode()), func_desc);
        emulator.include_macro(STORE_REG_MACR0);
        emulator.include_macro(LOAD_REG_MACR0);
        switch (opcode) {
            case INVOKEVIRTUAL: {
                emulator.include_macro(INVOKEVIRTUAL_MACRO);
                var param_list = new ArrayList<Emulator.Register>();
                for (int i = 0; i < params.length; i++) {
                    param_list.add(emulator.stack.pop());
                }

                var rev_params = param_list.reversed();
                var out = emulator.stack.pop();

                // call store macro
                buffer.append(String.format("%s %d\n", STORE_REG_MACR0.name(),emulator.locals_size));

//                buffer.append(String.format("mov rsi, %s\n", out.name_8byte()));
//                buffer.append("mov rax, [rdi]\n");
//                buffer.append("call qword [rax+248]\n");
//
//                buffer.append(String.format("mov rdi, [rsp+%d]\n", emulator.locals_size + 24));
//
//                buffer.append(String.format("lea rcx, [S%s]\n", abs(func_desc.hashCode())));
//                buffer.append(String.format("lea rdx, [S%s]\n", abs(func_name.hashCode())));
//                buffer.append("mov rsi, rax\n");
//                buffer.append("mov rax, [rdi]\n");
//                buffer.append("call qword [rax+264]\n");
//
//                buffer.append(String.format("mov rdi, [rsp+%d]\n", emulator.locals_size + 24));
//
//                buffer.append(String.format("mov rsi, %s\n", out.name_8byte()));
//                buffer.append("mov rdx, rax\n");
                  //invokevirtual rbx, S1263150, S314717969, 1, r12
                  buffer.append(String.format("invokevirtual %s, S%s, S%s, %d,%d, ",
                          out.name_8byte(),
                          abs(func_desc.hashCode()),
                          abs(func_name.hashCode()),
                          emulator.locals_size+24,
                          rev_params.size()));

                for (int i = 0; i < rev_params.size(); i++) {
                    var arg = rev_params.get(i);
                    buffer.append(arg.name_8byte() + (i == rev_params.size()-1 ? "" : ", "));
                    emulator.push_reg_to_reg_pool(arg);
                }
                buffer.append("\n");
//
//                for (int i = 0; i < rev_params.size(); i++) {
//                    var arg = rev_params.get(i);
//                    buffer.append(String.format("mov %s, %s\n", ABI_CALL_CONV[i + 3].name_8byte(), arg.name_8byte()));
//                    emulator.push_reg_to_reg_pool(arg);
//                }
//
//                buffer.append("call qword [rax+488]\n");



                // call load macro
                buffer.append(String.format("%s %d\n", LOAD_REG_MACR0.name(),emulator.locals_size));

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

                // call store macro
                buffer.append(String.format("%s %d\n", STORE_REG_MACR0.name(),emulator.locals_size));

                buffer.append(String.format("lea rsi, [S%s]\n", abs(func_owner.hashCode())));
                buffer.append("mov rax, [rdi]\n");
                buffer.append("call qword [rax+48]\n");
                var cls_reg = emulator.pop_reg_from_reg_pool();
                buffer.append(String.format("mov %s, rax\n", cls_reg.name_8byte()));

                buffer.append(String.format("mov rdi, [rsp+%d]\n", emulator.locals_size + 24));

                buffer.append(String.format("lea rcx, [S%s]\n", abs(func_desc.hashCode())));
                buffer.append(String.format("lea rdx, [S%s]\n", abs(func_name.hashCode())));
                buffer.append("mov rsi, rax\n");
                buffer.append("mov rax, [rdi]\n");
                buffer.append("call qword [rax+904]\n");

                emulator.push_reg_to_reg_pool(cls_reg);

                buffer.append(String.format("mov rdi, [rsp+%d]\n", emulator.locals_size + 24));
                buffer.append(String.format("mov rsi, %s\n", cls_reg.name_8byte()));
                buffer.append("mov rdx, rax\n");
                for (int i = 0; i < rev_params.size(); i++) {
                    var arg = rev_params.get(i);
                    buffer.append(String.format("mov %s, %s\n", ABI_CALL_CONV[i + 3].name_8byte(), arg.name_8byte()));
                    emulator.push_reg_to_reg_pool(arg);
                }
                buffer.append("mov rax, [rdi]\n");
                buffer.append("call qword [rax+1032]\n");

                // call load macro
                buffer.append(String.format("%s %d\n", LOAD_REG_MACR0.name(),emulator.locals_size));

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
