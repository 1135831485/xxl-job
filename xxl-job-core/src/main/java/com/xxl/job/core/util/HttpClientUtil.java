package com.xxl.job.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * httpclient util
 * @author xuxueli 2015-10-31 19:50:41
 */
public class HttpClientUtil {

	/**
	 * post request
	 */
	public static byte[] postRequest(String reqURL, byte[] date) {
		byte[] responseBytes = null;
		
		HttpPost httpPost = new HttpPost(reqURL);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// init post
			/*if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			}*/
			if (date != null) {
				httpPost.setEntity(new ByteArrayEntity(date, ContentType.DEFAULT_BINARY));
			}
			// do post
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseBytes = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBytes;
	}
	
	/**
	 * read bytes from http request
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	public static final byte[] readBytes(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
        int contentLen = request.getContentLength();
		InputStream is = request.getInputStream();
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					if (readLengthThisTime == -1) {
						break;
					}
					readLen += readLengthThisTime;
				}
				return message;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new byte[] {};
	}
	
}
