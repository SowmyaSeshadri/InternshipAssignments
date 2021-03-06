package com.ztech.parser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ztech.bean.NewsItemBean;

public class RSStoJSON {

	private static Logger logger = Logger.getLogger(RSStoJSON.class.getName());

	public ArrayList<NewsItemBean> getNewsArrayList(String newsCategory) {
		NewsItemBean newsItemBean;
		ArrayList<NewsItemBean> newsArrayList = new ArrayList<NewsItemBean>();
		URL rssURL;
		JSONObject jsonObj;
		JSONArray jsonArray;
		try {
			rssURL = new URL(
					"https://news.google.com/news/rss/headlines/section/topic/" + newsCategory + "?ned=us&hl=en&gl=US");
			BufferedReader br = new BufferedReader(new InputStreamReader(rssURL.openStream()));
			StringBuilder rssStringBuilder = new StringBuilder("");
			String line;
			while ((line = br.readLine()) != null) {
				rssStringBuilder.append(line);
			}
			jsonObj = XML.toJSONObject(rssStringBuilder.toString());
			JSONObject object = (JSONObject) jsonObj.get("rss");
			object = (JSONObject) object.get("channel");
			jsonArray = (JSONArray) object.get("item");
			for (int i = 0; i < jsonArray.length(); i++) {
				newsItemBean = new NewsItemBean();
				object = (JSONObject) jsonArray.get(i);
				String description = object.getString("description");
				Document doc = Jsoup.parse(description);
				Element image = doc.select("img").first();
				String title = jsonArray.getJSONObject(i).getString("title");
				String link = jsonArray.getJSONObject(i).getString("link");
				if(description != null && image != null && link != null) {
					newsItemBean.setLink(link);
					newsItemBean.setImage(image.toString());
					newsItemBean.setTitle(title);
					newsArrayList.add(newsItemBean);	
				}
			}
			return (newsArrayList);
		} catch (MalformedURLException e) {
			logger.warning("The URL is invalid or improper.");
		} catch (JSONException e) {
			logger.warning("Error parsing the data to JSON");
		} catch (IOException e) {
			logger.warning("Error in reading the data from RSS feed.");
		}
		return null;
	}

}
