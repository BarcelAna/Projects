package hr.fer.oprpp2.custom.scripting.demo;

import hr.fer.oprpp2.custom.scripting.nodes.*;
import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Program reads from the given file, parses file's content and writes it on the standard output using Visitor Design Pattern.
 */
public class TreeWriter {
    public static void main(String[] args) throws IOException {
        if(args.length!=1) {
            System.out.println("Program expects exactly one argument: file path");
            return;
        }
        String filePath = args[0];

        String docBody = new String(Files.readAllBytes(Path.of(filePath)), StandardCharsets.UTF_8);
        SmartScriptParser p = new SmartScriptParser(docBody);
        WriterVisitor visitor = new WriterVisitor();
        p.getDocumentNode().accept(visitor);

    }

    private static class WriterVisitor implements INodeVisitor {
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
}
