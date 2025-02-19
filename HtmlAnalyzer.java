import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Stack;

public class HtmlAnalyzer {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Jeito certo de usar: java HtmlAnalyzer <URL>");
            return;
        }

        String urlString = args[0];

        try {
            String htmlContent = fetchHtml(urlString);
            if (htmlContent == null) {
                System.out.println("URL connection error");
                return;
            }

            String deepsestText = extractDeepestText(htmlContent);
            System.out.println(deepsestText);

        } catch (Exception e) {
            System.out.println("URL connection error");
        }
    }

    private static String fetchHtml(String urlString) {
        try {
            URL url = new URI(urlString).toURL();
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
            reader.close();

            return content.toString();

        } catch (Exception e) {
            return null;
        }
    }

    private static String extractDeepestText(String html) {
        Stack<String> tagStack = new Stack<>();
        String deepestestText = "";
        int maxDepth = 0;
        int currentDepth = 0;

        String[] lines = html.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("<") && line.endsWith(">")) {
                if (line.startsWith("</")) {
                    if (!tagStack.isEmpty()) {
                        tagStack.pop();
                        currentDepth--;
                    } else {
                        return "malformed HTML";
                    }
                } else {
                    tagStack.push(line);
                    currentDepth++;
                }
            } else {
                if (currentDepth > maxDepth) {
                    maxDepth = currentDepth;
                    deepestestText = line;
                }
            }
        }
        return tagStack.isEmpty() ? deepestestText : "malformed HTML";
    }
}