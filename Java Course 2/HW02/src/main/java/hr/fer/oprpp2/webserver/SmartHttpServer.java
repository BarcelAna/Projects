package hr.fer.oprpp2.webserver;

import hr.fer.oprpp2.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple HTTP server.
 */
public class SmartHttpServer {
    /**
     * server IP address
     */
    private String address;

    /**
     * server domain name
     */
    private String domainName;

    /**
     * server port
     */
    private int port;

    /**
     * number of workers
     */
    private int workerThreads;

    /**
     * session timeout
     */
    private int sessionTimeout;

    /**
     * supported mime types
     */
    private Map<String,String> mimeTypes = new HashMap<String, String>();

    /**
     * server thread
     */
    private ServerThread serverThread;

    /**
     * thread pool
     */
    private ExecutorService threadPool;

    /**
     * path to the document root
     */
    private Path documentRoot;

    /**
     * flag which indicates server thread to stop
     */
    private boolean stop;

    /**
     * maps specific web worker to it's path
     */
    private Map<String, IWebWorker> workersMap = new HashMap<>();

    /**
     * session map
     */
    private Map<String, SessionMapEntry> session = new HashMap<>();

    /**
     * random object used for generating random sid-s
     */
    private Random sessionRandom = new Random();
    private SessionThread sessionThread;

    /**
     * Constructor which accepts path to the server configuration file.
     * @param configFileName
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public SmartHttpServer(String configFileName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        FileReader serverPropsReader = new FileReader(configFileName);
        Properties p = new Properties();

        readServerProperties(p, serverPropsReader);
    }

    /**
     * Utility method for reading server properties from server configuration file.
     * @param p
     * @param serverPropsReader
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void readServerProperties(Properties p, FileReader serverPropsReader) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        p.load(serverPropsReader);

        address = p.getProperty("server.address");
        domainName = p.getProperty("server.domainName");
        port = Integer.parseInt(p.getProperty("server.port"));
        workerThreads = Integer.parseInt(p.getProperty("server.workerThreads"));
        sessionTimeout = Integer.parseInt(p.getProperty("session.timeout"));
        String mimeConfigFileName = p.getProperty("server.mimeConfig");
        serverThread = new ServerThread();
        documentRoot = Path.of(p.getProperty("server.documentRoot"));
        String workersConfigFileName = p.getProperty("server.workers");
        stop = false;

        readMimeConfigFile(p, mimeConfigFileName);
        readWorkersConfigFile(p, workersConfigFileName);
    }

    /**
     * Utility method for reading workers configuration file.
     * @param p
     * @param workersConfigFileName
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void readWorkersConfigFile(Properties p, String workersConfigFileName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        p.clear();
        FileReader workersPropsReader = new FileReader(workersConfigFileName);
        p.load(workersPropsReader);
        Set<Map.Entry<Object, Object>> workersSet = p.entrySet();
        for(Map.Entry<Object, Object> we : workersSet) {
            String workerPath = (String)we.getKey();
            String fqcn = (String)we.getValue();

            Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
            Object newObject = referenceToClass.newInstance();
            IWebWorker iww = (IWebWorker) newObject;

            if(workersMap.get(workerPath) != null) {
                throw new RuntimeException("There is already IWebWorker object attached to this path.");
            }
            workersMap.put(workerPath, iww);
        }
    }

    /**
     * Utility method for reading mime type configuration file.
     * @param p
     * @param mimeConfigFileName
     * @throws IOException
     */
    private void readMimeConfigFile(Properties p, String mimeConfigFileName) throws IOException {
        p.clear();
        FileReader mimePropsReader = new FileReader(mimeConfigFileName);
        p.load(mimePropsReader);
        Set<Map.Entry<Object, Object>> mimeTypesSet = p.entrySet();
        for(Map.Entry<Object, Object> mt : mimeTypesSet) {
            mimeTypes.put((String)mt.getKey(), (String)mt.getValue());
        }
    }

    /**
     * Starts server thread and initializes thread pool
     */
    protected synchronized void start() {
        if(!serverThread.isAlive()) {
            serverThread.start();

            sessionThread = new SessionThread();
            sessionThread.setDaemon(true);
            sessionThread.start();
        }
        threadPool = Executors.newFixedThreadPool(workerThreads);
    }

    /**
     * Signals server thread to stop running.
     */
    protected synchronized void stop() {
        stop = true;
        threadPool.shutdown();
    }

    protected class SessionThread extends Thread  {
        @Override
        public void run() {
            while(!stop) {
                try {
                    sleep(300000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for(Map.Entry<String, SessionMapEntry> e : session.entrySet()) {
                    String key = e.getKey();
                    SessionMapEntry value = e.getValue();
                    if(value.validUntil < (System.currentTimeMillis()/1000)) {
                        session.remove(key);
                    }
                }
            }
        }
    }

    /**
     * ServerThread represents a thread listening for client requests for connection.
     */
    protected class ServerThread extends Thread {
        @Override
        public void run() {
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            while(!stop) {
                Socket client;
                try {
                    client = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                ClientWorker cw = new ClientWorker(client);
                threadPool.submit(cw);
            }
        }


        /**
         * ClientWorker class represents job which is executed by the client thread.
         */
        private class ClientWorker implements Runnable, IDispatcher {
            /**
             * socket for communication with client
             */
            private Socket csocket;

            /**
             * input stream
             */
            private InputStream istream;

            /**
             * output stream
             */
            private OutputStream ostream;

            /**
             * version of HTTP protocol
             */
            private String version;

            /**
             * used HTTP method
             */
            private String method;

            /**
             * host domain name
             */
            private String host;

            /**
             * request parameters
             */
            private Map<String, String> params = new HashMap<String, String>();

            /**
             * temporaty parameters
             */
            private Map<String, String> tempParams = new HashMap<String, String>();

            /**
             * permanent parameters
             */
            private Map<String, String> permPrams = new HashMap<String, String>();

            /**
             * list of output cookies
             */
            private List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();

            /**
             * session id
             */
            private String SID;

            /**
             * request context
             */
            private RequestContext context = null;

            /**
             * Constructor which accepts client socket.
             * @param csocket
             */
            public ClientWorker(Socket csocket) {
                super();
                this.csocket = csocket;
            }


            @Override
            public void run() {

                try {
                    istream = new BufferedInputStream(csocket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    ostream = new BufferedOutputStream(csocket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                Optional<byte[]> request;
                try {
                    request = readRequest();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(request.isEmpty()) {
                    return;
                }
                String requestStr = new String(
                        request.get(),
                        StandardCharsets.US_ASCII
                );
                List<String> headers = extractHeader(requestStr);

                String[] firstLine = headers.isEmpty() ?
                        null : headers.get(0).split(" ");

                if(firstLine==null || firstLine.length != 3) {
                    try {
                        sendEmptyResponse(ostream, 400, "Bad request");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    readMethod(firstLine);
                } catch (IOException e) {
                   e.printStackTrace();
                   return;
                }

                try {
                    readVersion(firstLine);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                readHost(headers);

                checkSession(headers);

                String path = readPathAndParameters(firstLine[1].trim());

                try {
                    internalDispatchRequest(path, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            /**
             * Utility method for extracting request header from the whole request.
             * @param requestHeader
             * @return List of header lines
             */
            private List<String> extractHeader(String requestHeader) {
                List<String> headers = new ArrayList<String>();
                String currentLine = null;
                for(String s : requestHeader.split("\n")) {
                    if(s.isEmpty()) break;
                    char c = s.charAt(0);
                    if(c==9 || c==32) {
                        currentLine += s;
                    } else {
                        if(currentLine != null) {
                            headers.add(currentLine);
                        }
                        currentLine = s;
                    }
                }
                if(!currentLine.isEmpty()) {
                    headers.add(currentLine);
                }
                return headers;
            }

            /**
             * Utility method for reading HTTP request from input stream
             * @return Optional object which contains request content as byte array, or null if there is no request to read
             * @throws IOException
             */
            private Optional<byte[]> readRequest() throws IOException {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int state = 0;
                l:		while(true) {
                    int b = istream.read();
                    if(b==-1) {
                        if(bos.size()!=0) {
                            throw new IOException("Incomplete header received.");
                        }
                        return Optional.empty();
                    }
                    if(b!=13) {
                        bos.write(b);
                    }
                    switch(state) {
                        case 0:
                            if(b==13) { state=1; } else if(b==10) state=4;
                            break;
                        case 1:
                            if(b==10) { state=2; } else state=0;
                            break;
                        case 2:
                            if(b==13) { state=3; } else state=0;
                            break;
                        case 3:
                            if(b==10) { break l; } else state=0;
                            break;
                        case 4:
                            if(b==10) { break l; } else state=0;
                            break;
                    }
                }
                return Optional.of(bos.toByteArray());
            }

            /**
             * Utility method for reading version of HTTP protocol from the header.
             * @param firstLine
             * @throws IOException
             */
            private void readVersion(String[] firstLine) throws IOException {
                version = firstLine[2].toUpperCase();
                if(!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
                    sendEmptyResponse(ostream, 400, "Bad request");
                }
            }

            /**
             * Utility method for reading HTTP method from the header.
             * @param firstLine
             * @throws IOException
             */
            private void readMethod(String[] firstLine) throws IOException {
                method = firstLine[0].toUpperCase();
                if(!method.equals("GET")) {
                    sendEmptyResponse(ostream, 400, "Bad request");
                }
            }

            /**
             * Utility method for reading host name from the header if there is a line defining it.
             * If there is no such line, host is set to previously defined domainName of the server.
             * @param headers
             * @throws IOException
             */
            private void readHost(List<String> headers) {
                for(int i = 1; i < headers.size(); ++i) {
                    String header = headers.get(i).trim();
                    if(header.startsWith("Host:")) {
                        String hostName = header.substring(5);
                        if(hostName.contains(":")) {
                            host = hostName.substring(0, hostName.indexOf(':'));
                        } else {
                            host = hostName;
                        }
                        break;
                    }
                }
                if(host==null) {
                    host = domainName;
                }
            }

            /**
             * Utility method for reading requested path and parameters.
             * @param requestedPathStr
             * @return requested path
             */
            private String readPathAndParameters(String requestedPathStr) {
                String path ;
                String paramString ;
                if(requestedPathStr.contains("?")) {
                    String[] reqPathArr = requestedPathStr.split("\\?");
                    path = reqPathArr[0];
                    paramString = reqPathArr[1];
                    parseParameters(paramString);
                } else {
                    path = requestedPathStr;
                }
                return path;
            }

            /**
             * Utility method for reading parameters from the request path and storing them in parameters map.
             * @param paramString
             */
            private void parseParameters(String paramString) {
                String[] parameters = paramString.split("&");
                for(String p:parameters) {
                    String[] nameAndValue = p.split("=");
                    params.put(nameAndValue[0], nameAndValue[1]);
                }
            }

            private synchronized void checkSession(List<String> headers) {
                String sidCandidate=null;
                for(String hl : headers) {
                    if(hl.startsWith("Cookie:")) {
                        String cookiesList = hl.substring(hl.indexOf(":")+1).trim();
                        String[] cookies = cookiesList.split(";");
                        for(String cookie:cookies) {
                            String[] nameAndValue = cookie.split("=");
                            if(nameAndValue[0].equals("sid")) {
                                tempParams.put("sidCandidate", nameAndValue[1].substring(1, nameAndValue[1].length()-1));
                                sidCandidate=nameAndValue[1].substring(1, nameAndValue[1].length()-1);
                            }
                        }
                    }
                }
                SessionMapEntry entry = null;
                boolean foundCandidate = false;
                if(sidCandidate!=null) {
                    entry = session.get(sidCandidate);
                    if(entry!=null) {
                        if(entry.host.equals(host)) {
                            if(entry.validUntil < (System.currentTimeMillis()/1000)) {
                                session.remove(sidCandidate);
                            } else {
                                entry.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
                                foundCandidate = true;
                            }
                        }
                    }
                }
                if(!foundCandidate){
                   entry = openSession();
                }

                permPrams = entry.map;

            }

            /**
             * Utility method for opening new session.
             * @return new SessionMapEntry object
             */
            private synchronized SessionMapEntry openSession() {
                String SID = generateSID();
                SessionMapEntry entry = new SessionMapEntry();
                entry.sid = SID;
                entry.map = new  ConcurrentHashMap<>();
                entry.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
                entry.host = host;

                session.put(SID, entry);

                outputCookies.add(new RequestContext.RCCookie("sid", SID, null, host, "/", true));

                return entry;
            }

            /**
             * Utility method for generating random SID.
             * SID is a String made up of 20 random letters.
             * @return
             */
            private String generateSID() {
                String sid = "";
                for(int i = 0; i < 20; ++i) {
                    int c = 65 + (int)(sessionRandom.nextFloat() * (90-65+1));
                    sid += (char)c;
                }
                return sid;
            }

            /**
             * Calls internalDispatchRequest method with given urlPath and directCall argument set to false.
             * @param urlPath
             * @throws Exception
             */
            @Override
            public void dispatchRequest(String urlPath) throws Exception {
                internalDispatchRequest(urlPath, false);
            }

            /**
             * Accepts url of the requested resource and serves the client.
             * @param url
             * @param directCall
             * @throws Exception
             */
            private void internalDispatchRequest(String url, boolean directCall)  throws Exception{
                IWebWorker iww;
                if((iww = workersMap.get(url))!=null) {
                    if(context==null) {
                        context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, null);
                    }
                    iww.processRequest(context);
                    ostream.flush();
                    csocket.close();
                    return;
                }

                if(url.contains("/ext/")) {
                    String worker = url.toString().substring(5);
                    String fqcn = "hr.fer.oprpp2.webserver.workers."+worker;

                    Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
                    Object newObject = referenceToClass.newInstance();
                    IWebWorker iww2 = (IWebWorker) newObject;

                    if(context==null) {
                        context = new RequestContext(ostream, params, permPrams, outputCookies);
                    }
                    iww2.processRequest(context);
                    ostream.flush();
                    csocket.close();

                    return;
                }

                serveFromDocumentRoot(url, directCall);

            }

            /**
             * From this method HTTP server serves static content from it's webroot folder.
             * This method first checks whether requested file is a script, if it is server runs the script and prints the result
             * to the client. If it's not a script than the content is served directly from the webroot folder.
             * @param url
             * @throws IOException
             */
            private void serveFromDocumentRoot(String url, boolean directCall) throws IOException {
                //Path requestedPath = documentRoot.resolve(url);
                Path requestedPath = Path.of(documentRoot+url);
                if(!Files.exists(requestedPath)) {
                    sendEmptyResponse(ostream, 403, "Forbidden");
                    return;
                }

                if(Files.isDirectory(requestedPath) || !Files.isReadable(requestedPath)) {
                    sendEmptyResponse(ostream, 404, "Not found");
                    return;
                }

                if(url.equals("/private") || url.startsWith("/private/")) {
                    if(directCall) {
                        sendEmptyResponse(ostream, 404, "Not found");
                    }
                }

                String extension = url.substring(url.indexOf('.')+1);
                if(extension.equals("smscr")) {
                    runWithSmScrEngine(requestedPath);
                } else {
                    serveStaticContent(extension, requestedPath);
                }
            }

            /**
             * Serves static content from the webroot folder.
             * @param extension
             * @param requestedPath
             * @throws IOException
             */
            private void serveStaticContent(String extension, Path requestedPath) throws IOException {
                String mimeType = mimeTypes.get(extension);
                if(mimeType==null) {
                    mimeType="application/octet-stream";
                }
                if(context==null) {
                    context = new RequestContext(ostream, params, permPrams, outputCookies);
                }
                context.setMimeType(mimeType);

                byte[] data = Files.readAllBytes(requestedPath);

                context.setContentLength((long) data.length);
                context.write(data);
                context.getOutputStream().flush();
            }

            /**
             * Runs the script using SmartScriptEngine.
             * @param file
             * @throws IOException
             */
            private void runWithSmScrEngine(Path file) throws IOException {
                byte[] data = Files.readAllBytes(file);
                String dataStr = new String(data, StandardCharsets.UTF_8);
                //RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
                if(context==null) {
                    //jel ne bi ovdje trebala poslati dispatcher?
                    context = new RequestContext(ostream, params, permPrams, outputCookies);
                }
                SmartScriptEngine engine = new SmartScriptEngine(
                        new SmartScriptParser(dataStr).getDocumentNode(),
                        context
                );
                engine.execute();
                ostream.flush();
                csocket.close();
            }
        }
        }

        private static void sendResponseWithData(OutputStream cos, int statusCode, String statusText, String contentType, byte[] data) throws IOException {

            cos.write(
                    ("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
                            "Server: simple java server\r\n"+
                            "Content-Type: "+contentType+"\r\n"+
                            "Content-Length: "+data.length+"\r\n"+
                            "Connection: close\r\n"+
                            "\r\n").getBytes(StandardCharsets.US_ASCII)
            );

            cos.write(data);
            cos.flush();
        }

        private static void sendEmptyResponse(OutputStream cos, int statusCode, String statusText) throws IOException {
            sendResponseWithData(cos, statusCode, statusText, "text/plain;charset=UTF-8", new byte[0]);
        }

    /**
     * SessionMapEntry class represents one session.
     */
    private static class SessionMapEntry {
            String sid;
            String host;
            long validUntil;
            Map<String, String> map;
        }

        public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
            if (args.length != 1) {
                System.out.println("Expecting only one argument: server configuration file name");
                return;
            }
            SmartHttpServer server = new SmartHttpServer(args[0]);
            server.start();
        }

    }


