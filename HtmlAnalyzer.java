import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlAnalyzer {
    public static void main(String[] args){
        System.out.println("HtmlAnalyzer iniciado.");

        if (args.length != 1) {
            System.out.println("Uso correto: java HtmlAnalyzer <URL>");
            return;
        }

        String urlString = args[0];
        System.out.println("URL recebida: " + urlString);

        // chamanndo o metodo de baixar
        String htmlContent = fetchHtml(urlString);

        if (htmlContent == null) {
            System.out.println("URL connection error");
        }

        System.out.println("Conteúdo HTML obtido:\n" + htmlContent);
    }

    // crinado o métode para baixar o html da url
    private static String fetchHtml(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader .close();

            return content.toString();

        } catch (Exception e) {
            return null;
        }
    }
}
