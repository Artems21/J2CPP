package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ILOAD;


public class LoadInsHandler extends InstructionHandler {

    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var load_inst = (VarInsnNode) inst;
        var opcode = inst.getOpcode();
        var reg = emulator.pop_reg_from_reg_pool();
        emulator.stack.push(reg);
        var local = emulator.find_in_locals(load_inst.var);

        return String.format("mov %s, [rsp+%d]", local.size() == 4 ? reg.name() : reg.name_8byte() , local.offset());
    }
}
