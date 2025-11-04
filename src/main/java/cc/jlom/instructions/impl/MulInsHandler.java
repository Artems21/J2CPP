package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;

public class MulInsHandler extends InstructionHandler {
    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var rhs = emulator.stack.pop();
        var lhs = emulator.stack.pop();
        emulator.push_reg_to_reg_pool(rhs);
        emulator.stack.push(lhs);
        return "imul " + lhs.name() +", " + rhs.name();    }
}
