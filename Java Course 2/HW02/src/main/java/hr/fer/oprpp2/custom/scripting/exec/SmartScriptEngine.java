package hr.fer.oprpp2.custom.scripting.exec;

import hr.fer.oprpp2.custom.scripting.elems.*;
import hr.fer.oprpp2.custom.scripting.nodes.*;
import hr.fer.oprpp2.webserver.RequestContext;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

/**
 * SmartScriptEngine is a class representing engine for executing documents parsed with SmartScriptParser.
 */
public class SmartScriptEngine {
    /**
     * document node which represents script to be executed
     */
    private DocumentNode documentNode;

    /**
     * request context
     */
    private RequestContext requestContext;

    /**
     * multistack object
     */
    private ObjectMultistack multistack = new ObjectMultistack();

    /**
     * Visitor for executing specific nodes
     */
    private INodeVisitor visitor = new INodeVisitor() {
        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            String variableName = node.getVariable().asText();
            ValueWrapper variableValue = new ValueWrapper(node.getStartExpression().asText());
            ValueWrapper endValue = new ValueWrapper(node.getEndExpression().asText());
            String incValue = node.getStepExpression().asText();

            multistack.push(variableName, variableValue);

            while(variableValue.numCompare(endValue.getValue())<=0) {
                for(int i = 0; i < node.numberOfChildren(); ++i) {
                    node.getChild(i).accept(this);
                }
                variableValue = multistack.pop(variableName);
                variableValue.add(incValue);
                multistack.push(variableName, variableValue);
            }

            multistack.pop(variableName);
        }

        @Override
        public void visitEchoNode(EchoNode node) throws IOException {
            Stack<Object> tempStack = new Stack<>();
            for(Element e : node.getElements()) {
                if(e instanceof ElementConstantDouble || e instanceof ElementConstantInteger) {
                    tempStack.push(e.asText());
                }else if(e instanceof ElementString) {
                    tempStack.push(e.asText().substring(1, e.asText().length()-1));
                }else if(e instanceof ElementVariable) {
                    ValueWrapper variableValue = multistack.peek(e.asText());
                    tempStack.push(variableValue.getValue());
                } else if(e instanceof ElementOperator) {
                    ValueWrapper v1 = new ValueWrapper(tempStack.pop());
                    ValueWrapper v2 = new ValueWrapper(tempStack.pop());
                    switch (e.asText()) {
                        case "+":
                            v1.add(v2.getValue());
                            break;
                        case "-":
                            v1.subtract(v2.getValue());
                            break;
                        case "*":
                            v1.multiply(v2.getValue());
                            break;
                        case "/":
                            v1.divide(v2.getValue());
                            break;
                    }
                    tempStack.push(v1.getValue());
                } else if(e instanceof ElementFunction) {
                    switch(e.asText()) {
                        case "sin":
                            Functions.sin(tempStack);
                            break;
                        case "decfmt":
                            Functions.decfmt(tempStack);
                            break;
                        case "dup":
                            Functions.dup(tempStack);
                            break;
                        case "swap":
                            Functions.swap(tempStack);
                            break;
                        case "setMimeType":
                            Functions.setMimeType(tempStack, requestContext);
                            break;
                        case "paramGet":
                            Functions.paramGet(tempStack, requestContext);
                            break;
                        case "pparamGet":
                            Functions.pparamGet(tempStack, requestContext);
                            break;
                        case "pparamSet":
                            Functions.pparamSet(tempStack, requestContext);
                            break;
                        case "pparamDel":
                            Functions.pparamDel(tempStack, requestContext);
                            break;
                        case "tparamGet":
                            Functions.tparamGet(tempStack, requestContext);
                            break;
                        case "tparamSet":
                            Functions.tparamSet(tempStack, requestContext);
                            break;
                        case "tparamDel":
                            Functions.tparamDel(tempStack, requestContext);
                            break;
                    }
                }
            }
            if(!tempStack.isEmpty()) {
                Object[] remaining = new Object[tempStack.size()];
                for(int i = remaining.length-1; i >= 0; --i) {
                    remaining[i] = tempStack.pop();
                }
                for(int i = 0; i < remaining.length; ++i) {
                    try {
                        requestContext.write(remaining[i].toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for(int i = 0; i < node.numberOfChildren(); ++i) {
                node.getChild(i).accept(this);
            }
        }
    };

    /**
     * Constructor which accepts document node of script to be executed and request context.
     * @param documentNode
     * @param requestContext
     */
    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = documentNode;
        this.requestContext = requestContext;
    }

    /**
     * Executes parsed document.
     */
    public void execute() {
        documentNode.accept(visitor);
    }

    /**
     * Utility class which offers number of functions.
     */
    private static class Functions {

        /**
         * Popps one element from the given stack, calculates it's sinus and pushes the result back on the stack.
         * @param tempStack
         * @throws RuntimeException - if popped objecct is not a number
         */
        public static void sin(Stack<Object> tempStack) {
            Object element = tempStack.pop();
            if(element instanceof Number) {
                Number x = (Number)element;
                Number result = Math.sin(x.doubleValue()*Math.PI/180);
                tempStack.push(result);
            } else {
                throw new RuntimeException("Popped object must be a Number to calculate sin.");
            }
        }

        /**
         * Formats decimal number using two popped objects.
         * @param tempStack
         * @throws RuntimeException - if first popped object is not a String or if second is not Integer, Double or String.
         */
        public static void decfmt(Stack<Object> tempStack) {
            Object element1 = tempStack.pop();

            DecimalFormat f;
            if(element1 instanceof String) {
                f = new DecimalFormat((String)element1);
            } else {
                throw new RuntimeException("Popped object must be a String to form DecimalFormat object");
            }

            Object element2 = tempStack.pop();
            if(element2 instanceof Integer || element2 instanceof Double || element2 instanceof String) {
                String result = f.format(element2);
                tempStack.push(result);
            } else {
                throw new RuntimeException("Popped object must be an Integer, Double or String object");
            }

        }

        /**
         * Duplicates popped object and returns it back to the stack.
         * @param tempStack
         */
        public static void dup(Stack<Object> tempStack) {
            Object v = tempStack.pop();
            tempStack.push(v);
            tempStack.push(v);
        }

        /**
         * Replaces the order of two topmost items on stack.
         * @param tempStack
         */
        public static void swap(Stack<Object> tempStack) {
            Object a = tempStack.pop();
            Object b = tempStack.pop();
            tempStack.push(a);
            tempStack.push(b);
        }

        /**
         * Sets request context's mime type to the popped value.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped object is not a String
         */
        public static void setMimeType(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String x;
            if(element1 instanceof String) {
                x = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }
            requestContext.setMimeType(x);
        }

        /**
         * Pops name and default value of the parameter from the stack.
         * Then tries to retrive the value of the parameter from the parameter map and store that value on the stack if such parameter exists, otherwise default value is stored.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped object are not String
         */
        public static void paramGet(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String defValue;
            if(element1 instanceof String) {
                defValue = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            Object element2 = tempStack.pop();
            String name;
            if(element2 instanceof String) {
                name = (String)element2;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            String value = requestContext.getParameter(name);
            tempStack.push(value==null?defValue:value);
        }

        /**
         * Pops name and default value of the parameter from the stack.
         * Then tries to retrive the value of the parameter from the persistent parameter map and store that value on the stack if such parameter exists, otherwise default value is stored.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped object are not String
         */
        public static void pparamGet(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String defValue;
            if(element1 instanceof String) {
                defValue = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            Object element2 = tempStack.pop();
            String name;
            if(element2 instanceof String) {
                name = (String)element2;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            String value = requestContext.getPersistentParameter(name);
            tempStack.push(value==null?defValue:value);
        }

        /**
         * Pops name and new value of the parameter from the stack.
         * Then sets persistent parameter to the given values.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped parameter name is not String
         */
        public static void pparamSet(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String name;
            if(element1 instanceof String) {
                name = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            Object element2 = tempStack.pop();
            String value = element2.toString();

            requestContext.setPersistentParameter(name, value);
        }

        /**
         * Pops name of the parameter from the stack.
         * Then removes persistent parameter with that name.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped parameter name is not String
         */
        public static void pparamDel(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String name;
            if(element1 instanceof String) {
                name = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }
            requestContext.removePersistentParameter(name);
        }

        /**
         * Pops name and default value of the parameter from the stack.
         * Then tries to retrieve the value of the parameter from the temporary parameter map and store that value on the stack if such parameter exists, otherwise default value is stored.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped object are not String
         */
        public static void tparamGet(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String defValue;
            if(element1 instanceof String) {
                defValue = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            Object element2 = tempStack.pop();
            String name;
            if(element2 instanceof String) {
                name = (String)element2;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            String value = requestContext.getTemporaryParameter(name);
            tempStack.push(value==null?defValue:value);
        }

        /**
         * Pops name and new value of the parameter from the stack.
         * Then sets temporary parameter to the given values.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped parameter name is not String
         */
        public static void tparamSet(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String name;
            if(element1 instanceof String) {
                name = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }

            Object element2 = tempStack.pop();
            String value = element2.toString();

            requestContext.setTemporaryParameter(name, value);
        }

        /**
         * Pops name of the parameter from the stack.
         * Then removes temporary parameter with that name.
         * @param tempStack
         * @param requestContext
         * @throws RuntimeException - if popped parameter name is not String
         */
        public static void tparamDel(Stack<Object> tempStack, RequestContext requestContext) {
            Object element1 = tempStack.pop();
            String name;
            if(element1 instanceof String) {
                name = (String)element1;
            } else {
                throw new RuntimeException("Popped object must be String.");
            }
            requestContext.removeTemporaryParameter(name);
        }
    }
}
