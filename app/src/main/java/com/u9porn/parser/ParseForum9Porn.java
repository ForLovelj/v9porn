package com.u9porn.parser;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.u9porn.adapter.BaseHeaderAdapter;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.F9PornContent;
import com.u9porn.data.model.F9PronItem;
import com.u9porn.data.model.PinnedHeaderEntity;
import com.u9porn.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * @author flymegoc
 * @date 2018/1/23
 */

public class ParseForum9Porn {
    private static final String TAG = ParseForum9Porn.class.getSimpleName();

    public static BaseResult<List<PinnedHeaderEntity<F9PronItem>>> parseIndex(String html) {
        BaseResult<List<PinnedHeaderEntity<F9PronItem>>> baseResult = new BaseResult<>();
        Document doc = Jsoup.parse(html);
        Elements tds = doc.getElementsByAttributeValue("background", "images/listbg.gif");
        List<PinnedHeaderEntity<F9PronItem>> forum91PornItemSectionList = new ArrayList<>();
        for (Element td : tds) {
            Elements elements = td.select("a");
            if (td.select("a").first().attr("title").contains("最新精华")) {
                PinnedHeaderEntity<F9PronItem> pronItemPinnedHeaderEntity = new PinnedHeaderEntity<>(null, BaseHeaderAdapter.TYPE_HEADER, "最新精华");
                forum91PornItemSectionList.add(pronItemPinnedHeaderEntity);
            } else if (td.select("a").first().attr("title").contains("最新回复")) {
                PinnedHeaderEntity<F9PronItem> pronItemPinnedHeaderEntity = new PinnedHeaderEntity<>(null, BaseHeaderAdapter.TYPE_HEADER, "最新回复");
                forum91PornItemSectionList.add(pronItemPinnedHeaderEntity);
            } else {
                PinnedHeaderEntity<F9PronItem> pronItemPinnedHeaderEntity = new PinnedHeaderEntity<>(null, BaseHeaderAdapter.TYPE_HEADER, "本周热门");
                forum91PornItemSectionList.add(pronItemPinnedHeaderEntity);
            }

            for (Element element : elements) {
                F9PronItem f9PronItem = new F9PronItem();
                String allInfo = element.attr("title").replaceAll("\n", "");
                int titleIndex = allInfo.indexOf("主题标题:");
                int authorIndex = allInfo.indexOf("主题作者:");
                int authorPublishTimeIndex = allInfo.indexOf("发表时间:");
                int viewCountIndex = allInfo.indexOf("浏览次数:");
                int replyCountIndex = allInfo.indexOf("回复次数:");
                int lastPostTimeIndex = allInfo.indexOf("最后回复:");
                int lastPostAuthorIndex = allInfo.indexOf("最后发表:");

                String title = StringUtils.subString(allInfo, titleIndex + 5, authorIndex);
                String author = StringUtils.subString(allInfo, authorIndex + 5, authorPublishTimeIndex);
                String authorPublishTime = StringUtils.subString(allInfo, authorPublishTimeIndex + 5, viewCountIndex);
                String viewCount = StringUtils.subString(allInfo, viewCountIndex + 5, replyCountIndex).replace("次", "").trim();
                String replyCount = StringUtils.subString(allInfo, replyCountIndex + 5, lastPostTimeIndex).replace("次", "").trim();
                String lastPostTime = StringUtils.subString(allInfo, lastPostTimeIndex + 5, lastPostAuthorIndex);
                String lastPostAuthor = StringUtils.subString(allInfo, lastPostAuthorIndex + 5, allInfo.length());

                f9PronItem.setLastPostTime(lastPostTime);
                f9PronItem.setLastPostAuthor(lastPostAuthor);
                f9PronItem.setTitle(title);
                f9PronItem.setAuthor(author);
                f9PronItem.setViewCount(Long.parseLong(viewCount));
                f9PronItem.setReplyCount(Long.parseLong(replyCount));
                f9PronItem.setAuthorPublishTime(authorPublishTime);


                String contentUrl = element.attr("href");
                int starIndex = contentUrl.indexOf("tid=");
                String tidStr = StringUtils.subString(contentUrl, starIndex + 4, contentUrl.length());
                if (!TextUtils.isEmpty(tidStr) && TextUtils.isDigitsOnly(tidStr)) {
                    f9PronItem.setTid(Long.parseLong(tidStr));
                }

                PinnedHeaderEntity<F9PronItem> pronItemPinnedHeaderEntity = new PinnedHeaderEntity<>(f9PronItem, BaseHeaderAdapter.TYPE_DATA, "");
                forum91PornItemSectionList.add(pronItemPinnedHeaderEntity);
            }
        }
        baseResult.setData(forum91PornItemSectionList);
        return baseResult;
    }

    public static BaseResult<List<F9PronItem>> parseForumList(String html, int currentPage) {
        BaseResult<List<F9PronItem>> baseResult = new BaseResult<>();
        baseResult.setTotalPage(1);
        Document doc = Jsoup.parse(html);
        Element table = doc.getElementsByClass("datatable").first();
        Elements tbodys = table.select("tbody");
        List<F9PronItem> f9PronItemList = new ArrayList<>();
        boolean contentStart = false;
        for (Element tbody : tbodys) {
            F9PronItem f9PronItem = new F9PronItem();

            Element th = tbody.select("th").first();

            if (!contentStart && currentPage == 1) {
                if (th.text().contains("版块主题")) {
                    contentStart = true;
                }
                continue;
            }

            if (th != null) {
                String title = th.select("a").first().text();
                f9PronItem.setTitle(title);
                String contentUrl = th.select("a").first().attr("href");
                int starIndex = contentUrl.indexOf("tid=");
                int endIndex = contentUrl.indexOf("&");
                String tidStr = StringUtils.subString(contentUrl, starIndex + 4, endIndex);
                if (!TextUtils.isEmpty(tidStr) && TextUtils.isDigitsOnly(tidStr)) {
                    f9PronItem.setTid(Long.parseLong(tidStr));
                } else {
                    Logger.t(TAG).d("tidStr::" + tidStr);
                }
                Elements imageElements = th.select("img");
                List<String> stringList = null;
                for (Element element : imageElements) {
                    if (stringList == null) {
                        stringList = new ArrayList<>();
                    }
                    stringList.add(element.attr("src"));
                }
                f9PronItem.setImageList(stringList);

                Elements agreeElements = th.select("font");
                if (agreeElements != null && agreeElements.size() >= 1) {
                    String agreeCount = th.select("font").last().text();
                    f9PronItem.setAgreeCount(agreeCount);
                } else {
                    Logger.t(TAG).d("can not parse agreeCount");
                }
            }

            Elements tds = tbody.select("td");
            for (Element td : tds) {
                switch (td.className()) {
                    case "folder":
                        String folder = td.select("img").attr("src");
                        f9PronItem.setFolder(folder);
                        break;
                    case "icon":
                        Element iconElement = td.select("img").first();
                        if (iconElement != null) {
                            String icon = iconElement.attr("src");
                            f9PronItem.setIcon(icon);
                        }
                        break;
                    case "author":
                        String author = td.select("a").first().text();
                        String authorPublishTime = td.select("em").first().text();
                        f9PronItem.setAuthor(author);
                        f9PronItem.setAuthorPublishTime(authorPublishTime);
                        break;
                    case "nums":
                        String replyCount = td.select("strong").first().text();
                        String viewCount = td.select("em").first().text();
                        if (!TextUtils.isEmpty(replyCount) && TextUtils.isDigitsOnly(replyCount)) {
                            f9PronItem.setReplyCount(Long.parseLong(replyCount));
                        }
                        if (!TextUtils.isEmpty(viewCount) && TextUtils.isDigitsOnly(viewCount)) {
                            f9PronItem.setViewCount(Long.parseLong(viewCount));
                        }
                        break;
                    case "lastpost":
                        String lastPostAuthor = td.select("a").first().text();
                        String lastPostTime = td.select("em").first().text();
                        f9PronItem.setLastPostAuthor(lastPostAuthor);
                        f9PronItem.setLastPostTime(lastPostTime);
                        break;
                    default:
                }

            }
            f9PronItemList.add(f9PronItem);
        }
        if (currentPage == 1) {
            Element pageElement = doc.getElementsByClass("pages").first();
            String page = pageElement.getElementsByClass("last").first().text().replace("...", "").trim();
            Logger.t(TAG).d("totalPage:::" + page);
            if (!TextUtils.isEmpty(page) && TextUtils.isDigitsOnly(page)) {
                baseResult.setTotalPage(Integer.parseInt(page));
            }
        }
        baseResult.setData(f9PronItemList);
        return baseResult;
    }

    public static BaseResult<F9PornContent> parseContent(String html, boolean isNightModel, String baseUrl) {
        BaseResult<F9PornContent> baseResult = new BaseResult<>();
        Document doc = Jsoup.parse(html);
        //尝试解析header中的地址
        Element linkTag = doc.select("link").first();
        String address = "";
        if (linkTag != null) {
            String href = linkTag.attr("href");
            address = StringUtils.subString(href, 0, href.indexOf("archiver"));
        }
        //如果成功解析到地址且和原地址不同，则替换

        if (!TextUtils.isEmpty(address) && !address.equals(baseUrl)) {
            HttpUrl newUrl = HttpUrl.parse(address);
            HttpUrl oldUrl = HttpUrl.parse(baseUrl);
            //要区分http 和https ,只比对域名部分
            if (newUrl != null && oldUrl != null && !newUrl.host().equals(oldUrl.host())) {
                baseUrl = address;
                Logger.t(TAG).e("替换前缀地址为::::" + baseUrl);
            } else {
                Logger.t(TAG).e("域名一致，无需替换::::" + baseUrl);
            }

        }
        Element content = doc.getElementsByClass("t_msgfontfix").first();

        if (content == null) {
            List<String> stringList = new ArrayList<>();
            F9PornContent f9PornContent = new F9PornContent();
            f9PornContent.setImageList(stringList);
            f9PornContent.setContent("暂不支持解析该网页类型或者帖子已被封禁了");
            baseResult.setData(f9PornContent);
            return baseResult;
        }
        Elements attachPopups = doc.getElementsByClass("imgtitle");
        if (attachPopups != null) {
            for (Element element : attachPopups) {
                element.html("");
            }
        }
        //去掉段落样式
        Elements ps = content.select("p");
        for (Element p : ps) {
            p.attr("style", "");
        }
        //去掉字体大小以及适配夜间模式
        Elements fonts = content.select("font");
        for (Element font : fonts) {
            font.attr("style", "font-size: 16px");
            font.attr("size", "3");
            if (isNightModel) {
                font.attr("color", "#5ACC87");
            }
        }
        //调整图片
        Elements imagesElements = content.select("img");
        List<String> stringList = new ArrayList<>();
        for (Element element : imagesElements) {
            //优先提取src里面的值
            String imgUrl = element.attr("src");
            //只替换不为空且结尾为.jpg 但链接不完整的
            boolean canUserSrcValue = !TextUtils.isEmpty(imgUrl) && imgUrl.endsWith(".jpg") && !imgUrl.startsWith("http");
            if (canUserSrcValue) {
                imgUrl = baseUrl + imgUrl;
                element.attr("src", imgUrl);
                stringList.add(imgUrl);
            } else {
                String fileValue = element.attr("file");
                if (!TextUtils.isEmpty(fileValue)) {
                    HttpUrl httpUrl = HttpUrl.parse(fileValue);
                    if (httpUrl != null) {
                        //如果是完整的连接就不要拼接了
                        imgUrl = element.attr("file");
                    } else {
                        imgUrl = baseUrl + element.attr("file");
                    }
                    element.attr("src", imgUrl);
                    stringList.add(imgUrl);
                }
            }
            Logger.t(TAG).e("最终图片地址::::" + imgUrl);
            element.attr("width", "100%");
            element.attr("style", "margin-top: 1em;");
            element.attr("alt", "[图片无法加载...]");
            element.attr("onclick", "HostApp.toast(\"" + imgUrl + "\")");
        }

        F9PornContent f9PornContent = new F9PornContent();
        String contentStr = content.html().replace("<dd", "<dt").replace("</dd>", "</dt>");
        f9PornContent.setContent(contentStr);
        f9PornContent.setImageList(stringList);
        baseResult.setData(f9PornContent);
        return baseResult;
    }
}
