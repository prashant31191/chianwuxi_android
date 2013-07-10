package com.wuxi.app.fragment.homepage.goverpublicmsg;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.R;
import com.wuxi.app.fragment.commonfragment.NavigatorWithContentFragment;
import com.wuxi.app.fragment.commonfragment.SimpleListViewFragment;
import com.wuxi.app.listeners.InitializContentLayoutListner;
import com.wuxi.domain.MenuItem;

/**
 * 政府公开信息 之  市政府信息公开目录
 * @author 杨宸 智佳
 * */

public class MunicipalGovInfoPublicDirecFragment extends BaseFragment implements
InitializContentLayoutListner{
	protected View view;

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
		view = inflater.inflate(R.layout.fragment_publicgovermsg_municipalgovinfopublicdirec_layout, null);
		
		
		this.inflater = inflater;
		context = this.getActivity();
		initUI();
		return view;
	}


	private void initUI() {
		NavigatorWithContentFragment navigatorWithContentFragment=new NavigatorWithContentFragment();
		navigatorWithContentFragment.setParentMenuItem(menuItem);
		navigatorWithContentFragment.setDataType(NavigatorWithContentFragment.DATA_TPYE_MENUITEM);
		
		onTransaction(navigatorWithContentFragment);
	}
	
	private void onTransaction(NavigatorWithContentFragment fragment) {

		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(R.id.goverpublic_titlebelow_content_layout, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}
	

	
	@Override
	public void bindContentLayout(Fragment fragment) {
		// TODO Auto-generated method stub
		
	}
	
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	///初始化子列表的布局格式
	@Override
	public void initializSubFragmentsLayout() {
		
		for(MenuItem menu:titleMenus){
			if(menu.getName().equals("最新信息公开")
					||menu.getName().equals("信息公开指南")
					||menu.getName().equals("信息公开制度"))
			menu.setContentFragment(SimpleListViewFragment.class);
			else if(menu.getName().equals("工作意见箱"))
				menu.setContentFragment(WorkSuggestionBoxFragment.class);
			else
				menu.setContentFragment(NavigatorWithContentFragment.class);
		}
	}

}
