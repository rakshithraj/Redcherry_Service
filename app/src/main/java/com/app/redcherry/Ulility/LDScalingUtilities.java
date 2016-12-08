package com.app.redcherry.Ulility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;




public class LDScalingUtilities {
	final static String  SERVER_TIME_OFFSET="server_time_offset";
	final static String  SERVER_TIME="server_time";
	final static String  TIME_ZONE="time_zone";
	
	/**
	 *check   keyboard is visible or not
	 * @param rootView
	 * @return true if visible else false
	 */
	public static boolean isSoftKeyBoardVisible(View rootView) {
		// TODO Auto-generated method stub
		int heightDiff = rootView.getRootView().getHeight()
				- rootView.getHeight();
		// if more than 100 pixels, its probably a keyboard...
		// keyboard is visible, do something here
// keyboard is not visible, do something here
		return heightDiff > 100;
	}

	/**
	 * get rounded bitmap
	 * @param scaleBitmapImage
	 * @param targetWidth
	 * @param targetHeight
	 * @param matrix
	 * @return bitmap
	 */
	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,
			int targetWidth, int targetHeight, Matrix matrix) {

		if(targetWidth==0 || targetHeight==0){
			targetWidth=50;
			targetHeight=50;
		}

		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		canvas.drawBitmap(scaleBitmapImage, new Rect(0, 0, scaleBitmapImage.getWidth(),
				scaleBitmapImage.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);

		targetBitmap = getborder(targetBitmap);

		return Bitmap
				.createBitmap(targetBitmap, 0, 0, targetBitmap.getWidth(),
						targetBitmap.getHeight(), matrix, true);
	}

	/**
	 * get border on bitmap
	 * @param bitmap
	 * @return bitmap
	 */
	private static Bitmap getborder(Bitmap bitmap) {
		// TODO Auto-generated method stub

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int radius = Math.min(h / 2, w / 2);
		Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);

		Paint p = new Paint();
		p.setAntiAlias(true);

		Canvas c = new Canvas(output);
		c.drawARGB(0, 0, 0, 0);
		p.setStyle(Style.FILL);

		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		c.drawBitmap(bitmap, 4, 4, p);
		p.setXfermode(null);
		p.setStyle(Style.STROKE);
		p.setColor(Color.WHITE);
		p.setStrokeWidth(3);
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		return output;
	}

	/**
	 * check image ORIENTATION
	 * @param exifOrientation
	 * @return ORIENTATION as float
	 */
	public static float exifToDegrees(float exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}
	
	
	

	/**
	 * rotate a image in file to required ORIENTATION
	 * @param activity
	 * @param file
	 * @return file
	 * @throws IOException
	 */
	public static File convertInToReqiuredOrientation(Activity activity,File file) throws IOException {
		try {
			String Path = activity.getFilesDir().getAbsolutePath() + "/"+file.getName();

			File f = new File(Path);
			copyFile(file, f);
			
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}
			// System.out.println("convertInToReqiuredOrientation");
			Matrix mat = new Matrix();
			mat.postRotate(angle);
			Bitmap correctBmp;
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			Bitmap bmp1 = BitmapFactory.decodeStream(new FileInputStream(f),null, null);
			correctBmp = Bitmap.createBitmap(bmp1, 0, 0, bmp1.getWidth(),
					bmp1.getHeight(), mat, true);
			FileOutputStream fOut = new FileOutputStream(f.getAbsolutePath());
			correctBmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
			fOut.flush();
			fOut.close();
			return f;
			// System.out.println("end convertInToReqiuredOrientation");
         
		} catch (OutOfMemoryError e) {
			Log.d("newtag","error="+e.toString());
			//System.out.println("" + e.getMessage());
		} catch (IOException e) {
			Log.d("newtag","error="+e.toString());
			//System.out.println("" + e.getMessage());
		} catch (Exception e) {
			Log.d("newtag","error="+e.toString());
		}
		return file;
	}
	
	
	
	
	
	/**
	 * add Smily
	 * @param dd
	 * @return SpannableStringBuilder
	 */
	public static SpannableStringBuilder addSmily(Drawable dd) {
		 dd.setBounds(0, 0, dd.getIntrinsicWidth(),dd.getIntrinsicHeight());
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(":-)");
		builder.setSpan(new ImageSpan(dd), builder.length() - ":-)".length(),builder.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return builder;
		}

		  /**
		   * convert view to drawable
		   * @param view
		   * @param Activity
		   * @return Object
		   */
		  public static Object getDrawableFromTExtView( View view,Activity Activity) {

		    int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		    view.measure(spec, spec);
		    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		    Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
		            Config.ARGB_8888);
		    Canvas c = new Canvas(b);
		    c.translate(-view.getScrollX(), -view.getScrollY());
		    view.draw(c);
		    view.setDrawingCacheEnabled(true);
		    Bitmap cacheBmp = view.getDrawingCache();
		    Bitmap viewBmp = cacheBmp.copy(Config.ARGB_8888, true);
		    view.destroyDrawingCache();
		   // return new BitmapDrawable(viewBmp);
		    return new BitmapDrawable(Activity.getResources(), viewBmp);

		}
		  
		  
		  
		  
		  

			  

/**
 * set date to textview
 * @param dayOfWeek
 * @param tvNewsfeedTime 
 * @param hour_min_sec
 * @param isNewsfeed as boloean true if newsfeed
 * @param isChatScreen as boloean true if ChatScreen
 * @return none
 */
		  public static  void setDay(String dayOfWeek, TextView tvNewsfeedTime,String hour_min_sec,boolean isNewsfeed, boolean isChatScreen) {
				// TODO Auto-generated method stub
		     	 if( isNewsfeed)
		     		tvNewsfeedTime.setText( dayOfWeek+"\n"+hour_min_sec); 
		     	 else
		     		 if( isChatScreen)
				     		tvNewsfeedTime.setText( dayOfWeek+" "+hour_min_sec); 
				     	 else
				           tvNewsfeedTime.setText( dayOfWeek); 
			}
			
		  public static String  getHour_min_sec(String date){
			  String hour_min_sec;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); 
				  Date newfeedDate = null;
				  try {
					  newfeedDate = formatter.parse(date);
					} catch (ParseException e) {
					    e.printStackTrace();
					} 
				  Calendar newfeedcal = Calendar.getInstance();
				  newfeedcal.setTime(newfeedDate);
				  
				 // hour_min_sec=newfeedcal.get(Calendar.HOUR)+":"+newfeedcal.get(Calendar.MINUTE)+":"+newfeedcal.get(Calendar.SECOND);
				  
				  hour_min_sec = (String) DateFormat.format("h:mm:ss a", newfeedcal.getTime());
					  
				/*  if(hour_min_sec.charAt(0)=='0'){
					  hour_min_sec=hour_min_sec.substring(1);
				  }
					  */
				  //hour_min_sec=hour_min_sec.replace("AM", "am").replace("PM","pm");
			  return hour_min_sec;
		  }
		  
		  /**
		   * get Year_Month_Day from date
		   * @param date
		   * @return Year_Month_Day as String
		   */
		  public static String  getDYear_Month_Day(String date){
			  String year_montn_day;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); 
				  Date newfeedDate = null;
				  try {
					  newfeedDate = formatter.parse(date);
					} catch (ParseException e) {
					    e.printStackTrace();
					} 
				  Calendar newfeedcal = Calendar.getInstance();
				  newfeedcal.setTime(newfeedDate);
				  
				 // hour_min_sec=newfeedcal.get(Calendar.HOUR)+":"+newfeedcal.get(Calendar.MINUTE)+":"+newfeedcal.get(Calendar.SECOND);
				  year_montn_day = (String) DateFormat.format("MM/dd/yyyy", newfeedcal.getTime());
				  
			  return year_montn_day;
		  }
	
		  

		    
		/**
		 * copy file from source to destination
		 * @param sourceFile
		 * @param destFile
		 * @return true if success else false
		 * @throws IOException
		 */
		    	  private static void copyFile(File sourceFile, File destFile)
		    	      throws IOException {
		    	    if (!destFile.exists()) {
		    	      destFile.createNewFile();

		    	      FileChannel source = null;
		    	      FileChannel destination = null;
		    	      try {
		    	        source = new FileInputStream(sourceFile).getChannel();
		    	        destination = new FileOutputStream(destFile).getChannel();
		    	        destination.transferFrom(source, 0, source.size());
		    	      } finally {
		    	        if (source != null)
		    	          source.close();
		    	        if (destination != null)
		    	          destination.close();
		    	      }
					}
				  }

		   /**
		    * read a file
		    * @param file
		    * @return file content as String
		    * @throws IOException
		    */
		    	  public String readTextFile(File file) throws IOException {
		    	    byte[] buffer = new byte[(int) file.length()];
		    	    BufferedInputStream stream = new BufferedInputStream(
		    	        new FileInputStream(file));
		    	    stream.read(buffer);
		    	    stream.close();

		    	    return new String(buffer);
		    	  }

		    

		      
		    	  
		    	  /**
		    	   * get difference of server time and device time in second
		    	   * @param servertimestr a String
		    	   * @return diffSeconds as long
		    	   */
		    	  public static  long calculateOffset(String servertimestr){
		    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");  
		    			
		    	    	Date clienttime = new Date();
		    			
		    			Date servertime = null;
		    			
		    			try {
		    				servertime = formatter.parse(servertimestr);
		    			} catch (ParseException e) {
		    			    e.printStackTrace();
		    			}  
		    			
		    			// Get msec from each, and subtract.
		    			long diff = servertime.getTime() - clienttime.getTime();

					  return diff / 1000;
		    		}
		    	  
		    	  
		    	  
		    	  /**
		    	   * crop image from center 
		    	   * @param srcBmp
		    	   * @param point
		    	   * @return Bitmap
		    	   */
		    	  public static Bitmap getSquareShape(Bitmap srcBmp, Point point
		    				) {
		    		  Bitmap scaleBitmapImage;
		    		 // int temp;
		    		    if (srcBmp.getWidth() >= srcBmp.getHeight()){

		    		    	//scaleBitmapImage=ImageViewLoader.getResizedBitmap(srcBmp,srcBmp.getHeight(),srcBmp.getHeight());
		    		    	
		    			  scaleBitmapImage = Bitmap.createBitmap(
		    			     srcBmp, 
		    			     srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
		    			     0,
		    			     srcBmp.getHeight(), 
		    			     srcBmp.getHeight()
		    			     );

		    			}else{
		    				//scaleBitmapImage=ImageViewLoader.getResizedBitmap(srcBmp,srcBmp.getWidth(),srcBmp.getWidth());
		    				scaleBitmapImage = Bitmap.createBitmap(
		    			     srcBmp,
		    			     0, 
		    			     srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
		    			     srcBmp.getWidth(),
		    			     srcBmp.getWidth() 
		    			     );
		    			}
		    			
		    		
		    			return scaleBitmapImage;
		    		}	  
		    	  
		    	  
}
