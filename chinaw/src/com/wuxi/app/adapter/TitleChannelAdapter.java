package com.wuxi.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuxi.app.R;
import com.wuxi.domain.Channel;

/**
 * 头部滑动模块数据适配器
 * 
 * @author wanglu
 * 
 */
public class TitleChannelAdapter extends BasicAdapter {

	private List<Channel> items;
	private int screenIndex;

	public TitleChannelAdapter(Context context, int view, int[] viewId,
			String[] dataName, List<Channel> items, int screenIndex) {
		super(context, view, viewId, dataName);
		this.items = items;
		this.screenIndex = screenIndex;

	}

	@Override
	public int getCount() {

		return items.size();
	}

	@Override
	public Object getItem(int position) {

		return items.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	class ViewHolder {

		public TextView title_text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String chanelName = items.get(position).getChannelName();// 频道名称
		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = getInflater().inflate(getView(), null);
			int[] viewId = getViewId();
			viewHolder = new ViewHolder();

			viewHolder.title_text = (TextView) convertView
					.findViewById(viewId[0]);

			if (screenIndex == 0 && position == 0) {

				viewHolder.title_text.setBackground(context.getResources()
						.getDrawable(R.drawable.title_item_select_bg));

				viewHolder.title_text.setTextColor(Color.WHITE);

			}

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title_text.setText(chanelName);
		return convertView;
	}

}