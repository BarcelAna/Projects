package hr.fer.oprpp2.custom.scripting.demo;

import hr.fer.oprpp2.custom.scripting.nodes.*;

public class WriterVisitor implements INodeVisitor {
    String document = "";
    @Override
    public void visitTextNode(TextNode node) {
        document += node.toString();
    }

    @Override
    public void visitForLoopNode(ForLoopNode node) {
        document += node.toString();
    }

    @Override
    public void visitEchoNode(EchoNode node) {
        document += node.toString();
    }

    @Override
    public void visitDocumentNode(DocumentNode node) {
        for(int i = 0; i < node.numberOfChildren(); ++i) {
               Node child = node.getChild(i);
               child.accept(this);
        }
        System.out.println(document);
    }
}
