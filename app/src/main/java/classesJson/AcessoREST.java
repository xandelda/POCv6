package classesJson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.StrictMode;
import android.util.Log;


public class AcessoREST {
	
	 //private int TIMEOUT_MILLISEC = 3000;
	 
	 public String chamadaGET(String url){

	        HttpClient httpclient = new DefaultHttpClient();
	        HttpGet get = new HttpGet(url);
	        String retorno = "";
	        try {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            String responseBody = httpclient.execute(get,responseHandler);
	            retorno = responseBody;
				return retorno;
	        } catch (ClientProtocolException e) {
	        	Log.i("POC", "Erro no GET: ClientProtocol"+e.getLocalizedMessage()+"\n"+e.getMessage());
	        } catch (IOException e) {
	        	Log.i("POC", "Erro no GET: IOException\n\n"+e.getLocalizedMessage()+"\n"+e.getMessage());
	        } catch (Throwable e) {
	        	Log.i("POC", "Erro no GET: Throwable\n\n"+e.toString()+"\n"+e.getLocalizedMessage()+"\n"+e.getMessage());
	        }
	        return "Falha no GET";
	 }










	public String chamadaPOST(String url, String json){

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(json);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer retorno = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				retorno.append(inputLine);
			}

			in.close();
			return retorno.toString();
		} catch (MalformedURLException e) {
			Log.i("POC", "Erro no POST: MalformedURLException"+e.getLocalizedMessage()+"\n"+e.getMessage());
		} catch (ProtocolException e) {
			Log.i("POC", "Erro no POST: ProtocolException"+e.getLocalizedMessage()+"\n"+e.getMessage());
		} catch (IOException e) {
			Log.i("POC", "Erro no POST: IOException"+e.getLocalizedMessage()+"\n"+e.getMessage());
		}
		return "Falha no POST";
	}










}
