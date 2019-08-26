package com.u9porn.parser;


import com.u9porn.data.model.DouBanMeizi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ParseDouBanMeiZi {
    public static List<DouBanMeizi> JsoupDoubanMeizi(String html, int cid) {

        List<DouBanMeizi> list = new ArrayList<>();

        try {

            Document parse = Jsoup.parse(html);
            Elements elements = parse.select("div[class=thumbnail]>div[class=img_single]>a>img");
            DouBanMeizi meizi;
            for (Element e : elements) {
                String src = e.attr("src");
                String title = e.attr("title");

                meizi = new DouBanMeizi();
                meizi.setUrl(src);
                meizi.setTitle(title);
                meizi.setType(cid);

                list.add(meizi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }
}
