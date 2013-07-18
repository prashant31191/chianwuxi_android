package com.wuxi.app.fragment.homepage.goverpublicmsg;


import java.util.List;

import com.wuxi.app.R;
import com.wuxi.app.engine.ChannelService;
import com.wuxi.app.fragment.commonfragment.MenuItemMainFragment;
import com.wuxi.app.fragment.homepage.informationcenter.ContentListFragment;
import com.wuxi.app.fragment.homepage.informationcenter.InfoNavigatorWithContentFragment;
import com.wuxi.app.fragment.homepage.informationcenter.WapFragment;
import com.wuxi.app.listeners.MenuItemInitLayoutListener;
import com.wuxi.app.listeners.PublicServiceInitLayoutImpl;
import com.wuxi.domain.Channel;
import com.wuxi.domain.MenuItem;
import com.wuxi.exception.NetException;

/**
 * 政府公开信息Fragment
 * @author 杨宸 智佳
 * */

public class PublicGoverMsgFragment extends MenuItemMainFragment{


	@Override
	public void initializSubFragmentsLayout(List<MenuItem> items) {
		// TODO Auto-generated method stub
		for (final MenuItem menu : items) {
			// wap类型菜单
			if (menu.getType() == MenuItem.WAP_MENU) {
				menu.setContentFragment(WapFragment.class);
			} 
			// 如果菜单上频道菜单
			else if (menu.getType() == MenuItem.CHANNEL_MENU) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						ChannelService channelService = new ChannelService(
								context);
						try {
							List<Channel> channels = channelService
									.getSubChannels(menu.getChannelId());

							if (channels != null) {
								System.out.println("channels:"+channels.get(0).getChannelName());
								menu.setContentFragment(InfoNavigatorWithContentFragment.class);
							} else {
								menu.setContentFragment(ContentListFragment.class);// 内容列表界面
							}
						} catch (NetException e) {
							e.printStackTrace();
						}
					}
				}).start();

			} 
			// 普通菜单
			else if (menu.getType() == MenuItem.CUSTOM_MENU) {
				menu.setContentFragment(InfoNavigatorWithContentFragment.class);
			}
			//定制菜单
			else if(menu.getType() == MenuItem.APP_MENU){
				//目前就工作意见箱一个定制菜单
				if(menu.getName().endsWith("工作意见箱")){
					menu.setContentFragment(WorkSuggestionBoxFragment.class);
				}
			}
		}
	}


	@Override
	protected int getTitlePerScreenItemCount() {
		return 5;
	}

	@Override
	protected MenuItemInitLayoutListener getMenuItemInitLayoutListener() {
		return new PublicServiceInitLayoutImpl();
	}


<<<<<<< HEAD

=======
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_chanel_layout;
	}


	@Override
	protected String getTitleText() {
		return menuItem.getName();
	}

	
	
>>>>>>> 0ff2f65410b4072861a6ebc127badd2cd7d786b2
}
