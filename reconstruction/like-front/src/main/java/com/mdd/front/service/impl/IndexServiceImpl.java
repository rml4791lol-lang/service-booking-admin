/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.entity.article.Article
 *  com.mdd.common.entity.decorate.DecoratePage
 *  com.mdd.common.entity.setting.HotSearch
 *  com.mdd.common.mapper.article.ArticleMapper
 *  com.mdd.common.mapper.decorate.DecoratePageMapper
 *  com.mdd.common.mapper.decorate.DecorateTabbarMapper
 *  com.mdd.common.mapper.setting.HotSearchMapper
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.HttpUtils
 *  com.mdd.common.util.ListUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.IDecorateTabbarService
 *  com.mdd.front.service.IIndexService
 *  com.mdd.front.service.impl.IndexServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.entity.article.Article;
import com.mdd.common.entity.decorate.DecoratePage;
import com.mdd.common.entity.setting.HotSearch;
import com.mdd.common.mapper.article.ArticleMapper;
import com.mdd.common.mapper.decorate.DecoratePageMapper;
import com.mdd.common.mapper.decorate.DecorateTabbarMapper;
import com.mdd.common.mapper.setting.HotSearchMapper;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.HttpUtils;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IDecorateTabbarService;
import com.mdd.front.service.IIndexService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.mdd.front.vo.decorateTabbar.DecorateTabbarVo;

@Service
public class IndexServiceImpl
implements IIndexService {
    @Resource
    IDecorateTabbarService iDecorateTabbarService;
    @Resource
    DecoratePageMapper decoratePageMapper;
    @Resource
    DecorateTabbarMapper decorateTabbarMapper;
    @Resource
    HotSearchMapper hotSearchMapper;
    @Resource
    ArticleMapper articleMapper;

    public Map<String, Object> index() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        DecoratePage decoratePage = (DecoratePage)this.decoratePageMapper.selectOne(new QueryWrapper<DecoratePage>().eq("id", (Object)1).last("limit 1"));
        LinkedList articleList = new LinkedList();
        List<Article> articles = this.articleMapper.selectList(new QueryWrapper<Article>().eq("is_show", (Object)1).isNull("delete_time").orderByDesc("id").last("limit 20"));
        for (Article article : articles) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", article.getId());
            map.put("title", article.getTitle());
            map.put("desc", article.getDesc());
            map.put("abstract", article.getAbstractField());
            map.put("image", UrlUtils.toAbsoluteUrl((String)article.getImage()));
            map.put("author", article.getAuthor());
            map.put("click", article.getClickActual() + article.getClickVirtual());
            map.put("create_time", TimeUtils.timestampToDate((Long)article.getCreateTime()));
            articleList.add(map);
        }
        String data = decoratePage.getData();
        JSONArray dataArray = JSONArray.parseArray((String)data, (JSONReader.Feature[])new JSONReader.Feature[0]);
        for (int i = 0; i < dataArray.size(); ++i) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            JSONArray tempArray = dataJson.getJSONObject("content").getJSONArray("data");
            if (StringUtils.isNull(tempArray)) {
                JSONObject tempJSON = dataJson.getJSONObject("content");
                if (StringUtils.isNull(tempJSON)) continue;
                if (StringUtils.isNotNull(tempJSON.getString("image"))) {
                    tempJSON.put("image", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("image")));
                }
                if (StringUtils.isNotNull(tempJSON.getString("bg"))) {
                    tempJSON.put("bg", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("bg")));
                }
                if (!StringUtils.isNotNull(tempJSON.getString("qrcode"))) continue;
                tempJSON.put("qrcode", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("qrcode")));
                continue;
            }
            for (int j = 0; j < tempArray.size(); ++j) {
                JSONObject tempImageJson = tempArray.getJSONObject(j);
                tempImageJson.put("image", (Object)UrlUtils.toAbsoluteUrl((String)tempImageJson.getString("image")));
                tempImageJson.put("bg", (Object)UrlUtils.toAbsoluteUrl((String)tempImageJson.getString("bg")));
            }
        }
        decoratePage.setData(dataArray.toJSONString(new JSONWriter.Feature[0]));
        response.put("domain", UrlUtils.domain());
        response.put("page", decoratePage);
        response.put("article", articleList);
        return response;
    }

    public Map<String, Object> decorate(Integer id) {
        DecoratePage decoratePage = (DecoratePage)this.decoratePageMapper.selectOne(new QueryWrapper<DecoratePage>().eq("id", (Object)id).last("limit 1"));
        Assert.notNull((Object)decoratePage, (String)"\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        String data = decoratePage.getData();
        if (StringUtils.isJsonArrayString((String)data)) {
            JSONArray dataArray = JSONArray.parseArray((String)data, (JSONReader.Feature[])new JSONReader.Feature[0]);
            for (int i = 0; i < dataArray.size(); ++i) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                JSONArray tempArray = dataJson.getJSONObject("content").getJSONArray("data");
                if (StringUtils.isNull(tempArray)) {
                    JSONObject tempJSON = dataJson.getJSONObject("content");
                    if (StringUtils.isNull(tempJSON)) continue;
                    if (StringUtils.isNotNull(tempJSON.getString("image"))) {
                        tempJSON.put("image", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("image")));
                    }
                    if (StringUtils.isNotNull(tempJSON.getString("bg"))) {
                        tempJSON.put("bg", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("bg")));
                    }
                    if (!StringUtils.isNotNull(tempJSON.getString("qrcode"))) continue;
                    tempJSON.put("qrcode", (Object)UrlUtils.toAbsoluteUrl((String)tempJSON.getString("qrcode")));
                    continue;
                }
                for (int j = 0; j < tempArray.size(); ++j) {
                    JSONObject tempImageJson = tempArray.getJSONObject(j);
                    tempImageJson.put("image", (Object)UrlUtils.toAbsoluteUrl((String)tempImageJson.getString("image")));
                    tempImageJson.put("bg", (Object)UrlUtils.toAbsoluteUrl((String)tempImageJson.getString("bg")));
                }
            }
            decoratePage.setData(dataArray.toJSONString(new JSONWriter.Feature[0]));
        }
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("type", decoratePage.getType());
        response.put("name", decoratePage.getName());
        response.put("data", decoratePage.getData());
        response.put("meta", decoratePage.getMeta());
        return response;
    }

    public Map<String, Object> config() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        List<DecorateTabbarVo> tabbar = this.iDecorateTabbarService.getTabbarLists();
        JSONObject style = JSONObject.parse((String)ConfigUtils.get((String)"tabbar", (String)"style"), (JSONReader.Feature[])new JSONReader.Feature[0]);
        JSONObject loginMap = new JSONObject();
        Map<String, String> loginConfig = ConfigUtils.get((String)"login");
        loginMap.put("login_way", (Object)JSONArray.parseArray((String)loginConfig.getOrDefault("login_way", "[]"), (JSONReader.Feature[])new JSONReader.Feature[0]));
        loginMap.put("coerce_mobile", (Object)Integer.parseInt(loginConfig.getOrDefault("coerce_mobile", "0")));
        loginMap.put("login_agreement", (Object)Integer.parseInt(loginConfig.getOrDefault("login_agreement", "0")));
        loginMap.put("third_auth", (Object)Integer.parseInt(loginConfig.getOrDefault("third_auth", "0")));
        loginMap.put("wechat_auth", (Object)Integer.parseInt(loginConfig.getOrDefault("wechat_auth", "0")));
        loginMap.put("qq_auth", (Object)Integer.parseInt(loginConfig.getOrDefault("qq_auth", "0")));
        JSONObject website = new JSONObject();
        website.put("h5_favicon", (Object)UrlUtils.toAbsoluteUrl((String)ConfigUtils.get((String)"website", (String)"h5_favicon", (String)"")));
        website.put("shop_name", (Object)ConfigUtils.get((String)"website", (String)"shop_name", (String)""));
        website.put("shop_logo", (Object)UrlUtils.toAbsoluteUrl((String)ConfigUtils.get((String)"website", (String)"shop_logo", (String)"")));
        Map<String, String> websiteConfig = ConfigUtils.get((String)"copyright");
        String copyright = websiteConfig.getOrDefault("config", "[]");
        List<Map<String, String>> copyrightMap = ListUtils.stringToListAsMapStr((String)copyright);
        JSONObject webPage = new JSONObject();
        webPage.put("status", (Object)Integer.valueOf(ConfigUtils.get((String)"web_page", (String)"status", (String)"1")));
        webPage.put("page_status", (Object)Integer.valueOf(ConfigUtils.get((String)"web_page", (String)"page_status", (String)"0")));
        webPage.put("page_url", (Object)ConfigUtils.get((String)"web_page", (String)"page_url", (String)""));
        webPage.put("url", (Object)(UrlUtils.domain() + "/mobile"));
        response.put("domain", UrlUtils.domain());
        response.put("style", style);
        response.put("tabbar", tabbar);
        response.put("login", loginMap);
        response.put("website", website);
        response.put("webPage", webPage);
        response.put("version", GlobalConfig.version);
        response.put("copyright", copyrightMap);
        return response;
    }

    public Map<String, String> policy(String type) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title", ConfigUtils.get((String)"agreement", (String)(type + "_title"), (String)""));
        map.put("content", ConfigUtils.get((String)"agreement", (String)(type + "_content"), (String)""));
        return map;
    }

    public List<String> hotSearch() {
        String isHotSearch = ConfigUtils.get((String)"search", (String)"isHotSearch", (String)"0");
        LinkedList<String> list = new LinkedList<String>();
        if (Integer.parseInt(isHotSearch) == 1) {
            List<HotSearch> hotSearches = this.hotSearchMapper.selectList(new QueryWrapper<HotSearch>().orderByDesc(Arrays.asList("sort", "id")));
            for (HotSearch hotSearch : hotSearches) {
                list.add(hotSearch.getName());
            }
        }
        return list;
    }

    public Map<String, String> weather(String lat, String lng) {
        HashMap<String, String> result = new HashMap<String, String>();
        if (StringUtils.isBlank((CharSequence)lat) || StringUtils.isBlank((CharSequence)lng) || "0".equals(lat) || "0".equals(lng)) {
            return result;
        }
        try {
            JSONArray lives;
            String weatherUrl;
            String weatherRes;
            JSONObject weatherObj;
            String adcode;
            JSONObject regeocode;
            String key = ConfigUtils.get((String)"amap", (String)"web_key", (String)"599ff1831ba5e1de900f2b7d7ff385e0");
            String regeoUrl = "https://restapi.amap.com/v3/geocode/regeo?location=" + lng + "," + lat + "&key=" + key;
            String regeoRes = HttpUtils.sendGet((String)regeoUrl);
            JSONObject regeoObj = JSONObject.parse((String)regeoRes, (JSONReader.Feature[])new JSONReader.Feature[0]);
            if ("1".equals(regeoObj.getString("status")) && (regeocode = regeoObj.getJSONObject("regeocode")) != null && regeocode.getJSONObject("addressComponent") != null && StringUtils.isNotBlank((CharSequence)(adcode = regeocode.getJSONObject("addressComponent").getString("adcode"))) && "1".equals((weatherObj = JSONObject.parse((String)(weatherRes = HttpUtils.sendGet((String)(weatherUrl = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + adcode + "&key=" + key))), (JSONReader.Feature[])new JSONReader.Feature[0])).getString("status")) && (lives = weatherObj.getJSONArray("lives")) != null && lives.size() > 0) {
                JSONObject live = lives.getJSONObject(0);
                result.put("weather", live.getString("weather"));
                result.put("temperature", live.getString("temperature"));
                result.put("city", live.getString("city"));
                result.put("humidity", live.getString("humidity"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

