package com.wuxi.app.fragment.homepage.goverpublicmsg;


import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.wuxi.app.R;
import com.wuxi.app.fragment.BaseSlideFragment;
import com.wuxi.app.fragment.commonfragment.NavigatorWithContentFragment;
import com.wuxi.app.fragment.commonfragment.SimpleListViewFragment;
import com.wuxi.app.listeners.GoverMsgInitLayoutImpl;
import com.wuxi.app.listeners.InitializContentLayoutListner;
import com.wuxi.app.util.CacheUtil;
import com.wuxi.app.view.TitleScrollLayout;
import com.wuxi.domain.MenuItem;

/**
 * 政府公开信息Fragment
 * @author 杨宸 智佳
 * */

public class PublicGoverMsgFragment extends BaseSlideFragment implements InitializContentLayoutListner,
OnClickListener{
	private TitleScrollLayout mtitleScrollLayout;
	private static final int MANCOTENT_ID = R.id.model_main;
	private static final int TITLE__LOAD_SUCESS = 66;
	private static final int TITLE_LOAD_ERROR = 88;
	protected static final String TAG = "ChannelFragment";
	private LayoutInflater inflater;
	private ImageButton ib_nextItems;
	private MenuItem menuItem;// 菜单项
	private List<MenuItem> titleMenus;// 头部菜单选项
	private Context context;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_publicgovermsg_layout, null);
		InitBtn();
		this.setFragmentTitle(menuItem.getName());// 设置头部名称
		this.inflater = inflater;
		context = this.getActivity();
		initUI();
		return view;
	}


	private void initUI() {
		mtitleScrollLayout = (TitleScrollLayout) view
				.findViewById(R.id.title_scroll_action);// 头部控件
		mtitleScrollLayout.setInitializContentLayoutListner(this);// 设置绑定内容界面监听器
		mtitleScrollLayout.setMenuItemInitLayoutListener(new GoverMsgInitLayoutImpl());
		ib_nextItems = (ImageButton) view.findViewById(R.id.btn_next_screen);// 头部下一个按钮
		ib_nextItems.setOnClickListener(this);

		loadTitleData();
	}


	private void initData(MenuItem parentMenuItem) {

//		NavigatorChannelFragment navigatorChannelFragment=new NavigatorChannelFragment();
//		navigatorChannelFragment.setParentChannel(parentMenuItem);
//		bindFragment(navigatorChannelFragment);
	}


	@SuppressWarnings("unchecked")
	public void loadTitleData() {

		if (CacheUtil.get(menuItem.getId()) != null) {// 从缓存中查找获取子菜单

			titleMenus = (List<MenuItem>) CacheUtil.get(menuItem
					.getId());
			System.out.println("szie"+titleMenus.size());
			showTitleData();
			return;
		}


		//		new Thread(new Runnable() {
		//
		//			@Override
		//			public void run() {
		//
		//				MenuSevice menuSevice = new MenuSevice(context);
		//				try {
		//					titleMenus= menuSevice.getSubMenuItems(menuItem.getId());
		//
		//					if (null != titleMenus) {
		//						CacheUtil.put(menuItem.getId(), titleMenus);// 缓存起来
		//						handler.sendEmptyMessage(TITLE__LOAD_SUCESS);
		//
		//					} else {
		//						Message message = handler.obtainMessage();
		//						message.obj = "error";
		//						handler.sendEmptyMessage(TITLE_LOAD_ERROR);
		//					}
		//
		//				} catch (NetException e) {
		//
		//					LogUtil.i(TAG, "出错");
		//					e.printStackTrace();
		//					Message message = handler.obtainMessage();
		//					message.obj = e.getMessage();
		//
		//					handler.sendEmptyMessage(TITLE_LOAD_ERROR);
		//
		//				} catch (JSONException e) {
		//					LogUtil.i(TAG, "json error");
		//					e.printStackTrace();
		//					Message message = handler.obtainMessage();
		//					message.obj = e.getMessage();
		//				} catch (NODataException e) {
		//					LogUtil.i(TAG, "no data");
		//					e.printStackTrace();
		//					Message message = handler.obtainMessage();
		//					message.obj = e.getMessage();
		//				}
		//			}
		//		}
		//
		//				).start();
	}

	/**
	 * 显示头部数据 wanglu 泰得利通
	 */
	private void showTitleData() {
		initializSubFragmentsLayout();
		mtitleScrollLayout.setPerscreenCount(5);     //设置滑动头选项每屏为5个
	
		mtitleScrollLayout.initMenuItemScreen(context, inflater, titleMenus);// 初始化头部空间
	
		//		initData(titleMenus.get(0));//默认显示第一个channel的子channel页

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next_screen:// 下一屏
			mtitleScrollLayout.goNextScreen();
			break;

		}
	}


	@Override
	public void bindContentLayout(Fragment fragment) {
		bindFragment(fragment); 
	}

	private void bindFragment(Fragment fragment) {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(MANCOTENT_ID, fragment);// 替换内容界面

		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	///初始化子列表的布局格式
	@Override
	public void initializSubFragmentsLayout() {

		for(MenuItem menu:titleMenus){
			String nameStr=menu.getName();
			if(nameStr.equals("最新信息公开")
					||nameStr.equals("信息公开指南")
					||nameStr.equals("信息公开制度"))
				menu.setContentFragment(SimpleListViewFragment.class);
			else if(nameStr.equals("工作意见箱"))
				menu.setContentFragment(WorkSuggestionBoxFragment.class);
			else if(nameStr.equals("市政府信息公开目录"))
				menu.setContentFragment(MunicipalGovInfoPublicDirecFragment.class);
			else
				menu.setContentFragment(NavigatorWithContentFragment.class);
		}
	}


}