package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

import static cc.jlom.macros.MacrosImpl.*;
import static java.lang.Math.abs;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.LDC;

public class PushInsHandler extends InstructionHandler {
    @Override
    public String convert(AbstractInsnNode inst, Emulator emulator) {
        var opcode = inst.getOpcode();
        var reg = emulator.pop_reg_from_reg_pool();
        emulator.stack.push(reg);
        if (opcode == LDC) {
            var ldc_inst = (LdcInsnNode) inst;
            if (!(ldc_inst.cst instanceof String))
                throw new RuntimeException("Unexpected object type in PushHandler: " + ldc_inst.cst.getClass());

            emulator.include_macro(STORE_REG_MACR0);
            emulator.include_macro(LOAD_REG_MACR0);
            emulator.include_macro(CREATE_JSTR_MACRO);

            var str = (String) ldc_inst.cst;
            var buffer = new StringBuilder();
            /* mov	rax, QWORD PTR [rdi]
               lea  rsi, [str]
	           jmp	[QWORD PTR 1336[rax]] */
            var str_data = "S" + abs(str.hashCode());
            emulator.set_call_true();
            emulator.add_to_data(str_data, str);

            // call store macro
            buffer.append(String.format("%s %d\n", STORE_REG_MACR0.name(), emulator.locals_size));

            // call create jstr macro
            buffer.append(String.format("%s %s\n", CREATE_JSTR_MACRO.name(), str_data));

            // call load macro
            buffer.append(String.format("%s %d\n", LOAD_REG_MACR0.name(), emulator.locals_size));

            buffer.append(String.format("mov %s, rax\n", reg.name_8byte()));


            return buffer.toString();
        }

        var push_inst = (IntInsnNode) inst;
        return String.format("mov %s, %d", reg.name(), push_inst.operand);


    }
}
