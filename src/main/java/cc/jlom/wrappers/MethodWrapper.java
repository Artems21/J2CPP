package cc.jlom.wrappers;

import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class MethodWrapper {


    private final MethodNode methodNode;
    private final String name;

    public MethodWrapper(MethodNode methodNode) {
        this.methodNode = methodNode;
        this.name = methodNode.name;
    }

    public MethodNode node() {
        return methodNode;
    }

    public String name() {
        return name;
    }
}