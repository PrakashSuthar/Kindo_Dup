package com.example.prakash.kindo_puzz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.versionedparcelable.ParcelImpl;

//Refer https://www.mathworks.com/help/supportpkg/android/ref/gyroscope.html
class Puzzle1_Layout extends SurfaceView implements Runnable{
    Thread thread=null;
    boolean Draw=true;
    Bitmap background,background2,cock,cock2;
    Canvas canvas;
    Paint textPaint,greenPaint,brownPaint;
    SurfaceHolder surfaceHolder;
   // Context context
    int height,width,cock_x,cock_y;
    boolean inverted=false;
    //Use Gyroscopic Sensor
    SensorManager sensorManager;
    SensorEventListener accListener;
    Sensor accSensor;
    boolean inc=false,finished=false;
    Context con;
    public Puzzle1_Layout(final Context context) {
        super(context);
        surfaceHolder=getHolder();
        con=context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Log.i("Height:",""+height);
        Log.i("Width",""+width);
        background=BitmapFactory.decodeResource(getResources(),R.drawable.tree);
        background2= Bitmap.createScaledBitmap(background,(int)(0.8*width),(int)(0.8*height),true);
        cock=BitmapFactory.decodeResource(getResources(),R.drawable.cock);
        cock_x = (int) (0.5 * width);
        cock_y = (int) (0.2 * height);
        cock2= Bitmap.createScaledBitmap(cock,(int)(width*0.4),(int)(height*0.4),true);
        //cock2= Bitmap.createScaledBitmap(cock,cock_x,cock_y,true);
        sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        System.out.println("Sensor Manageer:"+sensorManager);
        accSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accSensor==null){
            Toast.makeText(context,"This Device has no Gyroscopic Sensor",Toast.LENGTH_SHORT).show();
        }
        System.out.println("Gyroscopic Sensor Value:"+accSensor);

        accListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //Action to be performed.
                if(sensorEvent.values[1] < -5f) { // clockwise
                    inverted=true;
                    System.out.println(sensorEvent.values[2]);
                    System.out.println(sensorEvent.values[1]);
                    System.out.println(sensorEvent.values[0]);
                    System.out.println("Inverted Portrait");
                    // getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
                else{
                    boolean pre=inverted;
                    inverted=false;
                    System.out.println(sensorEvent.values[2]);
                    System.out.println(sensorEvent.values[1]);
                    System.out.println(sensorEvent.values[0]);
                    System.out.println("Normal Portrait");
                    inverted=pre;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };


        //cock2=cock2.copy();
        //cock2.setHasAlpha(true);
    }

    @Override
    public void run() {
        setupPaintBrushes();
        //Draw Directly onto the Canvas..
        //Register Sensor Listener to the Sensor Manager
        sensorManager.registerListener(accListener,accSensor,SensorManager.SENSOR_DELAY_NORMAL);

        while (Draw){
            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }
            //get Hold of Canvas while we start to draw..

                //Above line unholds the canvas and draws the Bitmap onto the canvas.
                if(inverted==true)
                    changeCockCordinates();

                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    canvas.drawBitmap(background2, (int) (0.1 * width), (int) (0.1 * height), null);
                    //cock_x = (int) (0.5 * width);
                   // cock_y = (int) (0.1 * height);
                    canvas.drawBitmap(cock2, cock_x, cock_y, null);

            Paint paint=new Paint();
            paint.setColor(Color.TRANSPARENT);
            float textHeight = textPaint.descent() - textPaint.ascent();
            float textOffset = (textHeight / 2) - textPaint.descent();

            RectF bounds = new RectF(0, 0, width,(float)0.1*height);
            canvas.drawOval(bounds, paint);
            canvas.drawText("Mali is Stuck on tree", bounds.centerX(), bounds.centerY() + textOffset, textPaint);
            bounds = new RectF(0, 0, width,(float)0.1*height*2);
            canvas.drawText("Help him get Down", bounds.centerX(), bounds.centerY() + textOffset, textPaint);



                    surfaceHolder.unlockCanvasAndPost(canvas);

            if(finished==true){
                //Display Successfull Message.
                //Call New Activity
                success();
            }
        }
    }
    public void success(){
        Intent intent=new Intent(getContext(),Puzzle2.class);
        getContext().startActivity(intent);
    }
    public void pause() {
        Draw=false;
        while (true) {
            try {
                thread.join();
                break;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//while for case where try block throws Exception
        //Now Set thread to null
        thread=null;
        sensorManager.unregisterListener(accListener);

    }

    public void resume() {
        Draw=true;
        thread=new Thread(this);
        thread.start();
    }
    public void setupPaintBrushes(){
        textPaint=new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);

        greenPaint=new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStyle(Paint.Style.FILL);

        brownPaint=new Paint();
        brownPaint.setColor(Color.rgb(102,51,0));
        brownPaint.setStyle(Paint.Style.FILL);
    }

    public void changeCockCordinates(){
        if(inc==false)
            cock_y-=50;

        else{
            if(cock_y<.9*height)
            cock_y+=50;
            else
                finished=true;
        }
        if(cock_y<=0.12*height)
            inc=true;
        System.out.println("Cock_y"+cock_y);
    }

}
