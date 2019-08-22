package com.u9porn.parser;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.ProxyModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理抓取
 *
 * @author flymegoc
 * @date 2018/1/20
 */

public class ParseProxy {
    private static final String TAG = ParseProxy.class.getSimpleName();

    public static BaseResult<List<ProxyModel>> parseXiCiDaiLi(String html, int page) {
        BaseResult<List<ProxyModel>> baseResult = new BaseResult<>();
        baseResult.setTotalPage(1);
        Document doc = Jsoup.parse(html);

        Element ipList = doc.getElementById("ip_list");
        Elements trs = ipList.select("tr");
        int trSize = trs.size();
        List<ProxyModel> proxyModelList = new ArrayList<>();
        for (int i = 0; i < trSize; i++) {
            //第一是标题，跳过
            if (i == 0) {
                continue;
            }
            //tr里的td
            Elements tds = trs.get(i).select("td");
            ProxyModel proxyModel = new ProxyModel();
            for (int j = 0; j < tds.size(); j++) {
                Element td = tds.get(j);
                switch (j) {
                    case 0:
                        //国家
                        break;
                    case 1:
                        //ip
                        String ip = td.text();
                        proxyModel.setProxyIp(ip);
                        break;
                    case 2:
                        //端口
                        String port = td.text();
                        proxyModel.setProxyPort(port);
                        break;
                    case 3:
                        //城市
                        break;
                    case 4:
                        //匿名度
                        String anonymous = td.text();
                        proxyModel.setAnonymous(anonymous);
                        break;
                    case 5:
                        //类型 http https socket
                        String type = td.text();
                        if ("http".equalsIgnoreCase(type)) {
                            proxyModel.setType(ProxyModel.TYPE_HTTP);
                        } else if ("https".equalsIgnoreCase(type)) {
                            proxyModel.setType(ProxyModel.TYPE_HTTPS);
                        } else {
                            proxyModel.setType(ProxyModel.TYPE_SOCKS);
                        }
                        break;
                    case 6:
                        //速度
                        break;
                    case 7:
                        //连接时间
                        String responseTime = td.select("div").first().attr("title");
                        proxyModel.setResponseTime(responseTime);
                        break;
                    case 8:
                        //存活时间
                        break;
                    case 9:
                        //验证时间
                        break;
                    default:
                }
            }
            proxyModelList.add(proxyModel);
        }
        baseResult.setData(proxyModelList);
        if (page == 1) {
            Elements elements = doc.getElementsByClass("pagination").first().select("a");
            if (elements.size() > 3) {
                String totalPageStr = elements.get(elements.size() - 2).text();
                Logger.t(TAG).d(totalPageStr);
                if (TextUtils.isDigitsOnly(totalPageStr)) {
                    baseResult.setTotalPage(Integer.parseInt(totalPageStr));
                }
            }
        }
        return baseResult;
    }
}
