package com.valueclickbrands.solr.util;

import java.util.HashMap;

public class ContentTypesMap {
	public static HashMap<String,String> ContentTypesMap=new HashMap<String,String>();//<param value, drupal type>
	public static HashMap<String,String> AdvertisingMap = new HashMap<String,String>();//<advertising,adtarget>
	public static HashMap<String,String> SubAdvertisingMap = new HashMap<String,String>();//<subadvertising,adtarget>
	public static HashMap<String,String> AdTargetMap = new HashMap<String,String>();//<adtarget,dfp>
	public static HashMap<String,Integer> AdvertisingTidMap = new HashMap<String,Integer>();//<adtarget,dfp>
	
	static{
		ContentTypesMap.put("article","Article");
		ContentTypesMap.put("financialedge","Short Article");
		ContentTypesMap.put("term","Term");
		ContentTypesMap.put("faq","FAQ");
		ContentTypesMap.put("tutorial","Tutorial");
		ContentTypesMap.put("walkthrough","Walkthrough");
		ContentTypesMap.put("examprep","Exam Prep");
		ContentTypesMap.put("slideshow","SlideShow");
		ContentTypesMap.put("forexanalysisfeeds","Forex News");
		ContentTypesMap.put("marketanalysisfeeds","Stock Analysis");
		ContentTypesMap.put("video","Video");
		ContentTypesMap.put("chartadvisorarticle","ChartAdvisor Article");
		ContentTypesMap.put("stockanalysisinvestopedia","Stock Analysis");
		ContentTypesMap.put("short article","Short Article");
		ContentTypesMap.put("exam prep","Exam Prep");
		ContentTypesMap.put("slide show","SlideShow");
		ContentTypesMap.put("forex news","Forex News");
		ContentTypesMap.put("stock analysis","Stock Analysis");
		ContentTypesMap.put("chartadvisor article","ChartAdvisor Article");
		ContentTypesMap.put("stock analysis","Stock Analysis");
		
		AdvertisingMap.put("Investing", "investopedia.com");
		AdvertisingMap.put("Forex", "investopedia.com/forex");
		AdvertisingMap.put("Personal Finance", "investopedia.com/pf");
		AdvertisingMap.put("Active Trading", "investopedia.com/trading");
		AdvertisingMap.put("Options & Futures", "investopedia.com/optionsandfutures");
		AdvertisingMap.put("ETFs & Mutual Funds", "investopedia.com/etfmf");
		AdvertisingMap.put("Professionals", "investopedia.com/professionals");
		
		SubAdvertisingMap.put("FXTrader", "investopedia.com/forex/fxtrader");
		SubAdvertisingMap.put("Insurance", "investopedia.com/pf/insurance");
		SubAdvertisingMap.put("Taxes", "investopedia.com/pf/taxes");
		SubAdvertisingMap.put("Retirement", "investopedia.com/pf/retirement");
		SubAdvertisingMap.put("ETF", "investopedia.com/etfmf/etf");
		SubAdvertisingMap.put("Mutual Fund", "investopedia.com/etfmf/mutualfunds");
		SubAdvertisingMap.put("Homepage", "investopedia.com/homepage");
		SubAdvertisingMap.put("Simulator", "investopedia.com/simulator");
		SubAdvertisingMap.put("Bonds", "investopedia.com/bonds");
		SubAdvertisingMap.put("Quizzer", "investopedia.com/professionals/quizzer");
		SubAdvertisingMap.put("FA", "investopedia.com/professionals/fa");
		SubAdvertisingMap.put("Quotes", "investopedia.com/trading/stocks");
		
		AdTargetMap.put("investopedia.com", "Investing/Investing");
		AdTargetMap.put("investopedia.com/bonds", "Investing/Bonds");
		AdTargetMap.put("investopedia.com/etfmf", "ETFMF/ETFMF");
		AdTargetMap.put("investopedia.com/etfmf/etf", "ETFMF/ETF");
		AdTargetMap.put("investopedia.com/etfmf/mutualfunds", "ETFMF/MF");
		AdTargetMap.put("investopedia.com/forex", "Forex/Forex");
		AdTargetMap.put("investopedia.com/forex/fxtrader", "Forex/Forex");
		AdTargetMap.put("investopedia.com/homepage", "Investing/Homepage");
		AdTargetMap.put("investopedia.com/optionsandfutures", "OptionsFutures/OptionsFutures");
		AdTargetMap.put("investopedia.com/pf", "PersonalFinance/PersonalFinance");
		AdTargetMap.put("investopedia.com/pf/insurance", "PersonalFinance/Insurance");
		AdTargetMap.put("investopedia.com/pf/retirement", "PersonalFinance/Retirement");
		AdTargetMap.put("investopedia.com/pf/taxes", "PersonalFinance/Taxes");
		AdTargetMap.put("investopedia.com/professionals", "Professional/Professional");
		AdTargetMap.put("investopedia.com/professionals/fa", "Professional/FinancialAdvisor");
		AdTargetMap.put("investopedia.com/professionals/quizzer", "Professional/Professional");
		AdTargetMap.put("investopedia.com/simulator", "Investing/Investing");
		AdTargetMap.put("investopedia.com/trading", "ActiveTrading/ActiveTrading");
		AdTargetMap.put("investopedia.com/trading/stocks", "ActiveTrading/Stocks");
		

		AdvertisingTidMap.put("Bonds", 308);
		AdvertisingTidMap.put("ETF", 305);
		AdvertisingTidMap.put("FA", 314);
		AdvertisingTidMap.put("FXTrader", 307);
		AdvertisingTidMap.put("Insurance", 311);
		AdvertisingTidMap.put("Quotes", 304);
		AdvertisingTidMap.put("Homepage", 309);
		AdvertisingTidMap.put("Mutual Fund", 306);
		AdvertisingTidMap.put("Quizzer", 315);
		AdvertisingTidMap.put("Retirement", 312);
		AdvertisingTidMap.put("Simulator", 310);
		AdvertisingTidMap.put("Taxes", 313);
		AdvertisingTidMap.put("Active Trading", 297);
		AdvertisingTidMap.put("ETFs & Mutual Funds", 298);
		AdvertisingTidMap.put("Forex", 299);
		AdvertisingTidMap.put("Investing", 300);
		AdvertisingTidMap.put("Options & Futures", 301);
		AdvertisingTidMap.put("Personal Finance", 302);
		AdvertisingTidMap.put("Professionals", 303);
	}
	
	public static String getDrupalContentTypesByParam(String str){
		return ContentTypesMap.get(str);
	}
	
	public static String getAdTargetByAdvertising(String advertising){
		return AdvertisingMap.get(advertising);
	}
	
	public static String getAdTargetBySubAdvertising(String subAdvertising){
		return SubAdvertisingMap.get(subAdvertising);
	}
	
	public static String getDFPByAdTarget(String adTarget){
		return AdTargetMap.get(adTarget);
	}	
	public static long getAdvertisingTid(String Advertising){
		Integer tid = AdvertisingTidMap.get(Advertising);
		if(tid !=null){
			return Long.valueOf(tid);
		}
		return 0;
	}
}
