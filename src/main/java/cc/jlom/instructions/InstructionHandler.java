package cc.jlom.instructions;


import cc.jlom.emulator.Emulator;
import cc.jlom.generator.Generator;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class InstructionHandler {
    public abstract String convert(AbstractInsnNode inst, Emulator emulator);
    public String name() {
        return this.getClass().getSimpleName();
    }
}
