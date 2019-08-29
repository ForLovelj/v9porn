package com.u9porn.parser;

import com.orhanobut.logger.Logger;
import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.data.model.kedouwo.KeDouRelated;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseKeDouWo {

    public static List<KeDouModel> parseVideoList(String html) {
        Document document = Jsoup.parse(html);
//        Elements recentItems = shareDoc.select("#list_videos_most_recent_videos_items").select(".item");
//        Elements recentItems = document.select("#list_videos_latest_videos_list_items").select(".item");
//        Elements recentItems = document.select("#list_videos_common_videos_list_items").select(".item");
        Elements recentItems = document.select("div.list-videos").first().select(".item");
        return parseList(recentItems);
    }

    public static KeDouRelated parseVideoDetail(String html) {
        KeDouRelated keDouRelated = new KeDouRelated();
        if (html.contains("您已超过观看限制")) {
            Logger.d("已经超出观看上限了");
            //设置标志位
            keDouRelated.setOutOfWatch(true);
        }
        Document document = Jsoup.parse(html);
        Element first = document.select("div.player-holder").first();
        String data = first.data();

//        final String reg = "(video_url+):\\s?(.+)(.mp4/)";
        final String reg = "(video_url+):\\s?(.+)(\\?br=\\d+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(data);
        if (m.find()) {
            String group = m.group();
//            String videoUrl = group.substring(group.indexOf("'")+1,group.lastIndexOf("/"));
            String videoUrl = group.substring(group.indexOf("'")+1);
            Logger.d("videoUrl: "+videoUrl);
            keDouRelated.setVideoUrl(videoUrl);
        }

        Elements relatedList = document.select("#list_videos_related_videos_items").first().select(".item");
        List<KeDouModel> keDouModels = parseList(relatedList);
        keDouRelated.setRelatedList(keDouModels);

        return keDouRelated;
    }

    private static List<KeDouModel> parseList(Elements elements) {
        List<KeDouModel> keDouModels = new ArrayList<>();
        for (Element item : elements) {
            KeDouModel keDouModel = new KeDouModel();
            String title = item.getElementsByClass("title").first().text();
            String imgUrl = item.select("img").first().attr("data-original");
            String duration = item.getElementsByClass("duration").first().text();
            String contentUrl = item.select("a").first().attr("href");
            Element ratingPositive = item.getElementsByClass("rating positive").first();
            Element ratingNegative = item.getElementsByClass("rating negative").first();

            String rating;
            if (ratingPositive != null) {
                rating = ratingPositive.text();
            } else {
                rating = ratingNegative.text();
            }
            String added = item.select(".added").first().text();
            String views = item.select(".views").first().text();

            String substring = contentUrl.substring(contentUrl.indexOf("videos/") + 7);
            String viewId = substring.substring(0, substring.indexOf("/"));

            keDouModel.setTitle(title);
            keDouModel.setImgUrl(imgUrl);
            keDouModel.setDuration(duration);
            keDouModel.setContentUrl(contentUrl);
            keDouModel.setRating(rating);
            keDouModel.setAdded(added);
            keDouModel.setViews(views);
            keDouModel.setViewId(viewId);
            keDouModels.add(keDouModel);
        }
        return keDouModels;
    }

    public static String parseRealVideoUrl(String html) {
        Document document = Jsoup.parse(html);
        String videoUrl = document.select("video").first().select("source").first().attr("src");
        return videoUrl;
    }

    public static String getRedirectUrl(String path) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(path)
                .openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        String location = conn.getHeaderField("Location");
        conn.disconnect();
        return location;
    }
}
