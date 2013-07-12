package com.wuxi.app.fragment.homepage.informationcenter;

import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.w;
import com.wuxi.app.BaseFragment;
import com.wuxi.app.R;
import com.wuxi.app.adapter.ContentNavigatorAdapter;
import com.wuxi.app.engine.ChannelService;
import com.wuxi.app.engine.MenuService;
import com.wuxi.app.util.CacheUtil;
import com.wuxi.domain.Channel;
import com.wuxi.domain.MenuItem;
import com.wuxi.exception.NODataException;
import com.wuxi.exception.NetException;

/**
 * 
 * 
 * @author wanglu 信息中心具有左右导航的视图
 * 
 */

public class InfoNavigatorWithContentFragment extends BaseFragment implements
		OnItemClickListener {
	private static final int DETAIL_ID = R.id.details;// 点击左侧导航时右侧要显示内容区域的ID
	protected static final int LEFT_CHANNEL_DATA__LOAD_SUCCESS = 1;// 左侧频道(菜单)加载
	protected static final int LEFT_MENUITEM_DATA__LOAD_SUCCESS = 2;// 左侧频道(菜单)加载
	protected static final int LEFT_DATA__LOAD_ERROR = 0;// 左侧频道(菜单)加载失败

	protected View view;
	protected ListView mListView;// 左侧ListView
	protected LayoutInflater mInflater;
	private Context context;

	private List<Channel> channels;
	private List<MenuItem> menuItems;

	private MenuItem parentMenuItem; // 父菜单

	private ContentNavigatorAdapter adapter;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String tip = "";

			if (msg.obj != null) {
				tip = msg.obj.toString();
			}

			switch (msg.what) {
			case LEFT_CHANNEL_DATA__LOAD_SUCCESS:
				showLeftChannelData();
				break;
			case LEFT_MENUITEM_DATA__LOAD_SUCCESS:
				showLeftMenuItemData();
				break;

			case LEFT_DATA__LOAD_ERROR:
				Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
				break;

			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.content_navigator_layout, null);
		mListView = (ListView) view.findViewById(R.id.lv_left_navigator);

		mInflater = inflater;
		context = getActivity();
		if (parentMenuItem.getType() == MenuItem.CHANNEL_MENU) {
			loadChannelData();
		} else if (parentMenuItem.getType() == MenuItem.CUSTOM_MENU) {// 普通菜单
			loadMenuItemData();// 加载子菜单

		}

		return view;

	}

	@SuppressWarnings("unchecked")
	private void loadMenuItemData() {

		if (null != CacheUtil.get(parentMenuItem.getId())) {
			menuItems = (List<MenuItem>) CacheUtil.get(parentMenuItem.getId());
			showLeftMenuItemData();
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {

				MenuService menuService = new MenuService(context);
				Message msg = handler.obtainMessage();
				try {
					menuItems = menuService.getSubMenuItems(parentMenuItem
							.getId());
					if (menuItems != null) {
						CacheUtil.put(parentMenuItem.getId(), menuItems);// 放入缓存
						msg.what = LEFT_MENUITEM_DATA__LOAD_SUCCESS;
						handler.sendMessage(msg);

					}

				} catch (NetException e) {
					e.printStackTrace();

					msg.obj = "网络连接错误稍后重试";
					msg.what = LEFT_DATA__LOAD_ERROR;
					handler.sendMessage(msg);
				} catch (NODataException e) {
					e.printStackTrace();
					msg.obj = e.getMessage();
					msg.what = LEFT_DATA__LOAD_ERROR;
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
					msg.obj = e.getMessage();
					msg.what = LEFT_DATA__LOAD_ERROR;
					handler.sendMessage(msg);
				}
			}
		}

		).start();

	}

	private void showLeftMenuItemData() {

		adapter = new ContentNavigatorAdapter(mInflater, null, menuItems);
		adapter.setSelectedPosition(0);
		mListView.setAdapter(adapter);// 设置适配器
		mListView.setOnItemClickListener(this);

		if (menuItems.size() > 0) {
			showMenItemContentFragment(menuItems.get(0));

		}
	}

	@SuppressWarnings("unchecked")
	private void loadChannelData() {

		if (null != CacheUtil.get(parentMenuItem.getChannelId())) {// 从缓存中查找
			channels = (List<Channel>) CacheUtil.get(parentMenuItem
					.getChannelId());
			showLeftChannelData();
			return;

		} else {// 从网络加载

			new Thread(new Runnable() {

				@Override
				public void run() {

					ChannelService channelService = new ChannelService(context);

					try {
						channels = channelService.getSubChannels(parentMenuItem
								.getChannelId());
						if (channels != null) {
							handler.sendEmptyMessage(LEFT_CHANNEL_DATA__LOAD_SUCCESS);
							CacheUtil.put(parentMenuItem.getChannelId(),
									channels);// 放入缓存
						}

					} catch (NetException e) {
						e.printStackTrace();
						Message msg = handler.obtainMessage();
						msg.obj = "网络连接错误稍后重试";
						handler.sendMessage(msg);
					}
				}
			}

			).start();

		}

	}

	/**
	 * 
	 * wanglu 泰得利通 显示数据
	 */
	private void showLeftChannelData() {
		adapter = new ContentNavigatorAdapter(mInflater, channels, null);
		adapter.setSelectedPosition(0);
		mListView.setAdapter(adapter);// 设置适配器
		mListView.setOnItemClickListener(this);

		if (channels.size() > 0) {
			showChannelContentFragment(channels.get(0));// 显示第一个Channel数据
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		Object object = parent.getItemAtPosition(position);

		if (object instanceof Channel) {// 如果是频道
			adapter.setSelectedPosition(position); // 刷新左侧导航listView背景
			adapter.notifyDataSetInvalidated();
			showChannelContentFragment((Channel) object);
		} else if (object instanceof MenuItem) {
			adapter.setSelectedPosition(position); // 刷新左侧导航listView背景
			adapter.notifyDataSetInvalidated();
			showMenItemContentFragment((MenuItem) object);

		}

	}

	/**
	 * 
	 * wanglu 泰得利通 显示子菜单的内容fragment
	 * 
	 * @param menuItem
	 */
	private void showMenItemContentFragment(MenuItem menuItem) {

		if (menuItem.getType() == MenuItem.WAP_MENU) {
			WapFragment wapFragment = new WapFragment();
			wapFragment.setParentItem(menuItem);
			showContentFragment(wapFragment);
		}
	}

	/**
	 * 
	 * wanglu 泰得利通 显示频道内容信息
	 */
	private void showChannelContentFragment(Channel channel) {

		// showContentFragment();

	}

	/**
	 * 显示替换主要内容区域
	 * 
	 * @param fragment
	 */
	protected void showContentFragment(Fragment fragment) {
		if (fragment != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(DETAIL_ID, fragment);// 替换视图

			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}

	}

	public void setParentMenuItem(MenuItem parentMenuItem) {
		this.parentMenuItem = parentMenuItem;
	}

}