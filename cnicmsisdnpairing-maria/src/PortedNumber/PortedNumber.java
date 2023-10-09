package PortedNumber;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pmd.util.StaticConstant;

public class PortedNumber {

	// public static Logger logger = Logger.getLogger(getClass());

	public PortedNumber() {
		super();
	}

	public static String callService(String msisdn) throws Exception {
		String finalvalue = null;
		try {

			String input = "{\"msisdn\":\"" + msisdn + "\"}";

			String url = StaticConstant.PortedNumberURL; // replace your URL here
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(input);
			wr.flush();
			wr.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// You can play with response which is available as string now:
			try {
				finalvalue = response.toString();

				String[] arrOfStr = finalvalue.split(",", 2);

				String arrOfStr1 = arrOfStr[1];
				String[] arrOfStr2 = arrOfStr1.split(":", 2);

				String str11 = arrOfStr2[1].replaceAll("[^a-zA-Z0-9]", "");

				finalvalue = str11.trim();

			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (Exception e) {
			// logger.error(getClass() + " not reachable" + e);
			// throw new Exception(getClass() + " not reachable" + e);
		}
		return finalvalue;
	}

}
