package rosaria.main;

import rosaria.html.Parser;
import rosaria.http.Crawler;
import rosaria.io.FileUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Rosaria extends Thread {
    private final URL url;
    private final String classname;
    private final int number ;
    public Rosaria(String url, String classname, int number) {
        try{
            this.url = new URL(url);
            this.classname = classname;
            this.number = number;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    public Rosaria(URL url , String classname, int number){
        this.url = url;
        this.classname = classname;
        this.number = number;
    }

    @Override
    public void run(){
        try{
            String  html = Crawler.crawl(url);
            var parser = new Parser(html);
            var body = new Parser(parser.getDivContentByClass(classname));
            String title = body.getTextByTag("h2");
            if (title == null) {
                title = body.getTextByTag("h3");
            }
            String filename = number + "-" + FileUtil.sanitizeFilename(title) + ".txt";
            String text = body.convertHtmlToText();
            if (text != null && !text.isEmpty()) {
                FileUtil.saveTextToFileAsync(text, filename);
                Main.successCnt.getAndIncrement();
            }
        } catch (IOException ignored) {
        }
    }
}
