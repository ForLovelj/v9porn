package com.u9porn.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.pxgav.PxgavModel;
import com.u9porn.data.model.pxgav.PxgavResultWithBlockId;
import com.u9porn.data.model.pxgav.PxgavVideoParserJsonResult;
import com.u9porn.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/22
 */

public class ParsePxgav {
    private static final String TAG = ParsePxgav.class.getSimpleName();

    /**
     * @param html 原网页
     * @return json===
     */
    public static BaseResult<PxgavVideoParserJsonResult> parserVideoUrl(String html) {
        BaseResult<PxgavVideoParserJsonResult> baseResult = new BaseResult<>();
        Document document = Jsoup.parse(html);
        Element videoWrapper = document.getElementsByClass("penci-entry-content entry-content").first();
        String videoHtml = videoWrapper.html();
        Logger.t(TAG).d(videoHtml);
        int index = videoHtml.indexOf("setup") + 6;
        int endIndexV = videoHtml.indexOf(");");
        String videoUrl = videoHtml.substring(index, endIndexV);
        Logger.t(TAG).d(videoUrl);

        PxgavVideoParserJsonResult pxgavVideoParserJsonResult = new Gson().fromJson(videoUrl, PxgavVideoParserJsonResult.class);

        Elements items = document.getElementsByClass("penci-block_content").first().select("article");
        List<PxgavModel> pxgavModelList = new ArrayList<>();
        for (Element element : items) {
            PxgavModel pxgavModel = new PxgavModel();
            Element a = element.selectFirst("a");
            String title = a.attr("title");
            pxgavModel.setTitle(title);
            String contentUrl = a.attr("href");
            pxgavModel.setContentUrl(contentUrl);
            String imgUrl = a.attr("style");

            String bigImg = StringUtils.subString(imgUrl, imgUrl.indexOf("url(") + 4, imgUrl.lastIndexOf("-"));
            Logger.t(TAG).d(bigImg);
            if (TextUtils.isEmpty(bigImg)) {
                pxgavModel.setImgUrl(imgUrl);
            } else {
                pxgavModel.setImgUrl(bigImg + ".jpg");
            }
            int beginIndex = bigImg.lastIndexOf("/");
            int endIndex = bigImg.lastIndexOf("-");
            String pId = StringUtils.subString(imgUrl, beginIndex + 1, endIndex);
            //Logger.t(TAG).d(pId);
            pxgavModel.setpId(pId);

            pxgavModelList.add(pxgavModel);
        }
        pxgavVideoParserJsonResult.setPxgavModelList(pxgavModelList);
        baseResult.setData(pxgavVideoParserJsonResult);
        return baseResult;
    }

    public static BaseResult<PxgavResultWithBlockId> videoList(String html, boolean isLoadMoreData) {
        BaseResult<PxgavResultWithBlockId> baseResult = new BaseResult<>();

        PxgavResultWithBlockId pxgavResultWithBlockId = new PxgavResultWithBlockId();

        baseResult.setTotalPage(1);
        Logger.t(TAG).d(html);
        Document doc = Jsoup.parse(html);
        Elements items = doc.getElementsByClass("penci-block_content").first().select("article");
        List<PxgavModel> pxgavModelList = new ArrayList<>();
        for (Element element : items) {
            PxgavModel pxgavModel = new PxgavModel();
            Element a = element.selectFirst("a");
            String title = a.attr("title");
            pxgavModel.setTitle(title);
            String contentUrl = a.attr("href");
            pxgavModel.setContentUrl(contentUrl);
            String imgUrl = a.attr("style");

            String bigImg = StringUtils.subString(imgUrl, imgUrl.indexOf("url(") + 4, imgUrl.lastIndexOf("-"));
            Logger.t(TAG).d(bigImg);
            if (TextUtils.isEmpty(bigImg)) {
                pxgavModel.setImgUrl(imgUrl);
            } else {
                pxgavModel.setImgUrl(bigImg + ".jpg");
            }
            int beginIndex = bigImg.lastIndexOf("/");
            int endIndex = bigImg.lastIndexOf("-");
            String pId = StringUtils.subString(imgUrl, beginIndex + 1, endIndex);
            //Logger.t(TAG).d(pId);
            pxgavModel.setpId(pId);

            pxgavModelList.add(pxgavModel);
        }
        pxgavResultWithBlockId.setPxgavModelList(pxgavModelList);
        if (isLoadMoreData) {
            baseResult.setData(pxgavResultWithBlockId);
            return baseResult;
        }
        //解析加载更多需要的数据
        Elements elements = doc.getElementsByClass("wpb_wrapper");
        String[] data = elements.last().getElementsByTag("script").html().split(";");
        String label = ".id = \"";
        for (String dat : data) {
            if (dat.contains(label)) {
                int startIndex = dat.indexOf(label);
                Logger.t(TAG).d(dat);
                try {
                    String blockId = dat.substring(startIndex + label.length()).replace("\"", "");
                    pxgavResultWithBlockId.setBlockId(blockId);
                    Logger.t(TAG).d("blockId数据：" + blockId);
                } catch (Exception e) {
                    Logger.t(TAG).e("无法获取blockId");
                }

                break;
            }
        }
        baseResult.setData(pxgavResultWithBlockId);
        return baseResult;
    }

    public static BaseResult<PxgavResultWithBlockId> moreVideoList(String html) {
        BaseResult<PxgavResultWithBlockId> baseResult = new BaseResult<>();
        baseResult.setTotalPage(1);

        Document doc = Jsoup.parse(html);
        Elements items = doc.select("article");
        List<PxgavModel> pxgavModelList = new ArrayList<>();
        for (Element element : items) {
            PxgavModel pxgavModel = new PxgavModel();
            Element a = element.selectFirst("a");
            String title = a.attr("title");
            pxgavModel.setTitle(title);
            String contentUrl = a.attr("href");
            pxgavModel.setContentUrl(contentUrl);
            String imgUrl = a.attr("style");

            String bigImg = StringUtils.subString(imgUrl, imgUrl.indexOf("url(") + 4, imgUrl.lastIndexOf("-"));
            Logger.t(TAG).d(bigImg);
            if (TextUtils.isEmpty(bigImg)) {
                pxgavModel.setImgUrl(imgUrl);
            } else {
                pxgavModel.setImgUrl(bigImg + ".jpg");
            }
            int beginIndex = bigImg.lastIndexOf("/");
            int endIndex = bigImg.lastIndexOf("-");
            String pId = StringUtils.subString(imgUrl, beginIndex + 1, endIndex);
            //Logger.t(TAG).d(pId);
            pxgavModel.setpId(pId);

            pxgavModelList.add(pxgavModel);
        }
        PxgavResultWithBlockId pxgavResultWithBlockId = new PxgavResultWithBlockId();
        pxgavResultWithBlockId.setPxgavModelList(pxgavModelList);
        baseResult.setData(pxgavResultWithBlockId);
        return baseResult;
    }
}
