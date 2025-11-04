package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

import static cc.jlom.macros.MacrosImpl.*;
import static java.lang.Math.abs;
import static org.objectweb.asm.Opcodes.GETSTATIC;

public class FieldInsHandler extends InstructionHandler {
    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var opcode = inst.getOpcode();
        var field_inst = (FieldInsnNode) inst;
        if (opcode != GETSTATIC)
            throw new RuntimeException("Implement me please <3 opcode: " + opcode);
        var desc = field_inst.desc;
        var name = field_inst.name;
        var owner = field_inst.owner;
        emulator.set_call_true();
        emulator.include_macro(STORE_REG_MACR0);
        emulator.include_macro(LOAD_REG_MACR0);
        emulator.include_macro(GETSTATIC_MACRO);



        emulator.add_to_data("S" + abs(desc.hashCode()), desc);
        emulator.add_to_data("S" + abs(name.hashCode()), name);
        emulator.add_to_data("S" + abs(owner.hashCode()), owner);
        var buffer = new StringBuffer();

        // call store macro
        buffer.append(String.format("%s %d\n", STORE_REG_MACR0.name(),emulator.locals_size));

        buffer.append(String.format("%s S%s,S%s,S%s\n", GETSTATIC_MACRO.name(), abs(owner.hashCode()), abs(name.hashCode()), abs(desc.hashCode())));

        // call load macro
        buffer.append(String.format("%s %d\n", LOAD_REG_MACR0.name(),emulator.locals_size));

        var reg = emulator.pop_reg_from_reg_pool();
        buffer.append(String.format("mov %s, rax\n", reg.name_8byte()));
        emulator.stack.push(reg);
        return buffer.toString();
    }
}
