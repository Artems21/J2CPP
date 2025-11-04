package cc.jlom.analyzer;

import cc.jlom.emulator.Emulator;
import cc.jlom.generator.Generator;
import cc.jlom.instructions.InstructionHandler;
import cc.jlom.instructions.impl.*;
import cc.jlom.logger.CustomLogger;
import cc.jlom.macros.MacroBuffer;
import cc.jlom.wrappers.ClassWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import static cc.jlom.utils.GenUtil.gen_name_4_asm;
import static org.objectweb.asm.Opcodes.*;


public class Analyzer {
    private static final List<String> blocked_names = Arrays.asList("<init>", "<clinit>", "main");

    private static final Map<Integer, InstructionHandler> instruction_handler_map = new HashMap<>();

    public static final Emulator.Register[] ABI_CALL_CONV = {
            new Emulator.Register("di"),
            new Emulator.Register("si"),
            new Emulator.Register("dx"),
            new Emulator.Register("cx"),
            new Emulator.Register("r8"),
            new Emulator.Register("r9")};


    public void process(ClassWrapper class_wrapper, Generator generator) {
        CustomLogger.get_logger().log("Analyzing " + class_wrapper.name() + " class");
        for (var method_wrapper : class_wrapper.methods()) {
            CustomLogger.get_logger().log("- Analyzing " + method_wrapper.name() + " metod");

            if (blocked_names.contains(method_wrapper.name()))
                continue;

            var method_node = method_wrapper.node();
            var generate_name = gen_name_4_asm(class_wrapper, method_wrapper);

            CustomLogger.get_logger().log("-- Generated name " + generate_name);
            var params = Type.getArgumentTypes(method_node.desc);
            var locals = method_node.localVariables;
            CustomLogger.get_logger().debug("Locals size " + locals.size());

            var emulator = new Emulator();

            fill_locals(emulator, locals);

            var method_buffer = generator.gen_method(generate_name);

            var code = generate_code(method_node, emulator);

            var needed_memory = calc_stack_memory(emulator);

            method_buffer.append("push rbp");
            method_buffer.append("mov rbp, rsp");
            // save callee saved registers on stack
            save_callee_registers(emulator, method_buffer);

            method_buffer.append(String.format("sub rsp, %d", needed_memory));

            store_params_to_locals(emulator, method_buffer, params.length);

            for (var line : code) {
                method_buffer.append(line);
            }

            method_buffer.append(String.format("add rsp, %d", needed_memory));


            // back callee saved registers from stack
            back_callee_registers(emulator, method_buffer);

            method_buffer.append("leave");
            method_buffer.append("ret");

            // add data to data section in file
            include_data(generator, emulator.data_section);
            // add macros to file
            include_macro(generator, emulator.macros);
        }
    }

    private int calc_stack_memory(Emulator emulator) {
        var locals_size = emulator.locals.size() * 8;
        var used_regs_size = emulator.used_regs.size() * 8 + 8;
        var memory_for_call = 32;
        var fin_size = locals_size + memory_for_call;
        CustomLogger.get_logger().debug(String.format("Is call inside: %b, stack size: %d, divide on 16: %d", emulator.is_call_inside, fin_size + used_regs_size, (fin_size + used_regs_size) % 16));
        if (emulator.is_call_inside) {
            if ((fin_size + used_regs_size) % 16 == 8) {
                return fin_size;
            } else {
                return fin_size + 8;
            }
        } else {
            if ((fin_size + used_regs_size) % 16 == 0) {
                return fin_size;
            } else {
                return fin_size + 8;
            }
        }
    }

    private void store_params_to_locals(Emulator emulator, Generator.MethodBuffer method_buffer, int params_count) {
        for (int i = 0; i < params_count; i++) {
            var local = emulator.locals.get(i);
            method_buffer.append(String.format("mov [rsp + %d], %s", local.offset(),
                    local.size() == 4 ? ABI_CALL_CONV[i + 2].name() : ABI_CALL_CONV[i + 2].name_8byte()));
        }
    }

    private void back_callee_registers(Emulator emulator, Generator.MethodBuffer method_buffer) {
        for (var reg : emulator.used_regs.reversed()) {
            method_buffer.append("pop " + reg.name_8byte());
        }
    }

    private void save_callee_registers(Emulator emulator, Generator.MethodBuffer method_buffer) {
        for (var reg : emulator.used_regs) {
            method_buffer.append("push " + reg.name_8byte());
        }

    }

    private void include_data(Generator generator, List<Emulator.Data> data_list) {
        for (var data : data_list) {
            generator.add_to_data(String.format("%s: db \"%s\", 0", data.name(), data.data()));
        }
    }

    private void include_macro(Generator generator, HashSet<MacroBuffer> macros) {
        for (var macro : macros) {
            generator.add_to_macros(macro);
        }
    }

    private List<String> generate_code(MethodNode method_node, Emulator emulator) {
        var code = new ArrayList<String>();
        for (var inst : method_node.instructions) {
            var opcode = inst.getOpcode();
            // just skip
            if (opcode == -1)
                continue;

            if (!instruction_handler_map.containsKey(opcode)) {
                CustomLogger.get_logger().log(String.format("--- Not find handler on opcode {%d}, skip", opcode));
                continue;
            }
            var handler = instruction_handler_map.get(opcode);
            CustomLogger.get_logger().debug(String.format("On opcode %d, call %s", opcode, handler.name()));
            var asm_inst = handler.convert(inst, emulator);
            code.add(asm_inst);
        }
        return code;
    }

    private void fill_locals(Emulator emulator, List<LocalVariableNode> locals) {
        var offset = 0;
        var id = 0;
//        for (var param : locals) {
//            switch (param.toString()) {
//                case "I":
//                    emulator.add_to_locals(id, offset, 4);
//                    id += 1;
//                    offset += 4;
//                    break;
//                default:
//                    if (param.toString().startsWith("L")) {
//                        emulator.add_to_locals(id, offset, 8);
//                        id += 2;
//                        offset += 8;
//                    } else {
//                        throw new RuntimeException("Please implement me param type " + param);
//                    }
//            }
//        }
        for (var local : locals) {
            switch (local.desc) {
                case "I":
                    emulator.add_to_locals(id, offset, 4);
                    id += 1;
                    offset += 4;
                    break;
                default:
                    if (local.desc.startsWith("L")) {
                        emulator.add_to_locals(id, offset, 8);
                        id += 2;
                        offset += 8;
                    } else {
                        throw new RuntimeException("Please implement me param type " + local.desc);
                    }
            }
        }
        emulator.locals_size = offset;

    }


    static {
        //create handlers

        // ADD
        add_handler_on_opcode(IADD, DADD, new AddInsHandler());
        // SUB
        add_handler_on_opcode(ISUB, DSUB, new SubInsHandler());
        // MUL
        add_handler_on_opcode(IMUL, DMUL, new MulInsHandler());
//        // DIV
//        add_handler_on_opcode(IDIV, DDIV, new DivInsHandler());
        // RET
        add_handler_on_opcode(IRETURN, RETURN, new RetInsHandler());
        // LOAD
        add_handler_on_opcode(ILOAD, ALOAD, new LoadInsHandler());
        // STORE
        add_handler_on_opcode(ISTORE, SASTORE, new StoreInsHandler());
        // FIELD
        add_handler_on_opcode(GETSTATIC, PUTFIELD, new FieldInsHandler());
        // CONST
        add_handler_on_opcode(ICONST_M1, DCONST_1, new ConstInsHandler());
        // PUSH
        add_handler_on_opcode(BIPUSH, LDC, new PushInsHandler());
        // INVOKE
        add_handler_on_opcode(INVOKEVIRTUAL, INVOKEDYNAMIC, new InvokeInsHandler());

    }

    private static void add_handler_on_opcode(int left, int right, InstructionHandler handler) {
        for (int i = left; i <= right; i++)
            add_handler_on_opcode(i, handler);
    }

    private static void add_handler_on_opcode(int opcode, InstructionHandler handler) {
        instruction_handler_map.put(opcode, handler);
    }

}
