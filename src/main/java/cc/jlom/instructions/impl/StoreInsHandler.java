package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;

public class StoreInsHandler extends InstructionHandler {
    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var var_inst = (VarInsnNode) inst;
        var opcode = inst.getOpcode();
        var reg = emulator.stack.pop();
        emulator.push_reg_to_reg_pool(reg);
        var local = emulator.find_in_locals(var_inst.var);

        if (ISTORE <= opcode && opcode <= ASTORE) {
            return String.format("mov [rsp + %d], %s", local.offset(), local.size() == 4 ? reg.name() : reg.name_8byte());
        }
        throw  new RuntimeException("Please implement me <3 StoreInsHandler");
    }
}
