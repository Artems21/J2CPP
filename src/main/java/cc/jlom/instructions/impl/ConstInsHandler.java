package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;

import static org.objectweb.asm.Opcodes.*;

public class ConstInsHandler extends InstructionHandler {
    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var opcode = inst.getOpcode();
        var reg = emulator.pop_reg_from_reg_pool();
        emulator.stack.push(reg);
        if (ICONST_M1 <= opcode && opcode <= ICONST_5) {
            return String.format("mov %s, %d", reg.name(), opcode - ICONST_0);
        }
        if (LCONST_0 <= opcode && opcode <= LCONST_1) {
            return String.format("mov %s, %d", reg.name_8byte(), opcode - LCONST_0);
        }
        if (FCONST_0 <= opcode && opcode <= FCONST_2) {
            return String.format("mov %s, %d", reg.name(), opcode - FCONST_0);
        }
        if (DCONST_0 <= opcode && opcode <= DCONST_1) {
            return String.format("mov %s, %d", reg.name_8byte(), opcode - DCONST_0);
        }
        return "null";
    }
}
