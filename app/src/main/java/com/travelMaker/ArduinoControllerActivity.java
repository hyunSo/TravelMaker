package com.travelMaker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ArduinoControllerActivity extends TravelActivity implements View.OnClickListener {

	private Context mContext = null;
	private ActivityHandler mHandler = null;

	private SerialListener mListener = null;
	private SerialConnector mSerialConn = null;

	private TextView mTextLog = null;
	private TextView mTextInfo = null;
	private Button mProductList;

	// About Camera
	MyCameraSurface mSurface;
	String mRootPath;

	String Toast_msg = null;

	int album_id;
	String album_name;
	String path, name, weight = "1.2";
	static boolean camera_flag, weight_flag;

	int PRODUCT_ID;
	ProductDatabaseHandler dbHandler = new ProductDatabaseHandler(this);


	private int tmp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// System
		mContext = getApplicationContext();

		// Layouts
		setContentView(R.layout.activity_arduino_controller);

		mTextInfo = (TextView) findViewById(R.id.text_info);
		mTextInfo.setMovementMethod(new ScrollingMovementMethod());
/*
		mProductList = (Button) findViewById(R.id.product_btn);
		mProductList.setOnClickListener(this);
*/
		album_id = Integer.parseInt(getIntent().getStringExtra("ALBUM_ID"));
		album_name = getIntent().getStringExtra("ALBUM_NAME");

		// Initialize
		mListener = new SerialListener();
		mHandler = new ActivityHandler();

		// Initialize Serial connector and starts Serial monitoring thread.
		mSerialConn = new SerialConnector(mContext, mListener, mHandler);
		mSerialConn.initialize();

		// Initialize Camera View
		mSurface = (MyCameraSurface)findViewById(R.id.previewFrame);
		mSurface.setOnClickListener(new FrameLayout.OnClickListener() {
			public void onClick(View v) {
				mSurface.mCamera.autoFocus(mAutoFocus);
			}
		});

		camera_flag = false; weight_flag = false;
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}



	@Override
	public void onDestroy() {
		super.onDestroy();

		mSerialConn.finalize();
	}



	@Override
	public void onClick(View v) {
		switch(v.getId()) {
/*		case R.id.product_btn:
			Intent i = new Intent(ArduinoControllerActivity.this, ProductList.class);
			i.putExtra("ALBUM_ID", album_id);
			i.putExtra("ALBUM_NAME", album_name);
			startActivity(i);
*/		default:
			break;
		}
	}



	public class SerialListener {
		public void onReceive(int msg, int arg0, int arg1, String arg2, Object arg3) {
			switch(msg) {
			case Constants.MSG_DEVICD_INFO:
				//mTextLog.append(arg2+"1");
				break;
			case Constants.MSG_DEVICE_COUNT:
				//mTextLog.append(Integer.toString(arg0) + " device(s) found \n");
				break;
			case Constants.MSG_READ_DATA_COUNT:
				//mTextLog.append(Integer.toString(arg0) + " buffer received \n");
				break;
			case Constants.MSG_READ_DATA:
				if(arg3 != null) {
					mTextInfo.setText((String) arg3);
					//mTextLog.append((String)arg3+"read");
					//mTextLog.append("\n");
				}
				break;
			case Constants.MSG_SERIAL_ERROR:
				//mTextLog.append(arg2+"2");
				break;
			case Constants.MSG_FATAL_ERROR_FINISH_APP:
				finish();
				break;
			}
		}
	}



	public class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			Show_Toast((String)msg.obj);
			switch(msg.what) {
			case Constants.MSG_DEVICD_INFO:
				//mTextLog.append((String)msg.obj+"a1");
				break;
			case Constants.MSG_DEVICE_COUNT:
				//mTextLog.append(Integer.toString(msg.arg1) + " device(s) found \n");
				break;
			case Constants.MSG_READ_DATA_COUNT:
				String con = (String)msg.obj;
				//mTextLog.append(con + "\n");
				if(con.charAt(0)!='a') { //사진촬영
					readyCamera();
				}
				else { //무게와따
					con = con.substring(1);
					mTextInfo.setText(con);
					//mTextLog.append("무게 : "+con);
					//mTextLog.append("\n");
					weight_flag = true;

					weight = con;
					//Show_Toast("무게왔다");
					checkAndSave();
				}
				break;
			case Constants.MSG_READ_DATA:
				if(msg.obj != null) {
					mTextInfo.setText((String)msg.obj);
					//mTextLog.append((String) msg.obj+"a3");
					//mTextLog.append("\n");
				}
				break;
			case Constants.MSG_SERIAL_ERROR:
				//mTextLog.append((String)msg.obj+"a4");
				break;
			}
		}
	}



	public void saveProduct() {
		if (weight != null && weight.length() != 0
				&& path != null && path.length() != 0
				&& name != null && name.length() != 0
				) {

			dbHandler.Add_Product(new Product(album_id, name,
					path, weight));
			Toast_msg = "물품이 저장되었습니다";
			Show_Toast(Toast_msg);
		}
	}



	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}



	public void Show_Toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}



	public void checkAndSave() {
		if( camera_flag && weight_flag ) {
			saveProduct();
			camera_flag = false; weight_flag = false;
		}
	}



	public void readyCamera() {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			//Do Something
			@Override
			public void run() {
				mSurface.mCamera.autoFocus(mAutoFocus);

			}
		}, 1000); // 1000ms
		//저장할 공간 /mnt/sdcard/CameraTest 이렇게 폴더 안에 파일이 생성된다
		mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
				"/" + album_name;
		File fRoot = new File(mRootPath);
		if (fRoot.exists() == false) {
			if (fRoot.mkdir() == false) {
				Toast.makeText(this, "사진을 저장할 폴더가 없습니다.", 1).show();
				//finish();
				return;
			}
		}
	}



	// 포커싱 성공하면 촬영 허가
	Camera.AutoFocusCallback mAutoFocus = new Camera.AutoFocusCallback() {

		public void onAutoFocus(boolean success, Camera camera) {
			mSurface.mCamera.takePicture(null, null, mPicture);
		}

	};



	// 사진 저장.
	Camera.PictureCallback mPicture = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
		//날짜로 파일 이름 만들기
		Calendar calendar = Calendar.getInstance();
		name = String.format("SH%02d%02d%02d-%02d%02d%02d.jpg",
				calendar.get(Calendar.YEAR) % 100, calendar.get(Calendar.MONTH)+1,
				calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		path = mRootPath + "/" + name;

		File file = new File(path);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "file error", 1).show();
			return;
		}
		//파일을 갤러리에 저장
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.parse("file://" + path);
		intent.setData(uri);
		sendBroadcast(intent);
		//Toast.makeText(getApplicationContext(), "사진이 저장 되었습니다", 0).show();
		camera.startPreview();
		// Set Camera Flag
		camera_flag = true;

		checkAndSave();
		return;
		}

	};

}



class MyCameraSurface extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	Camera mCamera;

	public MyCameraSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}



// 표면 생성시 카메라 오픈하고 미리보기 설정
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		mCamera.setDisplayOrientation(90);

		try {
			mCamera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
		}
	}



	// 표면 파괴시 카메라도 파괴한다.
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}



// 표면의 크기가 결정될 때 최적의 미리보기 크기를 구해 설정한다.
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		Camera.Parameters params = mCamera.getParameters();
		params.setPreviewSize(width, height);
		List<Camera.Size> cSize = mCamera.getParameters().getSupportedPreviewSizes();
		Camera.Size tmpSize = cSize.get(1);

		params.setPreviewSize(tmpSize.width, tmpSize.height);
		params.setPictureSize(tmpSize.width, tmpSize.height);
		params.setRotation(270);
		// mCamera.setParameters(params);
		mCamera.startPreview();
	}

}