package com.wuxi.app.fragment.homepage.mygoverinteractpeople;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.R;
import com.wuxi.app.engine.LetterService;
import com.wuxi.app.util.Constants;
import com.wuxi.domain.MyLetter;
import com.wuxi.exception.NODataException;
import com.wuxi.exception.NetException;

public class GIP12345IWantMailLayoutFragment extends BaseFragment implements OnClickListener,OnCheckedChangeListener{
	protected View view;
	protected LayoutInflater mInflater;
	private Context context;
	
	private final static int SEND_SUCCESS=1;
	private final static int SEND_FAILED=0;
	
	
	private MyLetter myLetter;

	RadioGroup mailType_radioGroup;
	public final String doprojectid_mayorBox = "6b8e124e-1e5c-4a11-8dd3-c6623c809eff"; // 市长信箱
	public final String doprojectid_suggestAndComplaint = "bfffa273-086a-47cb-a7a8-7ae8140550db"; // 市长信箱

	RadioGroup isOpen_radioGroup;
	public final int open = 1;
	public final int notopen = 0;

	RadioGroup isReplyMail_radioGroup;
	public final int replyMail = 1;
	public final int notreplyMail = 0;

	RadioGroup isReplyMsg_radioGroup;
	public final int replyMsg = 1;
	public final int notreplyMsg = 0;

	Spinner mailBoxType;

	EditText title_editText;
	EditText content_editText;

	ImageButton upload;
	ImageButton send;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String tip = "";

			if (msg.obj != null) {
				tip = msg.obj.toString();
			}
			switch (msg.what) {
			case SEND_SUCCESS:
				Toast.makeText(context, "提交成功！", 2000).show();
				break;
			case SEND_FAILED:
				Toast.makeText(context, "提交失败！", 2000).show();
				break;
			}
		};
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.gip_iwantmail_appui_layout, null);
		mInflater = inflater;
		context = getActivity();
		
		initView();
		return view;
	}

	public void initView(){
		myLetter = new MyLetter();

		mailType_radioGroup = (RadioGroup) view
				.findViewById(R.id.gip_12345_iwantmail_radiogroup_mailType);
		isOpen_radioGroup = (RadioGroup) view
				.findViewById(R.id.gip_12345_iwantmail_radiogroup_isopen);
		isReplyMail_radioGroup = (RadioGroup) view
				.findViewById(R.id.gip_12345_iwantmail_radiogroup_isNeedMailRaply);
		isReplyMsg_radioGroup = (RadioGroup) view
				.findViewById(R.id.gip_12345_iwantmail_radiogroup_isNeedMsgRaply);

		mailBoxType = (Spinner) view
				.findViewById(R.id.gip_12345_iwantmail_spinner_type);
		ArrayAdapter mailBoxType_Spinner_adapter = ArrayAdapter
				.createFromResource(context, R.array.mailBoxType,
						R.layout.my_spinner_small_item);
		mailBoxType_Spinner_adapter
		.setDropDownViewResource(R.layout.my_spinner_small_dropdown_item);
		mailBoxType.setAdapter(mailBoxType_Spinner_adapter);
		mailBoxType.setVisibility(View.VISIBLE);

		title_editText = (EditText) view
				.findViewById(R.id.gip_12345_iwantmail_editText_title);
		content_editText = (EditText) view
				.findViewById(R.id.gip_12345_iwantmail_editText_content);

		upload = (ImageButton) view
				.findViewById(R.id.gip_12345_iwantmail_imageBtn_upload);
		send = (ImageButton) view
				.findViewById(R.id.gip_12345_iwantmail_imageBtn_send);

		mailType_radioGroup.setOnCheckedChangeListener(this);
		isOpen_radioGroup.setOnCheckedChangeListener(this);
		isReplyMail_radioGroup.setOnCheckedChangeListener(this);
		isReplyMsg_radioGroup.setOnCheckedChangeListener(this);

		upload.setOnClickListener(this);
		send.setOnClickListener(this);

		mailBoxType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int p,
					long arg3) {
				// TODO Auto-generated method stub
				myLetter.setDoprojectid(String.valueOf((p + 1)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.gip_12345_iwantmail_imageBtn_upload:
			Toast.makeText(context, "上传附件（功能暂未实现）", 1000).show();
			break;
		case R.id.gip_12345_iwantmail_imageBtn_send:
			myLetter.setAccess_token(Constants.SharepreferenceKey.TEST_ACCESSTOKEN);
			myLetter.setTitle(title_editText.getText().toString());
			myLetter.setContent(content_editText.getText().toString());
			try {
				submitMyLetter();
			} catch (NetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NODataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}	
	}
	
	public void submitMyLetter() throws NetException, JSONException, NODataException{

		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LetterService service=new LetterService(context);
				try {
					if(service.submitMyLetter(myLetter)){
						handler.sendEmptyMessage(SEND_SUCCESS);					
					}						
					else {
						handler.sendEmptyMessage(SEND_FAILED);	
					}

				} catch (NetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NODataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}).start();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.gip_12345_iwantmail_radiobutton_mayorbox:
			myLetter.setDoprojectid(doprojectid_mayorBox);
			break;
		case R.id.gip_12345_iwantmail_radiobutton_suggestAndComplaint:
			myLetter.setDoprojectid(doprojectid_suggestAndComplaint);
			break;

		case R.id.gip_12345_iwantmail_radiobutton_open:
			myLetter.setOpenState(open);
			break;
		case R.id.gip_12345_iwantmail_radiobutton_notOpen:
			myLetter.setOpenState(notopen);
			break;

		case R.id.gip_12345_iwantmail_radiobutton_notNeedMail:
			myLetter.setSentMailBack(notreplyMail);
			break;
		case R.id.gip_12345_iwantmail_radiobutton_needMail:
			myLetter.setSentMailBack(replyMail);
			break;

		case R.id.gip_12345_iwantmail_radiobutton_needMsg:
			myLetter.setMsgStatus(replyMsg);
			break;
		case R.id.gip_12345_iwantmail_radiobutton_notNeedMsg:
			myLetter.setMsgStatus(notreplyMsg);
			break;
		}		
	}
}