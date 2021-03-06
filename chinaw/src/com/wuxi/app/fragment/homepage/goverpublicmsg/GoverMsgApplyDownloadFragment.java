package com.wuxi.app.fragment.homepage.goverpublicmsg;

import java.io.File;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.MainTabActivity;
import com.wuxi.app.R;
import com.wuxi.app.activity.homepage.goverpublicmsg.GPMApplyActivity;
import com.wuxi.app.engine.ApplyDeptService;
import com.wuxi.app.engine.ApplyGoverService;
import com.wuxi.app.engine.GoverSaoonFileService;
import com.wuxi.app.util.Constants;
import com.wuxi.domain.ApplyDept;
import com.wuxi.domain.ApplyGover;
import com.wuxi.exception.NetException;

/**
 * @类名： GoverMsgApplyDownloadFragment
 * @描述： 政府信息公开 依申请公开 各市区依申请公开 和 各部门依申请公开 界面
 * @作者： 罗森
 * @创建时间： 2013 2013-10-10 上午9:07:40
 * @修改时间：
 * @修改描述：
 */
public class GoverMsgApplyDownloadFragment extends BaseFragment {

	private View view;
	private LayoutInflater mInflater;
	private Context context;

	private List<ApplyDept> depts;
	private List<ApplyGover> govers;
	private final static int LOAD_GOVER_SUCCESS = 1;
	private final static int LOAD_DEPT_SUCCESS = 2;
	private final static int LOAD_FAILED = 0;

	private static final int FILE_DOWN_SUCCESS = 4;
	private static final int FILE_DOWN_ERROR = 5;

	private ListView content_listView;
	private ProgressBar progressBar;

	public static final int GOVER_TYPE = 0;
	public static final int DEPT_TYPE = 1;

	private int dataType = GOVER_TYPE; // 默认加载政府公开

	private ProgressDialog pd;

	public void setType(int type) {
		this.dataType = type;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String tip = "";

			if (msg.obj != null) {
				tip = msg.obj.toString();
			}
			switch (msg.what) {
			case LOAD_GOVER_SUCCESS:
				progressBar.setVisibility(ProgressBar.INVISIBLE);
				showGoverList();
				break;
			case LOAD_DEPT_SUCCESS:
				progressBar.setVisibility(ProgressBar.INVISIBLE);
				showDeptList();
				break;
			case FILE_DOWN_SUCCESS:
				Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
				break;
			case FILE_DOWN_ERROR:
				Toast.makeText(context, "下载出错，稍后再试", Toast.LENGTH_SHORT).show();
				break;
			case LOAD_FAILED:
				progressBar.setVisibility(ProgressBar.INVISIBLE);
				Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.govermsg_deptapply_layout, null);
		mInflater = inflater;
		context = getActivity();

		initView();

		return view;
	}

	/**
	 * @方法： initView
	 * @描述： 初始化布局控件
	 */
	private void initView() {
		content_listView = (ListView) view
				.findViewById(R.id.govermsg_deptapply_listview);
		progressBar = (ProgressBar) view
				.findViewById(R.id.govermsg_deptapply_progressbar);

		progressBar.setVisibility(ProgressBar.VISIBLE);
		if (dataType == GOVER_TYPE) {
			loadGoverData();
		} else if (dataType == DEPT_TYPE) {
			loadDeptData();
		}

		pd = new ProgressDialog(context);

		pd.setMessage("正在下载表格....");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

	}

	/**
	 * @方法： loadGoverData
	 * @描述： 加载市区数据
	 */
	private void loadGoverData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				ApplyGoverService applyGoverService = new ApplyGoverService(
						context);
				try {
					govers = applyGoverService
							.getGovers(Constants.Urls.APPLYGOVER_URL);
					if (null != govers) {
						handler.sendEmptyMessage(LOAD_GOVER_SUCCESS);
					} else {
						Message message = handler.obtainMessage();
						message.obj = "error";
						handler.sendEmptyMessage(LOAD_FAILED);
					}

				} catch (NetException e) {
					e.printStackTrace();
					Message message = handler.obtainMessage();
					message.obj = e.getMessage();
					handler.sendEmptyMessage(LOAD_FAILED);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * @方法： loadDeptData
	 * @描述： 加载部门数据
	 */
	private void loadDeptData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				ApplyDeptService applyDeptService = new ApplyDeptService(
						context);
				try {
					depts = applyDeptService
							.getDepts(Constants.Urls.APPLYDEPT_URL);
					if (null != depts) {
						handler.sendEmptyMessage(LOAD_DEPT_SUCCESS);
					} else {
						Message message = handler.obtainMessage();
						message.obj = "error";
						handler.sendEmptyMessage(LOAD_FAILED);
					}

				} catch (NetException e) {
					e.printStackTrace();
					Message message = handler.obtainMessage();
					message.obj = e.getMessage();
					handler.sendEmptyMessage(LOAD_FAILED);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * @方法： showGoverList
	 * @描述： 显示市区列表
	 */
	private void showGoverList() {
		if (govers != null)
			content_listView.setAdapter(new ApplyGoverAdapter());
	}

	/**
	 * @方法： showDeptList
	 * @描述： 显示部门列表
	 */
	private void showDeptList() {
		if (depts != null)
			content_listView.setAdapter(new ApplyDeptAdapter());
	}

	/**
	 * @类名： ApplyGoverAdapter
	 * @描述： 市区列表适配器
	 * @作者： 罗森
	 * @创建时间： 2013 2013-10-10 上午9:14:37
	 * @修改时间：
	 * @修改描述：
	 */
	public class ApplyGoverAdapter extends BaseAdapter implements
			OnClickListener {

		private ApplyGover gover;

		public ApplyGoverAdapter() {

		}

		@Override
		public int getCount() {
			return govers.size();
		}

		@Override
		public Object getItem(int position) {
			return govers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public TextView title_text;
			public ImageButton download_imgbtn;
			public ImageButton guide_imgbtn;
			public ImageButton apply_imgbtn;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.govermsg_deptapply_listview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.title_text = (TextView) convertView
						.findViewById(R.id.govermsg_deptapply_item_title);
				viewHolder.download_imgbtn = (ImageButton) convertView
						.findViewById(R.id.govermsg_deptapply_item_download);
				viewHolder.guide_imgbtn = (ImageButton) convertView
						.findViewById(R.id.govermsg_deptapply_item_guide);
				viewHolder.apply_imgbtn = (ImageButton) convertView
						.findViewById(R.id.govermsg_deptapply_item_apply);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			gover = govers.get(position);
			viewHolder.title_text.setText(gover.getDepName());
			viewHolder.apply_imgbtn.setOnClickListener(ApplyGoverAdapter.this);
			viewHolder.download_imgbtn
					.setOnClickListener(ApplyGoverAdapter.this);

			return convertView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.govermsg_deptapply_item_download:
				downloadTable();
				break;
			case R.id.govermsg_deptapply_item_guide:
				openBrowser(gover.getZhinanUrl());
				break;

			// 申请
			case R.id.govermsg_deptapply_item_apply:

				Intent intent = new Intent();
				intent.putExtra("doprojectid", gover.getDoProjectId());
				intent.setClass(getActivity(), GPMApplyActivity.class);
				MainTabActivity.instance.addView(intent);

				break;
			}
		}
	}

	/**
	 * @方法： openBrowser
	 * @描述： 打开连接
	 * @param url
	 */
	private void openBrowser(String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// 建立Intent对象，传入uri
		getActivity().startActivity(intent);
	}

	/**
	 * @类名： ApplyDeptAdapter
	 * @描述： 部门列表适配器
	 * @作者： 罗森
	 * @创建时间： 2013 2013-10-10 上午9:16:09
	 * @修改时间：
	 * @修改描述：
	 */
	public class ApplyDeptAdapter extends BaseAdapter implements
			OnClickListener {

		ApplyDept applyDept = null;

		public ApplyDeptAdapter() {

		}

		@Override
		public int getCount() {
			return depts.size();
		}

		@Override
		public Object getItem(int position) {
			return depts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public TextView title_text;
			public ImageButton download_imgbtn;
			public ImageButton guide_imgbtn;
			public ImageButton apply_imgbtn;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.govermsg_deptapply_listview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.title_text = (TextView) convertView
						.findViewById(R.id.govermsg_deptapply_item_title);
				viewHolder.download_imgbtn = (ImageButton) convertView
						.findViewById(R.id.govermsg_deptapply_item_download);
				viewHolder.guide_imgbtn = (ImageButton) convertView
						.findViewById(R.id.govermsg_deptapply_item_guide);
				viewHolder.apply_imgbtn = (ImageButton) convertView
						.findViewById(R.id.govermsg_deptapply_item_apply);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			applyDept = depts.get(position);
			viewHolder.title_text.setText(depts.get(position).getDepName());
			viewHolder.apply_imgbtn.setOnClickListener(ApplyDeptAdapter.this);
			viewHolder.download_imgbtn
					.setOnClickListener(ApplyDeptAdapter.this);
			return convertView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.govermsg_deptapply_item_download:
				downloadTable();
				break;
			case R.id.govermsg_deptapply_item_guide:
				openBrowser(applyDept.getZhinanUrl());
				break;

			// 申请
			case R.id.govermsg_deptapply_item_apply:

				if (applyDept != null) {
					Intent intent = new Intent();
					intent.putExtra("doprojectid", applyDept.getDoProjectId());
					intent.setClass(getActivity(), GPMApplyActivity.class);

					MainTabActivity.instance.addView(intent);
				}

				break;
			}
		}
	}

	/**
	 * @方法： downloadTable
	 * @描述： 下载表格
	 */
	private void downloadTable() {

		showDownloadComfirmDialog(Constants.Urls.GOVERMSG_TABLE_DOWNLOAD_URL);
	}

	/**
	 * @方法： showDownloadComfirmDialog
	 * @描述： 显示下载对话框
	 * @param url
	 */
	private void showDownloadComfirmDialog(final String url) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setIcon(R.drawable.logo);
		builder.setTitle("下载提示");
		builder.setMessage("确认要下载该"
				+ Constants.APPFiles.GOVERMSG_APPLYOPEN_TABLENAME
				+ "文件吗?\n 文件存放地址:" + Constants.APPFiles.DOWNLOAF_FILE_PATH
				+ Constants.APPFiles.GOVERMSG_APPLYOPEN_TABLENAME);
		builder.setCancelable(false);
		File file = new File(Constants.APPFiles.DOWNLOAF_FILE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {

					DownLoadThreadTask dowTask = new DownLoadThreadTask(
							url,
							Constants.APPFiles.DOWNLOAF_FILE_PATH
									+ Constants.APPFiles.GOVERMSG_APPLYOPEN_TABLENAME);

					new Thread(dowTask).start();
					pd.show();

				} else {
					Toast.makeText(context, "SDK不存在", Toast.LENGTH_SHORT)
							.show();

				}

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();

	}

	/**
	 * @类名： DownLoadThreadTask
	 * @描述： 开辟下载线程
	 * @作者： 罗森
	 * @创建时间： 2013 2013-10-10 上午9:18:11
	 * @修改时间：
	 * @修改描述：
	 */
	private class DownLoadThreadTask implements Runnable {

		private String url;
		private String filePath;

		public DownLoadThreadTask(String url, String filePath) {

			this.url = url;
			this.filePath = filePath;
		}

		@Override
		public void run() {

			try {

				GoverSaoonFileService goverSaoonFileService = new GoverSaoonFileService(
						context);
				File file = goverSaoonFileService.dowloadGoverMsgTable(url,
						filePath, pd);
				if (file != null) {
					handler.sendEmptyMessage(FILE_DOWN_SUCCESS);
				}
				pd.dismiss();
			} catch (Exception e) {

				e.printStackTrace();

				pd.dismiss();

				handler.sendEmptyMessage(FILE_DOWN_ERROR);
			}

		}

	}

}
