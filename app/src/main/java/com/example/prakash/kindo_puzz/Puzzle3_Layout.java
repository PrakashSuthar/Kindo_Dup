package com.example.prakash.kindo_puzz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

class Puzzle3_Layout extends SurfaceView implements Runnable{
    Thread thread;
    boolean Draw=true,touch=false,night;
    Bitmap mountains;

    Canvas canvas;
    Paint textPaint,circlePaint,brownPaint,Porterpaint;
    SurfaceHolder surfaceHolder;
    DisplayMetrics displayMetrics;
    WindowManager windowManager;
    int height,width;
    float circle_x,circle_y,radius;
    float x,y,pre_y;//x,y are positions of touch point

    public Puzzle3_Layout(Context context) {
        super(context);
        surfaceHolder=getHolder();
        //Display metrics gives us Display screen parameterss heigth and width
        displayMetrics = new DisplayMetrics();
        windowManager=((Activity)getContext()).getWindowManager();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        //width and height are for the Android Screen Size...

        mountains=BitmapFactory.decodeResource(getResources(),R.drawable.mountains);
        System.out.println("mountain Width:"+mountains.getWidth()+"\nMountain Height:"+mountains.getHeight());

        Log.i("Height:",""+height);
        Log.i("Width",""+width);
        mountains = Bitmap.createScaledBitmap(mountains,width,(int)(0.7*height )   ,true);
        System.out.println("mountain Width:"+mountains.getWidth()+"\nMountain Height:"+mountains.getHeight());
        circle_x=(float)0.78*width;
        pre_y=circle_y=(float) 0.16*height;
        radius=(float) 0.1*width;
        //cock2=cock2.copy();
        //cock2.setHasAlpha(true);
    }

    @Override
    public void run() {
        setupPaintBrushes();
        //Draw Directly onto the Canvas..
        while (Draw){
            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }


            //get Hold of Canvas while we start to draw..
           // if(touch==false) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                canvas.drawCircle(circle_x,circle_y,radius,circlePaint);
                canvas.drawBitmap(mountains, 0 ,(int)(0.2*height), null);

            //canvas.drawRect((int)(0.1*width),(int)(0.70*height));
                //canvas.drawRec

               // canvas.drawPath(mPath, Porterpaint);
                //Text should be the topmost ...
            Paint paint=new Paint();
            paint.setColor(Color.TRANSPARENT);
            float textHeight = textPaint.descent() - textPaint.ascent();
            float textOffset = (textHeight / 2) - textPaint.descent();

            RectF bounds = new RectF(0, 0, width,(float)0.1*height);
            canvas.drawOval(bounds, paint);
            canvas.drawText("Roosi can't get help,nor can we.", bounds.centerX(), bounds.centerY() + textOffset, textPaint);
            bounds = new RectF(0, 0, width,(float)0.1*height*2);
            canvas.drawText("Help!", bounds.centerX(), bounds.centerY() + 3*textOffset, textPaint);
                surfaceHolder.unlockCanvasAndPost(canvas);
           // }
          //  else
           // {
           //     canvas=surfaceHolder.lockCanvas();
           //     canvas.drawPath();
           //     surfaceHolder.unlockCanvasAndPost(canvas);
           // }
            //Above line unholds the canvas and draws the Bitmap onto the canvas.
        }
    }

    public void pause() {
        Draw=false;
        while (true) {
            try {
                thread.join();
                break;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//while for case where try block throws Exception
        //Now Set thread to null
        thread=null;

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
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        circlePaint=new Paint();
        circlePaint.setColor(Color.YELLOW);
        circlePaint.setTextSize(100);
        circlePaint.setStyle(Paint.Style.FILL);

        brownPaint=new Paint();
        brownPaint.setColor(Color.rgb(102,51,0));
        brownPaint.setStyle(Paint.Style.FILL);
        brownPaint.setAntiAlias(true);
        brownPaint.setDither(true);
        brownPaint.setColor(Color.WHITE);
        brownPaint.setStyle(Paint.Style.STROKE);
        brownPaint.setStrokeJoin(Paint.Join.ROUND);
        brownPaint.setStrokeCap(Paint.Cap.ROUND);
        brownPaint.setStrokeWidth(12);

    }

    private Point getScreenSize() {

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
            if(x>=(circle_x-radius)&&x<=circle_x+radius){
                if(y>=circle_y-radius && y<=circle_y+radius)
                {
                    System.out.println("Circle Held in hand");
                    {
                        if(y>pre_y){

                            circle_y=y;
                        }
                    }
                }
            }

    }

    private void touch_move(float x, float y) {
        if(y>pre_y){
            circle_y=y;
        }
    }

    private void touch_up() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touch=true;
        x = event.getX();
        y = event.getY();
       // night=false;
        System.out.println("Handle Touch Event");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                //invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                //invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                //invalidate();
                break;
        }
        if(y>=0.6*height)
        {
            night=true;
            success();
        }
        return true;
    }


    public void success(){
        Intent intent=new Intent(getContext(),Puzzle1.class);
        getContext().startActivity(intent);
    }
}

