package com.travelMaker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
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

	private MediaPlayer player = null;

	private SoundPool sound_pool;
	private int sound_beep;

	private View V;

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
	AlbumDatabaseHandler AlbumdbHandler = new AlbumDatabaseHandler(this);
	Album c;
	Button temp;

	private int tmp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// System
		mContext = getApplicationContext();

		// Layouts
		setContentView(R.layout.activity_arduino_controller);
		V = getWindow().getDecorView().getRootView();
/*
		mTextInfo = (TextView) findViewById(R.id.text_info);
		mTextInfo.setMovementMethod(new ScrollingMovementMethod());
*/
		mProductList = (Button) findViewById(R.id.product_btn);
		mProductList.setOnClickListener(this);

		album_id = DataCenter.getAlbumId();
		album_name = DataCenter.getAlbumName();
		c = AlbumdbHandler.Get_Album(album_id);

		temp = (Button) findViewById(R.id.button);
		temp.setOnClickListener(this);

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

		// init signal sound
		initSound();
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
			case R.id.button:
				weight_flag = true;
				camera_flag = true;
				weight = new String("3.33");
				name = new String("tempName");
				path = new String("tempPath");
				checkAndSave();
				break;
			case R.id.product_btn:
				Intent i = new Intent(ArduinoControllerActivity.this, ProductList.class);
				DataCenter.setAlbumId(album_id);
				DataCenter.setAlbumName(album_name);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
				break;
			default:
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

//			Show_Toast((String)msg.obj);
			switch(msg.what) {
			case Constants.MSG_DEVICD_INFO:
				//mTextLog.append((String)msg.obj+"a1");
				break;
			case Constants.MSG_DEVICE_COUNT:
				//mTextLog.append(Integer.toString(msg.arg1) + " device(s) found \n");
				break;
			case Constants.MSG_READ_DATA_COUNT:
				String con = (String)msg.obj;
				//Show_Toast((String)msg.obj);
				//mTextLog.append(con + "\n");
				if(con.charAt(0)=='1') { //사진촬영
					readyCamera();
				}
				else if(con.charAt(0)=='a'){ //무게와따
					con = con.substring(1);

					weight = con;
//					Show_Toast(weight);
					weight_flag = true;
					checkAndSave();
				}
				break;
			case Constants.MSG_READ_DATA:
				if(msg.obj != null) {
					mTextInfo.setText((String)msg.obj);
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
					path, weight, 0));
			AlbumdbHandler.Update_Album_Weight(c, weight);
			dbHandler.close();
			//Toast_msg = "물품이 저장되었습니다";
			//Show_Toast(Toast_msg);
		}
	}



	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onStop () {
		super.onStop();
		if (player != null && player.isPlaying()) {
			player.stop();
			player.release();
			player = null;
		}
	}



	public void Show_Toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	private void initSound()
	{
		sound_pool = new SoundPool( 5, AudioManager.STREAM_MUSIC, 0 );
		sound_beep = sound_pool.load( getApplicationContext(), R.raw.beep, 1 );
	}

	public void playSound()
	{
		sound_pool.play( sound_beep, 1f, 1f, 0, 0, 1f );
	}


	public void checkAndSave() {

		if( camera_flag && weight_flag ) {
			saveProduct();
			if (Double.parseDouble(c.get_currWeight()) >= Double.parseDouble(c.get_maxWeight())){
				AlertDialog.Builder adb = new AlertDialog.Builder(this);
				adb.setTitle("무게 제한 초과");
				adb.setMessage("무게 제한이 초과되었습니다.\n물품 목록으로 돌아가시겠습니까?");
				adb.setNegativeButton("취소", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
					int which){
						if (player != null && player.isPlaying()) {
							player.stop();
							player.release();
							player = null;
						}
					}
				});
				adb.setPositiveButton("네",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								if(player != null && player.isPlaying()){
									player.stop();
									player.release();
									player = null;
								}
								Intent i = new Intent(ArduinoControllerActivity.this, ProductList.class);
								DataCenter.setAlbumId(album_id);
								DataCenter.setAlbumName(album_name);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(i);
								finish();
							}
						});
				// alarm
				Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
				player = new MediaPlayer();
				try {
					player.setDataSource(this, alert);
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
					player.setAudioStreamType(AudioManager.STREAM_ALARM);
					player.setLooping(true);
					try {
						player.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					player.start();
				}
				adb.show();
			}
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
//				Toast.makeText(this, "사진을 저장할 폴더가 없습니다.", 1).show();
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
//			Toast.makeText(getApplicationContext(), "file error", 1).show();
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

		playSound();
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