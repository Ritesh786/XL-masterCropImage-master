package com.extralarge.fujitsu.xl.ReporterSection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

//import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

//import tourntravels.lue.com.tourstravel.Manifest;
//import tourntravels.lue.com.tourstravel.MarshmallowPermission;
//

public class UtilityClass {

	public static ProgressDialog progressDialog;
	public static TelephonyManager tm;

	public UtilityClass() {
		// TODO Auto-generated constructor stub


	}


	public static void showProgressDialog(Context context,String msg)
	{
		progressDialog=new ProgressDialog(context);
		progressDialog.setMessage(msg);
		progressDialog.show();
	}

	public static void closeProgressDialog()
	{
		if(progressDialog.isShowing()) progressDialog.dismiss();
	}

	public static void ShowMessage(Context context, String Title, String Message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(Title);
		alertDialog.setMessage(Message);
		alertDialog.show();
	}

//	public static String getMacId(Context context)
//	{
//		String macId=null;
//		MarshmallowPermission permission=new MarshmallowPermission(context, Manifest.permission.ACCESS_WIFI_STATE);
//		if(permission.result==-1 || permission.result==0)
//		{
//			try
//			{
//				macId=getMacAddress(context);
//			}catch(Exception e){}
//		}
//		else if(permission.result==1)
//		{
//			macId=getMacAddress(context);
//		}
//		return macId;
//	}


//	private static String getMacAddress(Context context)
//	{
//		WifiManager manager;
//		String macId=null;
//		manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		if(manager!=null)
//			macId= manager.getConnectionInfo().getMacAddress();
//		return macId;
//	}

//	public static String getIMEI(Context context)
//	{
//		String imei=null;
//		MarshmallowPermission permission=new MarshmallowPermission(context, Manifest.permission.READ_PHONE_STATE);
//		if(permission.result==-1 || permission.result==0)
//		{
//			try
//			{
//				tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//				if(tm!=null)imei = tm.getDeviceId();
//			}catch(Exception e){}
//		}
//		else if(permission.result==1)
//		{
//			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			if(tm!=null)imei = tm.getDeviceId();
//		}
//
//		return imei;
//	}

	public static void setStatusBarColor(Activity activity) {
		if (Build.VERSION.SDK_INT >= 21) {

			Window window = activity.getWindow();


			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			// finally change the color
			window.setStatusBarColor(Color.parseColor("#004488"));

		}
	}


	public static Bitmap getImage(String imageName,String folderName)
	{
		Bitmap bitmap=null;
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), folderName);
		try {
			File mypath = new File(mediaStorageDir.getPath(), imageName);
			bitmap= BitmapFactory.decodeStream(new FileInputStream(mypath));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return bitmap;
	}


	public static Bitmap getImage(String imageName)
	{
		Bitmap bitmap=null;
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),GlobalVariables.FOLDER_NAME);
		try {
			File mypath = new File(mediaStorageDir.getPath(), imageName);
			bitmap= BitmapFactory.decodeStream(new FileInputStream(mypath));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return bitmap;
	}

	public static Uri getImageUri(String imageName)
	{
		Uri imageUri=null;
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),GlobalVariables.FOLDER_NAME);
		try {
			File mypath = new File(mediaStorageDir.getPath(), imageName);
			imageUri=Uri.parse(mypath.toString());
		}catch (Exception e){}
		return imageUri;
	}


	public static boolean deleteimagefromfolder(String fileName)
	{
		boolean imagedeleted=false;
		try {
			File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), GlobalVariables.FOLDER_NAME);
			File file = new File(mediaStorageDir.getAbsolutePath() + File.separator + fileName);
			imagedeleted=file.delete();
		}
		catch(Exception e)
		{e.printStackTrace();}
		return imagedeleted;
	}

	
	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected() == true);
	}




	public static String getDateString(String Formats) {
		SimpleDateFormat postFormater = new SimpleDateFormat(Formats);

		String newDateStr = postFormater.format(Calendar.getInstance()
				.getTime());
		return newDateStr;
	}




//	public static String readResponse(HttpResponse httpResponse) {
//		InputStream is=null;
//		String return_text="";
//		try {
//			is=httpResponse.getEntity().getContent();
//			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
//			String line="";
//			StringBuffer sb=new StringBuffer();
//			while ((line=bufferedReader.readLine())!=null)
//			{
//				sb.append(line);
//			}
//			return_text=sb.toString();
//			Log.d("return_text",""+return_text);
//		} catch (Exception e)
//		{
//
//		}
//		return return_text;
//
//
//	}

	public static Bitmap GenerateThumbnail(Bitmap imageBitmap,
										   int THUMBNAIL_HEIGHT, int THUMBNAIL_WIDTH) {

		Float width = new Float(imageBitmap.getWidth());
		Float height = new Float(imageBitmap.getHeight());
		Float ratio = width / height;
		Bitmap CompressedBitmap=null;
		if(THUMBNAIL_HEIGHT>0) {
			CompressedBitmap = Bitmap.createScaledBitmap(imageBitmap,
					(int) (THUMBNAIL_HEIGHT * ratio), THUMBNAIL_HEIGHT, false);
		}
		return CompressedBitmap;
	}


	public static byte[] convertBitmapToByte(Bitmap bitmap)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if(bitmap!=null) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
		}
	    return baos.toByteArray();
	}


	public static boolean saveImage(String filename,Bitmap image) throws FileNotFoundException {
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), GlobalVariables.FOLDER_NAME);
		File file = new File(mediaStorageDir.getAbsolutePath() + File.separator + filename);
		FileOutputStream fo = new FileOutputStream(file);

		if(!mediaStorageDir.exists()) {
			mediaStorageDir.mkdir();
		}

		if (image != null) {
			image.compress(Bitmap.CompressFormat.PNG, 100, fo);
			return true;
		} else return false;
	}


	public static String getRandomString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 10) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}



	public static Bitmap getImageFromUri(Context context,Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap =MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
