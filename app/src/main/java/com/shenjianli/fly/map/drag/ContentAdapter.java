package com.shenjianli.fly.map.drag;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.shenjianli.fly.R;

import java.util.List;


public class ContentAdapter extends BaseAdapter {

	private List<?> mContent;
	private Context mContext;
	 private LayoutInflater mInflater = null;
	public ContentAdapter(Context context,List<?> content)
	{
		this.mContent = content;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContent.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mContent.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		 ViewHolder holder = null;
         //如果缓存convertView为空，则需要创建View
         if(convertView == null)
         {
             holder = new ViewHolder();
             //根据自定义的Item布局加载布局
             convertView = mInflater.inflate(R.layout.item_content_list, null);
             holder.title = (TextView)convertView.findViewById(R.id.tv_title);
             holder.info = (TextView)convertView.findViewById(R.id.tv_info);
             //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
             convertView.setTag(holder);
         }else
         {
             holder = (ViewHolder)convertView.getTag();
         }
         if(position == 0)
         {
        	 holder.title.setTextColor(Color.RED);
         }
         else {
			holder.title.setTextColor(Color.BLACK);
		}

         holder.title.setText((String)((PoiInfo)mContent.get(position)).name);
         holder.info.setText((String)((PoiInfo)mContent.get(position)).address);
         
         return convertView;
	}

	//ViewHolder静态类
    static class ViewHolder
    {
        public TextView title;
        public TextView info;
    }
}
