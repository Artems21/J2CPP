package cc.jlom.utils;

import cc.jlom.wrappers.ClassWrapper;
import cc.jlom.wrappers.MethodWrapper;
import org.objectweb.asm.tree.MethodNode;

public class GenUtil {
    public static String gen_name_4_asm(ClassWrapper owner, MethodWrapper method) {
        return "Java_" + owner.name().replace(".class", "").replace("/", "_") + "_" + method.name().replace("_", "_1");
    }
}
