package com.wuxi.app.fragment.homepage.mygoverinteractpeople;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.wuxi.app.R;
import com.wuxi.app.fragment.MainMineFragment;
import com.wuxi.app.fragment.MainSearchFragment;
import com.wuxi.app.fragment.commonfragment.HomeBaseSlideLevelFragment;
import com.wuxi.app.fragment.homepage.logorregister.LoginFragment;
import com.wuxi.app.fragment.homepage.more.SystemSetFragment;
import com.wuxi.app.util.GIPRadioButtonStyleChange;

/**
 * 我的政民互动  主Fragment 
 * @author 杨宸 智佳
 * */

public class GoverInterPeopleMineFragment extends MainMineFragment implements
OnCheckedChangeListener {
	public View view = null;

	protected LayoutInflater mInflater;
	private int GIP_MINE_FRAME_CONTENT=R.id.goverinterpeople_mine_content_fragment;
	private RadioGroup radioGroup;
	private final  int[] radioButtonIds={
			R.id.goverinterpeople_mine_radioButton_12345,
			R.id.goverinterpeople_mine_radioButton_suggestionPlatform,
			R.id.goverinterpeople_mine_radioButton_internetGoverSaloon,
			R.id.goverinterpeople_mine_radioButton_infoPublicPlatform,
			R.id.goverinterpeople_mine_radioButton_publicForum
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.goverinterpeople_mine_layout, null);
		mInflater = inflater;
		context = getActivity();
		init();


		radioGroup = (RadioGroup) view.findViewById(R.id.goverinterpeople_mine_radioGroup);
		radioGroup.setOnCheckedChangeListener(this);


		return view;
	}

	public void init(){
		GoverInterPeopleMineFragment mine12345Fragment=new GIPMine12345Fragment();
		onTransaction(mine12345Fragment);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		GIPRadioButtonStyleChange radioButtonStyleChange=new GIPRadioButtonStyleChange();
		radioButtonStyleChange.refreshRadioButtonStyle(view,radioButtonIds,checkedId);
		switch (checkedId) {

		case R.id.goverinterpeople_mine_radioButton_12345:
			init();
			break;

		case R.id.goverinterpeople_mine_radioButton_suggestionPlatform:	
			GoverInterPeopleMineFragment suggestionPlatformFragment=new GIPMineSuggestionPlatformFragment();
			onTransaction(suggestionPlatformFragment);
			break;

		case R.id.goverinterpeople_mine_radioButton_internetGoverSaloon:
			GoverInterPeopleMineFragment internetGoverSaloonFragment=new GIPMineInternetGoverSaloonFragment();
			onTransaction(internetGoverSaloonFragment);
			break;

		case R.id.goverinterpeople_mine_radioButton_infoPublicPlatform:
			GoverInterPeopleMineFragment infoPublicPlatformFragment=new GIPMineInfoPublicPlatformFragment();
			onTransaction(infoPublicPlatformFragment);

			break;

		case R.id.goverinterpeople_mine_radioButton_publicForum:
			GoverInterPeopleMineFragment publicForumFragment=new GIPMinePublicForumFragment();
			onTransaction(publicForumFragment);
			break;
		}
	}

	private void onTransaction(GoverInterPeopleMineFragment fragment) {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(GIP_MINE_FRAME_CONTENT, fragment);
		ft.commit();

	}
}
