package siap.web;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

public class SiapHttpClient
{
  public SiapHttpClient()
  {
  }

//==============================================================================
// Tale metodo effettua una chiamanta in POST HTTP all'url speficicato in input
// inviando il content specificato
// Restituisce la risposta sotto forma di stringa
// In caso di errore nell'invocazione genera una eccezione
//==============================================================================
  public String postMethod(String serverUrl, String content) throws Exception
  {
    //if (1==1) return "";
    
    String response = "";
    // Create an instance of HttpClient.
    HttpClient client = new HttpClient();

    // Create a method instance.
    PostMethod post = new PostMethod(serverUrl);

    // Provide custom retry handler is necessary
    // da approfondire il significato di questi parametri everificare se sono necessari
    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
    retryhandler.setRequestSentRetryEnabled(false);
    retryhandler.setRetryCount(3);
    post.setMethodRetryHandler(retryhandler);
    try
    {
      post.setRequestBody(content);
      // Execute the method.
      int statusCode = client.executeMethod(post);

      if (statusCode != HttpStatus.SC_OK)
      {
        // risposta ricevuta, ma con errore HTTP
        throw new Exception("Method failed: " + post.getStatusLine());
      }
      // Read the response body.
      byte[] responseBody = post.getResponseBody();
      // Deal with the response.
      // Use caution: ensure correct character encoding and is not binary data
      response = new String(responseBody);
    }
    catch (IOException e)
    {
      System.err.println("Failed to connect.");
      e.printStackTrace();
      throw e;
    }
    catch (Exception e)
    {
      System.err.println("Method failed Exception: " + post.getStatusLine());
      throw e;
    }
    finally
    {
      // Release the connection.
      post.releaseConnection();
    }
    return response;
  }

}