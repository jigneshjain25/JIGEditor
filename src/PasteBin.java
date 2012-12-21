import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class PasteBin {
        private String token;
        private String devkey;
        private String loginURL = "http://www.pastebin.com/api/api_login.php";
        private String pasteURL = "http://www.pastebin.com/api/api_post.php";

        public PasteBin(String devkey) {
                this.devkey = devkey;
        }

        public String checkResponse(String response) {
                if (response.substring(0, 15) == "Bad API request")
                        return response.substring(17);
                return "";
        }

        public String login(String username, String password)
                        throws UnsupportedEncodingException {
                String api_user_name = URLEncoder.encode(username, "UTF-8");
                String api_user_password = URLEncoder.encode(password, "UTF-8");
                String data = "api_dev_key=" + this.devkey + "&api_user_name="
                                + api_user_name + "&api_user_password=" + api_user_password;

                String response = this.page(this.loginURL, data);

                String check = this.checkResponse(response);
                if (!check.equals(""))
                        return "false";
                this.token = response;
                return response;
        }

        public String makePaste(String code, String name, String format, String life)
                        throws UnsupportedEncodingException {
                String content = URLEncoder.encode(code, "UTF-8");
                String title = URLEncoder.encode(name, "UTF-8");
                String data = "api_option=paste&api_user_key=" + this.token
                                + "&api_paste_private=0&api_paste_name=" + title
                                + "&api_paste_expire_date="+life+"&api_paste_format=" + format
                                + "&api_dev_key=" + this.devkey + "&api_paste_code=" + content;
                String response = this.page(this.pasteURL, data);
                String check = this.checkResponse(response);
                if (!check.equals(""))
                        return check;
                return response;
        }

        public String page(String uri, String urlParameters) {
                URL url;
                HttpURLConnection connection = null;
                try {
                        // Create connection
                        url = new URL(uri);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type",
                                        "application/x-www-form-urlencoded");

                        connection.setRequestProperty("Content-Length",
                                        "" + Integer.toString(urlParameters.getBytes().length));
                        connection.setRequestProperty("Content-Language", "en-US");

                        connection.setUseCaches(false);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        // Send request
                        DataOutputStream wr = new DataOutputStream(
                                        connection.getOutputStream());
                        wr.writeBytes(urlParameters);
                        wr.flush();
                        wr.close();

                        // Get Response
                        InputStream is = connection.getInputStream();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                        String line;
                        StringBuffer response = new StringBuffer();
                        while ((line = rd.readLine()) != null) {
                                response.append(line);
                        }
                        rd.close();
                        return response.toString();

                } catch (Exception e) {

                        e.printStackTrace();
                        return null;

                } finally {

                        if (connection != null) {
                                connection.disconnect();
                        }
                }
        }

        public void setToken(String token) {
                this.token = token;
        }
}