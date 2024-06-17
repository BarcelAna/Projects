package hr.fer.oprpp2.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * RequestContext objects contains all important information of some HTTP request such as used encoding, mime type, response status code and text, content length exc.
 */
public class RequestContext {

    /**
     * output stream for writing request data
     */
    private OutputStream outputStream;

    /**
     * used charset
     */
    private Charset charset;

    /**
     * used encoding
     */
    public String encoding = "UTF-8";

    /**
     * response status code
     */
    public int statusCode = 200;

    /**
     * response status text
     */
    public String statusText = "OK";

    /**
     * mime type of requested data
     */
    public String mimeType = "text/html";

    /**
     * content length
     */
    public Long contentLength = null;

    /**
     * read only map of parameters
     */
    private Map<String, String> parameters;

    /**
     * temporary parameters
     */
    private Map<String, String>  temporaryParameters;

    /**
     * persistent parameters
     */
    private Map<String, String> persistentParameters;

    /**
     * output cookies
     */
    private List<RCCookie> outputCookies;

    /**
     * flag which indicates whether header is generated or not
     */
    private boolean headerGenerated = false;

    /**
     * session identifier
     */
    private String sid;

    /**
     * dispatcher
     */
    private IDispatcher dispatcher;

    /**
     * Constructor which sets all given parameters.
     * @param outputStream
     * @param parameters
     * @param persistentParameters
     * @param outputCookies
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies){
        this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
    }

    /**
     * Constructor which sets all given parameters.
     * @param outputStream
     * @param parameters
     * @param persistentParameters
     * @param outputCookies
     * @param temporaryParameters
     * @param dispatcher
     * @param sid
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies, Map<String, String> temporaryParameters, IDispatcher dispatcher, String sid){
        if(outputStream==null) {
            throw new NullPointerException("Output stream must not be null.");
        }
        this.outputStream = outputStream;
        this.parameters = parameters!=null ? parameters : Collections.unmodifiableMap(new HashMap<>());
        this.persistentParameters = persistentParameters!=null ? persistentParameters : new HashMap<>();
        this.outputCookies = outputCookies!=null ? outputCookies : new ArrayList<>();
        this.temporaryParameters = temporaryParameters!=null?temporaryParameters: new HashMap<>();
        this.sid = sid;
        this.dispatcher = dispatcher;
    }

    /**
     * Returns value of parameter of given name from parameters map.
     * @param name
     * @return parameter value
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * Returns set of parameter names
     * @return collection of parameter names
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    /**
     * Returns values of parameter from persistentParameters map
     * @param name
     * @return parameter value or null if there is no such parameter
     */
    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    /**
     * Returns the names of persistent parameters.
     * @return set of parameter names
     */
    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    /**
     * Stores new persistent parameter with given name and value.
     * @param name
     * @param value
     */
    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    /**
     * Removes persistent parameter with given name.
     * @param name
     */
    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    /**
     * Returns value of temporary parameter with given name
     * @param name
     * @return parameter value
     */
    public String getTemporaryParameter(String name) {
        return temporaryParameters.get(name);
    }

    /**
     * Returns set of temporary parameter names.
     * @return collection of temporary parameter names.
     */
    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    /**
     * Returns session identifier.
     * @return sid
     */
    public String getSessionID() {
        return sid;
    }

    /**
     * Stores new temporary parameter with given name and value.
     * @param name
     * @param value
     */
    public void setTemporaryParameter(String name, String value) {
        temporaryParameters.put(name, value);
    }

    /**
     * Removes temporary parameter with given name.
     * @param name
     */
    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    /**
     * Returns dispatcher
     * @return IDispatcher object
     */
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Returns output stream
     * @return output stream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Returs used charset
     * @return charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Sets encoding to the given value if header is not already generated.
     * @param encoding
     * @throws RuntimeException - if header is already set.
     */
    public void setEncoding(String encoding) {
        if(headerGenerated) {
            throw new RuntimeException("Can't set this parameter because header is already set.");
        }
        this.encoding = encoding;
    }

    /**
     * Sets status code to the given value if header is not already generated.
     * @param statusCode
     * @throws RuntimeException - if header is already set.
     */
    public void setStatus(int statusCode) {
        if(headerGenerated) {
            throw new RuntimeException("Can't set this parameter because header is already set.");
        }
        this.statusCode = statusCode;
    }

    /**
     * Sets status text to the given value if header is not already generated.
     * @param statusText
     * @throws RuntimeException - if header is already set.
     */
    public void setStatusText(String statusText) {
        if(headerGenerated) {
            throw new RuntimeException("Can't set this parameter because header is already set.");
        }
        this.statusText = statusText;
    }

    /**
     * Sets mime type to the given value if header is not already generated.
     * @param mimeType
     * @throws RuntimeException - if header is already set.
     */
    public void setMimeType(String mimeType) {
        if(headerGenerated) {
            throw new RuntimeException("Can't set this parameter because header is already set.");
        }
        this.mimeType = mimeType;
    }

    /**
     * Adds given RCCookie to the list of output cookies if header is not already generated.
     * @param cookie
     * @throws RuntimeException - if header is already generated
     */
    public void addRCCookie(RCCookie cookie) {
        if(headerGenerated) {
            throw new RuntimeException("Can't set this parameter because header is already set.");
        }
        outputCookies.add(cookie);
    }

    /**
     * Sets contentLength to the given value if header is not already generated.
     * @param contentLength
     * @throws RuntimeException - if header is already set.
     */
    public void setContentLength(Long contentLength) {
        if(headerGenerated) {
            throw new RuntimeException("Can't set this parameter because header is already set.");
        }
        this.contentLength = contentLength;
    }

    /**
     * Writes given data to the output stream.
     * If header is not generated yet, this method first generates and writes header to the stream and then writes the given data.
     * @param data
     * @return RequestContext object
     * @throws IOException
     */
    public RequestContext write(byte[] data) throws IOException {
        return write(data, 0, data.length);
    }

    /**
     * Writes length of the given data to the output stream, starting from the given offset.
     * If header is not generated yet, this method first generates and writes header to the stream and then writes the given data.
     * @param data
     * @return RequestContext object
     * @throws IOException
     */
    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        if(!headerGenerated) {
            generateHeader();
        }
        outputStream.write(data, offset, len);
        return this;
    }

    /**
     * Writes given text to the output stream.
     * If header is not generated yet, this method first generates and writes header to the stream and thenwrites the given data.
     * @param text
     * @return RequestContext object
     * @throws IOException
     */
    public RequestContext write(String text) throws IOException {
        byte[] data = text.getBytes(encoding);
        return write(data, 0 , data.length);
    }

    /**
     * Generates request header and writes it to the output.
     * @throws IOException
     */
    public void generateHeader() throws IOException {
        StringBuilder sb = new StringBuilder();
        this.charset = Charset.forName(encoding);

        sb.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
        sb.append("Content-Type: " + mimeType);
        if(mimeType.startsWith("text/")) {
            sb.append("; charset=" + encoding);
        }
        sb.append("\r\n");
        if(contentLength!=null) {
            sb.append("Content-Length: " + contentLength+"\r\n");
        }
        if(!outputCookies.isEmpty()) {
            writeCookies(sb);
        }
        sb.append("\r\n");

        outputStream.write(sb.toString().getBytes("ISO-8859-1"));
        headerGenerated = true;

    }

    /**
     * Utility method for writing stored cookies to the header if such exists.
     * @param sb
     */
    private void writeCookies(StringBuilder sb) {
        for(RCCookie c : outputCookies) {
            sb.append("Set-Cookie: " + c.name + "=\"" + c.value + "\"");
            if(c.domain!=null) {
                sb.append("; Domain=" + c.domain);
            }
            if(c.path!=null) {
                sb.append("; Path=" + c.path);
            }
            if(c.maxAge!=null) {
                sb.append("; Max-Age=" + c.maxAge);
            }
            sb.append("\r\n");
        }
    }

    /**
     * Class representing one session cookie.
     * Each cookie has name, value, domain, path, maxAge and httpOnly property.
     */
    public static class RCCookie {
        private String name;
        private String value;
        private String domain;
        private String path;
        private Integer maxAge;
        private boolean httpOnly;

        public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
            this.httpOnly = httpOnly;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getDomain() {
            return domain;
        }

        public String getPath() {
            return path;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public boolean isHttpOnly() {
            return httpOnly;
        }
    }
}
