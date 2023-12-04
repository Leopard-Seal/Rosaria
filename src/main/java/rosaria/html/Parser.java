package rosaria.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class Parser {
    public final String html;
    public final Document docs;
    public Parser(String html){
        this.html = html;
        this.docs = Jsoup.parse(html);
    }

    // HTMLをプレーンテキストに変換するメソッド
    public String convertHtmlToText() {
        return convertNodeToText(docs.body());
    }

    private String convertNodeToText(Node node) {
        StringBuilder text = new StringBuilder();
        for (Node childNode : node.childNodes()) {
            if (childNode instanceof TextNode) {
                TextNode textNode = (TextNode) childNode;
                text.append(textNode.getWholeText());
            } else if (childNode instanceof Element) {
                Element element = (Element) childNode;
                String tagName = element.tagName().toLowerCase();
                if (tagName.equals("br") || tagName.equals("p")) {
                    text.append("\n");
                }
                text.append(convertNodeToText(childNode));
                if (tagName.equals("p")) {
                    text.append("\n");
                }
            }
        }
        return text.toString().trim();
    }

    // Method to extract HTML content of a specific div class
    public String getDivContentByClass(String classname){
        Elements elements = docs.select("div." + classname);
        return elements.html();  // This will return the HTML content
    }

    public String getTextByTag(String tagName) {
        Elements elements = docs.getElementsByTag(tagName);
        return elements.text();
    }

    public String getElementsByAttribute(String attributeName) {
        Elements elements = docs.getElementsByAttribute(attributeName);
        return elements.toString();
    }

    public String getElementByAttribute(String attributeName) {
        Elements elements = docs.getElementsByAttribute(attributeName);
        return elements.first().text();
    }

    public String getElementsByAttributeValue(String attribute, String value) {
        Elements elements = docs.getElementsByAttributeValue(attribute, value);
        return elements.toString();
    }

    public String getAttributeValue(String selector, String attribute) {
        Element element = docs.select(selector).first();
        return element != null ? element.attr(attribute) : "";
    }


}
