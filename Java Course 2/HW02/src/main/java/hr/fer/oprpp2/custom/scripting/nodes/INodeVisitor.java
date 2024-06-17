package hr.fer.oprpp2.custom.scripting.nodes;

import java.io.IOException;

public interface INodeVisitor {
    public void visitTextNode(TextNode node);
    public void visitForLoopNode(ForLoopNode node);
    public void visitEchoNode(EchoNode node) throws IOException;
    public void visitDocumentNode(DocumentNode node);
}
