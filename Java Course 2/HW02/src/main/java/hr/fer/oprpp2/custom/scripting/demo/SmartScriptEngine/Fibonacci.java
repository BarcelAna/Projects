package hr.fer.oprpp2.custom.scripting.demo.SmartScriptEngine;

import hr.fer.oprpp2.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp2.webserver.RequestContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fibonacci {
    public static void main(String[] args) throws IOException {
        Path p = Path.of(args[0]);
        String documentBody = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
        Map<String,String> parameters = new HashMap<String, String>();
        Map<String,String> persistentParameters = new HashMap<String, String>();
        List<RequestContext.RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
// create engine and execute it
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();
    }
}
