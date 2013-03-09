package com.example.testlistview;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.xml.sax.XMLReader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;



public class MainActivity extends Activity implements OnItemClickListener{
	
	private ImageGetter m_imgGetter = new ImageGetter() {
		@Override  
        public Drawable getDrawable(String source) {
			URL url = null;
			Bitmap bitmap = null;
			try {
				url = new URL(source);
			} catch (MalformedURLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return null;
			}
			ImageView image = (ImageView) MainActivity.this.findViewById(R.id.itemImage);			
			bitmap =((BitmapDrawable)image.getDrawable()).getBitmap();   
			
//			File file = ImageViewWithCache.getCachedImgFromUrl(url);
//			if (file != null) {
//				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//				if (bitmap == null)
//					return null;
//			} else {
//				bitmap = ImageViewWithCache.getBitmapFromUrl(url, Api.getInstance().getCookieStorage().getCookies());
//				if (bitmap == null)
//					return null;
//				ImageViewWithCache.cacheBitmapFromUrl(url, bitmap);
//			}
			//bitmap.setDensity(ImageViewWithCache.getDensityDpi(ShowThreadPage.this));
			Drawable drawable = new BitmapDrawable(bitmap);
			drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
			return drawable;
        }  
	};

	public class MyHtmlTagHandler implements TagHandler, OnClickListener {

	    public void handleTag(boolean opening, String tag, Editable output,
	             XMLReader xmlReader) {
	         if(tag.equalsIgnoreCase("strike") || tag.equals("s")) {
	             processStrike(opening, output);
	         }
	     }

	     private void processStrike(boolean opening, Editable output) {
	         int len = output.length();
	         if(opening) {
	             output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
	         } else {
	             Object obj = getLast(output, StrikethroughSpan.class);
	             int where = output.getSpanStart(obj);

	             output.removeSpan(obj);

	             if (where != len) {
		        	 String downloadlink = "http://www.baidu.com";
		        	 output.setSpan(new URLSpan(downloadlink), where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	                 //output.setSpan(new StrikethroughSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	             }
	         }
	     }

	     private Object getLast(Editable text, Class kind) {
	         Object[] objs = text.getSpans(0, text.length(), kind);

	         if (objs.length == 0) {
	             return null;
	         } else {
	             for(int i = objs.length;i>0;i--) {
	                 if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
	                     return objs[i-1];
	                 }
	             }
	             return null;
	         }
	     }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new Builder(v.getContext());
			builder.setTitle("msg x clicked");
			AlertDialog dialog = builder.create();            
			dialog.show();
		}

	}
	
	MyHtmlTagHandler myTagHand = new MyHtmlTagHandler() {
 
	};
	
	private JSONArray m_model = null;
	ListView listView;
	String[] titles={"标题1","标题2","标题3","标题4"};
	String[] texts={"文本内容A","文本内容B","文本内容C","文本内容D"};
	int[] resIds={R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon};
	private ShowthreadAdapter m_adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setTitle("BaseAdapter for ListView");
		listView=(ListView)this.findViewById(R.id.MyListView);
		listView.setOnItemClickListener(this);		
		
		JSONObject item = new JSONObject();		
		
		item.put("postid", 1);
		item.put("thumbnail", 0);
		item.put("username", Html.fromHtml("forest"));
		item.put("userid", 10003);
		//item.put("avatar", 2);
		//item.put("avatardateline", "");
		
		Time time = new Time();
		time.setToNow();
		
		int hour12 = time.hour;
		String hour = null;
		if (time.hour > 12) {
			hour12 = time.hour - 12;
		} 
		hour = "" + ((hour12 > 10)?hour12:("0"+hour12));
		item.put("posttime", hour + ":" + time.minute + " " + ((time.hour > 12)?"PM":"AM"));
		
		item.put("postdate", "今天");
		item.put("message", ".----------------http://www.baidu.com");
		//m_model.add(item);
		
		String strjson = "[{\"avatar\":\"1\",\"avatardateline\":\"1360059934\",\"message\":\"这么多年来，<strike>BLUELINE Under</strike><a href='http://www.baidu.com'>www.baidu.com</a>在大学学到了许多知识，非常感谢大学！<br /><br />这里显示的是全部信息。<br /><br />&nbsp;<br />&nbsp</Array>......\",\"otherattachments\":[{\"attachmentid\":\"76812\",\"filename\":\"xxxx.pdf\",\"filesize\":\"286243\"}],\"postdate\":\"2013-02-09\",\"postid\":\"1141818\",\"posttime\":\"18:21:51\",\"thumbnail\":1,\"thumbnailSpanned\":\"这么多年来，<strike>BLUELINE Under</strike><a href='http://www.baidu.com'>www.baidu.com</a>在大学学到了许多知识，非常感谢大学！\n\n这里显示的是缩略信息。\n?\n?......\",\"title\":\"【原创】打开ssh防火墙策略\",\"userid\":10000000,\"username\":\"test0001\"}]";
		m_model = JSONArray.parseArray(strjson);
				
		m_adapter = new ShowthreadAdapter();
		listView.setAdapter(m_adapter);
		//listView.setAdapter(new ListViewAdapter(titles,texts,resIds));
	
		
		
//		// 绑定XML中的ListView，作为Item的容器
//		ListView list = (ListView) findViewById(R.id.MyListView);
//
//		// 生成动态数组，并且转载数据
//		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
//		for (int i = 0; i < 30; i++) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("ItemTitle", "This is Title.....");
//			map.put("ItemText", "This is text.....");
//			mylist.add(map);
//		}
//		// 生成适配器，数组===》ListItem
//		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
//				mylist,// 数据来源
//				R.layout.my_listitem,// ListItem的XML实现
//
//				// 动态数组与ListItem对应的子项
//				new String[] { "ItemTitle", "ItemText" },
//
//				// ListItem的XML文件里面的两个TextView ID
//				new int[] { R.id.ItemTitle, R.id.ItemText });
//		// 添加并且显示
//		list.setAdapter(mSchedule);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		//对header进行点击也会触发该事件
//		if (position < 2)
//		{
//			AlertDialog.Builder builder = new Builder(v.getContext());
//			builder.setTitle("msg 2 clicked");
//			AlertDialog dialog = builder.create();            
//			dialog.show();
//			
//			return;
//		}
		final JSONObject item = m_model.getJSONObject(position);	//listView 中有两个header，所以此处减去2
		if (item.getInteger("thumbnail") != 1) 
			return;
		
		final ThreadItemFooter itemFooter = (ThreadItemFooter)v.findViewById(R.id.showthreadLoadTip);
		if (itemFooter.isLoading()) {
			return;
		}
		
		final TextView msg = (TextView)v.findViewById(R.id.showthreadMsg);
		if(true)
		{
//			AlertDialog.Builder builder = new Builder(v.getContext());
//			builder.setTitle("msg x clicked");
//			AlertDialog dialog = builder.create();            
//			dialog.show();
		}
		final int postId = Integer.parseInt(item.get("postid").toString());
//		NetClientCallback ncc = new NetClientCallback() {
//			@Override
//			public void execute(int status,
//					String response, List<Cookie> cookies) {
//				if (status == HttpClientUtil.NET_SUCCESS) {
//					final Spanned spanned = Html.fromHtml(response, m_imgGetter, null);
//					item.put("isExpanded", 1);
//					item.put("expandSpanned", spanned);
//					m_handler.post(new Runnable(){
//
//						@Override
//						public void run() {
//							msg.setText(spanned);
//							itemFooter.setLoadFinish();
//							itemFooter.setExpanded();
//						}
//						
//					});
//					return;
//				}
//				m_handler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						itemFooter.setLoadFinish();
//					}
//					
//				});
//				m_handler.sendEmptyMessage(status);
//			}
//		};
		
		//item.put("isExpanded", 1);
//		msg.setText("Load Finished!");
//		itemFooter.setLoadFinish();
		itemFooter.setExpanded();		
		
		//msg.setFocusable(false);
		
		if (item.containsKey("isExpanded") && item.getInteger("isExpanded") == 1) {
			item.put("isExpanded", 0);
			msg.setText((Spanned)item.get("thumbnailSpanned"));
			itemFooter.setCollapsed();
			listView.setSelection(position);
		}else {
			if (item.containsKey("expandSpanned")) {
				item.put("isExpanded", 1);
				msg.setText((Spanned)item.get("expandSpanned"));
				itemFooter.setExpanded();
			}else {
				itemFooter.setLoadFinish();
				final Spanned spanned = Html.fromHtml(item.get("message").toString(), m_imgGetter, myTagHand);
				
				item.put("isExpanded", 1);
				item.put("expandSpanned", spanned);
				//itemFooter.setLoading();
				//Api.getInstance().getForumFullThread(postId, ncc);
			}
		}		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class ListViewAdapter extends BaseAdapter {
		View[] itemViews;

		public ListViewAdapter(String[] itemTitles, String[] itemTexts,
				int[] itemImageRes) {
			itemViews = new View[itemTitles.length];

			for (int i = 0; i < itemViews.length; i++) {
				itemViews[i] = makeItemView(itemTitles[i], itemTexts[i],
						itemImageRes[i]);
			}
		}

		public int getCount() {
			return itemViews.length;
		}

		public View getItem(int position) {
			return itemViews[position];
		}

		public long getItemId(int position) {
			return position;
		}

		private View makeItemView(String strTitle, String strText, int resId) {
			LayoutInflater inflater = (LayoutInflater) MainActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 使用View的对象itemView与R.layout.item关联
			View itemView = inflater.inflate(R.layout.item, null);

			// 通过findViewById()方法实例R.layout.item内各组件
			TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
			title.setText(strTitle);
			TextView text = (TextView) itemView.findViewById(R.id.itemText);
			text.setText(strText);
			ImageView image = (ImageView) itemView.findViewById(R.id.itemImage);
			image.setImageResource(resId);
			
			return itemView;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				return itemViews[position];
			return convertView;
		}
	}
	
	private class ShowthreadAdapter extends BaseAdapter {		
		View[] itemViews;
		public ShowthreadAdapter() {
			super();
			
//			itemViews = new View[1];
//
//			for (int i = 0; i < itemViews.length; i++) {
//				itemViews[i] = makeItemView(titles[i], texts[i],
//						resIds[i]);
//			}
		}

		@Override
		public int getCount() {
			return (m_model == null)?0:m_model.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
		
//		public int getCount() {
//			return itemViews.length;
//		}

//		public View getItem(int position) {
//			return itemViews[position];
//		}

//		public long getItemId(int position) {
//			return position;
//		}

		private View makeItemView(String strTitle, String strText, int resId) {
			LayoutInflater inflater = (LayoutInflater) MainActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 使用View的对象itemView与R.layout.item关联
			View itemView = inflater.inflate(R.layout.item, null);

			// 通过findViewById()方法实例R.layout.item内各组件
			TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
			title.setText(strTitle);
			TextView text = (TextView) itemView.findViewById(R.id.itemText);
			text.setText(strText);
			ImageView image = (ImageView) itemView.findViewById(R.id.itemImage);
			image.setImageResource(resId);
			
			return itemView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.show_thread_item, null);
			}
						
			TextView username = (TextView)convertView.findViewById(R.id.showthreadUsername);
			TextView floorNum = (TextView)convertView.findViewById(R.id.showThreadFloorNum);
			floorNum.setText((position + 1) + "#");
			TextView posttime = (TextView)convertView.findViewById(R.id.showthreadPosttime);
			final TextView msg = (TextView)convertView.findViewById(R.id.showthreadMsg);
			//ImageViewWithCache img = (ImageViewWithCache)convertView.findViewById(R.id.showthreadHeadImg);
			ThreadItemFooter itemFooter = (ThreadItemFooter)convertView.findViewById(R.id.showthreadLoadTip);

			ImageView img = (ImageView) convertView.findViewById(R.id.itemImage);			
			
			final JSONObject item = m_model.getJSONObject(0);
			username.setText(Html.fromHtml(item.get("username").toString()));
			posttime.setText(item.get("postdate")+" "+item.get("posttime"));
			if (item.getInteger("avatar") == 1) {
				//img.setImageResource(R.drawable.icon);
				//img.setImageUrl(new URL(Api.getInstance().getUserHeadImageUrl(item.getInteger("userid"))));
			}else {
				//img.setImageResource(R.drawable.icon);
			}
//			//格式化缩略帖子缩略信息的Runnable
//			Runnable runFormatMessage = new Runnable() {
//
//				@Override
//				public void run() {
//					final Spanned spanned = Html.fromHtml(item.get("message").toString(), m_imgGetter, null);
//					item.put("thumbnailSpanned", spanned);					
//					m_handler.post(new Runnable(){
//
//						@Override
//						public void run() {
//							msg.setText(spanned);							
//						}
//						
//					});
//				}
//				
//			};
			
			String html = "<h1>this is h1</h1>" 
			        + "<p>This text is normal</p>" 
			        + "<img src='https://www.google.com.hk/intl/zh-CN/images/logo_cn.png' />"; 
			//final Spanned spanned = Html.fromHtml(html);	
			final Spanned spanned = Html.fromHtml(item.get("thumbnailSpanned").toString(), m_imgGetter, myTagHand);
			item.put("thumbnailSpanned", spanned);
			//item.put("isExpanded", 1);
			
			final CharSequence text  =  msg.getText();
			final int  end  =  text.length();
			//final Spannable spans = (Spannable) text;
			
			class  MyURLSpan extends ClickableSpan{   
			    private  String mUrl;   

			   MyURLSpan(String url) {   
			       mUrl  = url;
			   }

			   @Override
			    public   void  onClick(View widget) {
////				AlertDialog.Builder builder = new Builder(MainActivity.this);
////				builder.setTitle("Location clicked");
////				AlertDialog dialog = builder.create();
////				dialog.show();
			        //  TODO Auto-generated method stub 
			       //Toast.makeText(ctx,  " hello! " ,Toast.LENGTH_LONG).show();
			   }
			}
			 
			if (text instanceof Spannable){ 
				
				Spannable spans  =  (Spannable)msg.getText(); 
				URLSpan[] urls = spans.getSpans( 0 , end, URLSpan. class );

	            SpannableStringBuilder style = new  SpannableStringBuilder(text);   

	            style.clearSpans(); // should clear old spans    

	             for (URLSpan url : urls){   

	                MyURLSpan myURLSpan  =   new  MyURLSpan(url.getURL());   

	                style.setSpan(myURLSpan,spans.getSpanStart(url),spans.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
	             }
	             msg.setText(style);				
			}
			
//			ClickableSpan clickSpan = new ClickableSpan() {
//
//				@Override
//				public void onClick(View widget) {
//					// put whatever you like here, below is an example
////					AlertDialog.Builder builder = new Builder(MainActivity.this);
////					builder.setTitle("Location clicked");
////					AlertDialog dialog = builder.create();
////					dialog.show();
//					
//				}
//			};
			//spans.setSpan(clickSpan, 0, spans.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					AlertDialog.Builder builder = new Builder(v.getContext());
//					builder.setTitle("Location clicked");
//					AlertDialog dialog = builder.create();            
//					dialog.show();
					final View cView = getLayoutInflater().inflate(R.layout.show_thread_item, null);
					final ThreadItemFooter itemFooter = (ThreadItemFooter)cView.findViewById(R.id.showthreadLoadTip);
					if (itemFooter.isLoading()) {
						return;
					}
					
					if (item.containsKey("isExpanded") && item.getInteger("isExpanded") == 1) {
						item.put("isExpanded", 0);
						msg.setText((Spanned)item.get("thumbnailSpanned"));
						itemFooter.setCollapsed();
						listView.setSelection(position);
						Log.d("KXDEBUG", "setCollapsed 553");
					}else {
						if (item.containsKey("expandSpanned")) {
							item.put("isExpanded", 1);
							msg.setText((Spanned)item.get("expandSpanned"));
							itemFooter.setExpanded();
							Log.d("KXDEBUG", "setExpanded 559");
						}else {
							itemFooter.setLoadFinish();
							final Spanned spanned = Html.fromHtml(item.get("message").toString(), m_imgGetter, myTagHand);
							
							item.put("isExpanded", 1);
							item.put("expandSpanned", spanned);
							Log.d("KXDEBUG", "setExpanded 566");
							//itemFooter.setLoading();
							//Api.getInstance().getForumFullThread(postId, ncc);
						}
					}	
				}
			});
			
//			msg.setClickable(false);
      	 
			//msg.setAutoLinkMask(Linkify.WEB_URLS);
		
			msg.setMovementMethod(LinkMovementMethod.getInstance());			
			
			//长文章的点击扩展
			itemFooter.setVisibility((item.getInteger("thumbnail") == 1)?View.VISIBLE:View.GONE);
			if (item.containsKey("isExpanded") && item.getInteger("isExpanded") == 1) {
				//onItemClick中扩展文章，缓存格式化后的对象
				msg.setText((Spanned)item.get("expandSpanned"));
				itemFooter.setExpanded();				
				Log.d("KXDEBUG", "setExpanded 586");
				
			}else {
				if (item.containsKey("thumbnailSpanned")) {
					msg.setText((Spanned)item.get("thumbnailSpanned"));
				} else {
					//new Thread(runFormatMessage).start();
					final Spanned spannedmsg = Html.fromHtml(item.get("message").toString(), m_imgGetter, myTagHand);										
					msg.setText(spannedmsg);
				}
				itemFooter.setCollapsed();
				Log.d("KXDEBUG", "setExpanded 597");
			}
			
			//msg.setFocusable(false);
			
			//图片中带附件处理
			View attachmentView = convertView.findViewById(R.id.showthreadAttachment);
			if (!item.containsKey("thumbnailattachments") && !item.containsKey("otherattachments")) {
				attachmentView.setVisibility(View.GONE);
			}
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 5, 0, 0);
			LinearLayout attachmentList = (LinearLayout)convertView.findViewById(R.id.showThreadImgAttachmentList);
			if (item.containsKey("thumbnailattachments")) {
				JSONArray arr = item.getJSONArray("thumbnailattachments");
				attachmentView.setVisibility(View.VISIBLE);
				attachmentList.removeAllViews();
				for (int i = 0; i < arr.size(); i++) {
					ImageView imgWithCache = (ImageView) convertView.findViewById(R.id.itemImage);
					//ImageViewWithCache imgWithCache = new ImageViewWithCache(ShowThreadPage.this);
					imgWithCache.setLayoutParams(lp);
					int attachmentId = arr.getJSONObject(i).getInteger("attachmentid");
					//URL url = new URL(Api.getInstance().getAttachmentImgUrl(attachmentId));
					//imgWithCache.setImageUrl(url, Api.getInstance().getCookieStorage().getCookies());
					imgWithCache.setImageResource(R.drawable.icon);
					attachmentList.addView(imgWithCache);
				}
			}
			
			//其他类型附件处理
			attachmentList = (LinearLayout)convertView.findViewById(R.id.showThreadOtherAttachmentList);
			if (item.containsKey("otherattachments")) {
				JSONArray arr = item.getJSONArray("otherattachments");
				attachmentView.setVisibility(View.VISIBLE);
				attachmentList.removeAllViews();
				
				for (int i = 0; i < arr.size(); i++) {
					TextView filename = new TextView(MainActivity.this);
					filename.setLayoutParams(lp);
					filename.setText(arr.getJSONObject(i).getString("filename"));
					
//					int attachmentId = arr.getJSONObject(i).getInteger("attachmentid");
//					
//					String downloadlink = "http://www.baidu.com";
//
//					//创建一个 SpannableString对象 					
//					SpannableString sp = new SpannableString(arr.getJSONObject(i).getString("filename")); 
//					
//					//设置超链接 					
//					sp.setSpan(new URLSpan(downloadlink), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 					
//					filename.setText(sp); 
	
					//设置TextView可点击 
					//filename.setMovementMethod(LinkMovementMethod.getInstance()); 
					attachmentList.addView(filename);
				}
			}
			
			return convertView;
		}
	}
}
