package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
 
/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 *
 * @author www.codejava.net
 *
 */
public class MultipartUploadUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private OutputStream outputStream;
    private PrintWriter writer;
 
    /**
     * This constructor initializes a new HTTP POST request with content type is
     * set to multipart/form-data.
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUploadUtility(String requestURL, String charset, String urlParameters)
            throws IOException {
 
        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
 
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

        httpConn.setRequestProperty("Content-Length",
				"" + Integer.toString(urlParameters.getBytes().length));
        httpConn.setRequestProperty("Content-Language", "en-US");

        
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; application/x-www-form-urlencoded; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }
 
    /**
     * Add a upload file section to the request
     *
     * @param fieldName
     *            name attribute in <input type="file" name="..." />
     * @param uploadFile
     *            a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
    }
 
    /**
     * Write an array of bytes to the request's output stream.
     */
    public void writeFileBytes(byte[] bytes, int offset, int length)
            throws IOException {
        outputStream.write(bytes, offset, length);
    }
 
    /**
     * Complete the request and receives response from the server.
     *
     * @throws IOException
     *             if any network error occurred or the server returns
     *             non-HTTP_OK status code.
     */
    public void finish() throws IOException {
        outputStream.flush();
        writer.append(LINE_FEED);
        writer.flush();
 
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
 
        // check server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            while (reader.readLine() != null) {
                // do nothing, but necessary to consume response from the server
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
    }
}