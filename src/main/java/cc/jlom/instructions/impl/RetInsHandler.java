package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;

import static org.objectweb.asm.Opcodes.RETURN;

public class RetInsHandler extends InstructionHandler {

    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var opcode = inst.getOpcode();
        if (opcode == RETURN)
            return "xor eax, eax";
        return "mov eax, " + emulator.stack.pop().name();
    }
}
