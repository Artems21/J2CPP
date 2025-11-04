package cc.jlom.instructions.impl;

import cc.jlom.emulator.Emulator;
import cc.jlom.instructions.InstructionHandler;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

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

            var str = (String) ldc_inst.cst;
            var buffer = new StringBuilder();
            /* mov	rax, QWORD PTR [rdi]
               lea  rsi, [str]
	           jmp	[QWORD PTR 1336[rax]] */
            var str_data = "S" + abs(str.hashCode());
            emulator.set_call_true();
            emulator.add_to_data(str_data, str);

            buffer.append(String.format("mov [rsp+%d], rsi\n", emulator.locals_size));
            buffer.append(String.format("mov [rsp+%d], rdx\n", emulator.locals_size+8));
            buffer.append(String.format("mov [rsp+%d], rcx\n", emulator.locals_size+16));
            buffer.append(String.format("mov [rsp+%d], rdi\n", emulator.locals_size+24));
            buffer.append(String.format("lea rsi, [%s]\n", str_data));
            buffer.append("mov rax, [rdi]\n");
            buffer.append("call qword [rax+1336]\n");
            buffer.append(String.format("mov %s, rax\n", reg.name_8byte()));
            buffer.append(String.format("mov rsi, [rsp+%d]\n", emulator.locals_size));
            buffer.append(String.format("mov rdx, [rsp+%d]\n", emulator.locals_size+8));
            buffer.append(String.format("mov rcx, [rsp+%d]\n", emulator.locals_size+16));
            buffer.append(String.format("mov rdi, [rsp+%d]\n", emulator.locals_size+24));


            return buffer.toString();
        }

        var push_inst = (IntInsnNode) inst;
        return String.format("mov %s, %d", reg.name(), push_inst.operand);


    }
}
