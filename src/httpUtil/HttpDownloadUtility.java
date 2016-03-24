package httpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import ui.UIFileUploadHTTP;

/**
 * A utility that downloads a file from a URL.
 * 
 * @author www.codejava.net
 *
 */
public class HttpDownloadUtility extends SwingWorker<Void, Void> {
	private static final int BUFFER_SIZE = 4096;
	private static UIFileUploadHTTP gui;
	private String fileName = "";
	private String disposition;
	private String contentType;
	private int contentLength;
	private InputStream inputStream;
	private HttpURLConnection httpConn;

	/**
	 * Downloads a file from a URL
	 * 
	 * @param fileURL
	 *            HTTP URL of the file to be downloaded
	 * @param saveDir
	 *            path of the directory to save the file
	 * @throws IOException
	 */
	public String downloadFile(String fileURL, String saveDir, String sessionKey, UIFileUploadHTTP ui) {
		try {
			gui = ui;
			URL url = new URL(fileURL);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			int responseCode = httpConn.getResponseCode();

			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
				fileName = "";
				disposition = httpConn.getHeaderField("Content-Disposition");
				contentType = httpConn.getContentType();
				int contentLength = httpConn.getContentLength();

				if (disposition != null) {
					// extracts file name from header field
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						fileName = disposition.substring(index + 10, disposition.length() - 1);
					}
				} else {
					// extracts file name from URL
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
				}

				// opens input stream from the HTTP connection

				inputStream = httpConn.getInputStream();
				String saveFilePath = saveDir + File.separator + fileName;

				// opens an output stream to save into file
				FileOutputStream outputStream = new FileOutputStream(saveFilePath);

				long totalBytesRead = 0;
				int percentCompleted = 0;
				long fileSize = contentLength;

				int bytesRead = -1;
				byte[] buffer = new byte[BUFFER_SIZE];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
					totalBytesRead += bytesRead;
					percentCompleted = (int) (totalBytesRead * 100 / fileSize);

					setProgress(percentCompleted);
				}
				inputStream.close();
				outputStream.close();

				System.out.println("File downloaded");
				httpConn.disconnect();
				return "OK";
			} else {
				System.out.println("No file to download. Server replied HTTP code: " + responseCode);
				httpConn.disconnect();
				return "ERROR";
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(gui.getFrame(), "Error downloading file: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			setProgress(0);
			cancel(true);
		}

		return null;
	}

	public String getFileName() {
		return this.fileName;
	}

	public int getContentLength() {
		return this.contentLength;
	}

	public String getContentType() {
		return this.contentType;
	}

	public InputStream getInputStream() {
		return this.inputStream;
	}

	@Override
	protected void done() {
		if (!isCancelled()) {
			JOptionPane.showMessageDialog(gui.getFrame(), "File has been downloaded successfully!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void disconnect() throws IOException {
		inputStream.close();
		httpConn.disconnect();
	}

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}