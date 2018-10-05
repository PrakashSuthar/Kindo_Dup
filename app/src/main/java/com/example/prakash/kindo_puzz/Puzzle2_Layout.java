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

class Puzzle2_Layout extends SurfaceView implements Runnable{
    Thread thread;
    boolean Draw=true,touch=false,inRect=false,treasureRevealed=false,down=false,move=false,up=false,startSet1=false,startSet2=false,outside=true;
    Bitmap monsterRed,monsterBlue,treasurEmpty,treasureFull,Lump1,Lump2;
    private static final float TOUCH_TOLERANCE = 4;
    Canvas canvas;
    Paint textPaint,greenPaint,brownPaint,Porterpaint;
    //private Paint circlePaint;
    private Path circlePath,circlePath2;
    Path mPath;
    SurfaceHolder surfaceHolder;
    DisplayMetrics metrics;
    WindowManager windowManager;
    int height,width,monster_x,monster_y,scale_x,scale_y;
    float x,y;
    int X1,X2,Y1,Y2,X3,X4,X,Y;
    int Lwid,Lheight;
    String text="Who is the Thief??";
    public Puzzle2_Layout(Context context) {
        super(context);
        surfaceHolder=getHolder();
        //mPath = new Path();
        circlePath = new Path();
        circlePath2=new Path();
        //Display metrics gives us Display screen parameterss heigth and width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager=((Activity)getContext()).getWindowManager();
        Porterpaint = new Paint();
        Porterpaint.setAntiAlias(true);
        Porterpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//PorterDuff.Mode.CLEAR
        Porterpaint.setStrokeWidth(100);
        //Porterpaint.
       // Porterpaint.setAlpha(0);
        //circlePath.
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        scale_x=(int)(0.38*width);
        scale_y=(int)(0.3*width);
        Log.i("Height:",""+height);
        Log.i("Width",""+width);

        monsterRed = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.moster_red),scale_x,scale_y,true);
        monsterBlue= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.monster_blue),scale_x,scale_y,true);
        Lwid=monsterBlue.getWidth();
        Lheight=monsterBlue.getHeight();
        treasurEmpty=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.empty),scale_x,scale_y,true);
        treasureFull=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.fulltreasure),scale_x,scale_y,true);
        X1=(int) (0.1 * width) ;X2=X1+Lwid   ;Y1=(int) (0.40 * height)+monsterRed.getHeight()   ;Y2=Y1+Lheight;
        Lump1=Lump();
        Lump2=Lump();
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
                canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                canvas.drawBitmap(monsterBlue, (int) (0.1 * width), (int) (0.40 * height), null);
                canvas.drawBitmap(monsterRed, (int) (0.55 * width), (int) (0.40 * height), null);
                X3=(int) (0.55 * width);
            X4=(int) (0.55 * width)+monsterBlue.getWidth();
            canvas.drawBitmap(treasurEmpty,(int) (0.1 * width),(int) (0.40 * height)+monsterRed.getHeight(),null);
            canvas.drawBitmap(treasureFull,(int) (0.55 * width),(int) (0.40 * height)+monsterBlue.getHeight(),null);
                canvas.drawBitmap(Lump1,(int) (0.1 * width),(int) (0.40 * height)+monsterRed.getHeight(),null);
            canvas.drawBitmap(Lump2,(int) (0.55 * width),(int) (0.40 * height)+monsterBlue.getHeight(),null);


            float textHeight = textPaint.descent() - textPaint.ascent();
            float textOffset = (textHeight / 2) - textPaint.descent();

            RectF bounds = new RectF(0, 0, width,(float)0.1*height);
            canvas.drawText(text, bounds.centerX(), bounds.centerY() + textOffset, textPaint);
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


        greenPaint=new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setTextSize(100);
        greenPaint.setStyle(Paint.Style.FILL);

        brownPaint=new Paint();
        //brownPaint.setColor(Color.rgb(102,51,0));
        //brownPaint.setStyle(Paint.Style.FILL);
        brownPaint.setAntiAlias(true);
        //brownPaint.setDither(true);
        brownPaint.setColor(Color.RED);
        brownPaint.setStyle(Paint.Style.STROKE);
        brownPaint.setStrokeJoin(Paint.Join.ROUND);
        brownPaint.setStrokeCap(Paint.Cap.ROUND);
        //brownPaint.setStrokeWidth(12);
        brownPaint.setStrokeWidth(5);
        Porterpaint.setStrokeCap(Paint.Cap.ROUND);
        Porterpaint.setStrokeJoin(Paint.Join.ROUND);
       // Porterpaint.setStrokeJoin();
    }

    private Bitmap punchAHoleInABitmap(Bitmap foreground) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(foreground, 0, 0, Porterpaint);

        float radius = (float)(getScreenSize().x *.35);
        radius=10;
        canvas.drawCircle(x, y, radius, brownPaint);
        //canvas.drawColor(Color.GREEN, PorterDuff.Mode.CLEAR);
        return bitmap;
    }



    private Bitmap punchInLump1(){
        Bitmap bitmap = Bitmap.createBitmap(Lump1.getWidth(), Lump1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(bitmap);
       // Porterpaint = new Paint();
        can.drawBitmap(Lump1, 0, 0, null);
       // Porterpaint.setAntiAlias(true);
       // Porterpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//PorterDuff.Mode.CLEAR
       // Porterpaint.setAlpha(0);
        float radius = (float)(getScreenSize().x *0.001);
        radius=10;
        if(up){
            circlePath.lineTo(X-X1,Y-Y1);
            circlePath.reset();
            startSet1=false;
        }
        if(down){
            mX=x;
            mY=y;
            circlePath.reset();
            circlePath.moveTo(X-X1,Y-Y1);
            startSet1=true;
        }
        if(move){
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                //mX is previous x
                circlePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
            if(startSet1) {

                circlePath.lineTo(X - X1, Y - Y1);
            }
            else
            {
                startSet1=true;
                circlePath.reset();
                circlePath.moveTo(X-X1,Y-Y1);

            }
        }
        can.drawPath(circlePath,Porterpaint);
        return bitmap;
    }
    private Bitmap punchInLump2(){
        Bitmap bitmap = Bitmap.createBitmap(Lump1.getWidth(), Lump1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(bitmap);
        can.drawBitmap(Lump2, 0, 0, null);

        if(up){
            circlePath2.lineTo(X-X3,Y-Y1);
            circlePath.reset();
            startSet2=false;
        }
        if(down){
            circlePath2.reset();
            circlePath2.moveTo(X-X3,Y-Y1);
            startSet2=true;
        }
        if(move){
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                circlePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
            if(startSet2){

            circlePath2.lineTo(X-X3,Y-Y1);
        }
        else{
                startSet2=true;
                circlePath2.reset();
                circlePath2.moveTo(X-X3,Y-Y1);
            }
        }
        can.drawPath(circlePath2,Porterpaint);
        //can.drawCircle(X-X1, Y-Y1, radius, Porterpaint);
        System.out.println("circle Drawn;");
        return bitmap;

    }

    private boolean deterPunch(){
        int rect=0;
        //For Rectangle 1;
        if(X>=X1-20 &&X<=X2+20){
            if(Y>=Y1-20&&Y<=Y2+20) {
              //  outside=false;
                Lump1=punchInLump1();
                System.out.println("Inside Rectangle 1");
            return true;
            }
        }
        else {
            System.out.println("REctandle X="+X+"\nY="+Y);
            System.out.println("REctandle X3="+X3+"\nX4="+X4);
            if (X >= X3-20 && X<=X4+20){
                if(Y>=Y1-20&&Y<=Y2+20){
                   // outside=false;
                    Lump2= punchInLump2();
                    System.out.println("Inside Rectangle 2");
                return true;
                }
            }
            outside=true;
            if(startSet1)
			startSet1=false;
            startSet2=false;

        }
        canvas=surfaceHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(monsterBlue,(int)(0.1*width),(int)(0.40*height),null);
        //monsterRed=punchAHoleInABitmap(monsterRed);
        canvas.drawBitmap(monsterRed,(int)(0.55*width),(int)(0.40*height),null);
        //canvas.drawRect((int)(0.1*width),(int)(0.70*height));
        //canvas.drawRec
        canvas.drawBitmap(treasurEmpty,(int) (0.1 * width),(int) (0.40 * height)+monsterRed.getHeight(),null);
        canvas.drawBitmap(treasureFull,(int) (0.55 * width),(int) (0.40 * height)+monsterBlue.getHeight(),null);
       // if(!isTransparent(Lump1))
            canvas.drawBitmap(Lump1,(int) (0.1 * width),(int) (0.40 * height)+monsterRed.getHeight(),null);
        //if(!isTransparent(Lump2))
            canvas.drawBitmap(Lump2,(int) (0.55 * width),(int) (0.40 * height)+monsterBlue.getHeight(),null);

        //Text should be the topmost ...
        // canvas.drawPath(mPath,  Porterpaint);
        Paint paint=new Paint();
        paint.setColor(Color.TRANSPARENT);
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();

        RectF bounds = new RectF(0, 0, width,(float)0.1*height);
        canvas.drawOval(bounds, paint);
        canvas.drawText(text, bounds.centerX(), bounds.centerY() + textOffset, textPaint);
        surfaceHolder.unlockCanvasAndPost(canvas);

        return false;

    }
    private Point getScreenSize() {

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
    private float mX, mY;
    private void touch_start(float x, float y) {

        inRect=deterPunch();
        circlePath.reset();
        //mPath.moveTo(x, y);
        mX = x;
        mY = y;
        inRect=deterPunch();
        canvas=surfaceHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(monsterBlue,(int)(0.1*width),(int)(0.40*height),null);
        canvas.drawBitmap(monsterRed,(int)(0.55*width),(int)(0.60*height),null);
        canvas.drawBitmap(treasurEmpty,(int) (0.1 * width),(int) (0.40 * height)+monsterBlue.getHeight(),null);
        canvas.drawBitmap(treasureFull,(int) (0.55 * width),(int) (0.40 * height)+monsterBlue.getHeight(),null);
        canvas.drawBitmap(Lump1,(int) (0.1 * width),(int) (0.40 * height)+monsterRed.getHeight(),null);
        canvas.drawBitmap(Lump2,(int) (0.55 * width),(int) (0.40 * height)+monsterBlue.getHeight(),null);

        Paint paint=new Paint();
        paint.setColor(Color.TRANSPARENT);
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();

        RectF bounds = new RectF(0, 0, width,(float)0.1*height);
        canvas.drawOval(bounds, paint);
        canvas.drawText(text, bounds.centerX(), bounds.centerY() + textOffset, textPaint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touch=true;
        x = event.getX();
        y = event.getY();

        X=(int)x;Y= (int)y;
        inRect=false;
        System.out.println("Handle Touch Event");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down=true;
                inRect= deterPunch();
                down=false;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                move=true;
                inRect= deterPunch();
                move=false;

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                up=true;
                inRect= deterPunch();
                up=false;
                invalidate();
                break;
        }
        if(isTransparent(Lump1)&&isTransparent(Lump2))
        {
            treasureRevealed=true;
            success();
        }
        return true;
    }
    public Bitmap Lump() {
        Bitmap bitmap = Bitmap.createBitmap(
                monsterBlue.getWidth(), // Width
                monsterBlue.getHeight(), // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Canvas c=new Canvas(bitmap);
        c.drawColor(Color.GREEN);
    return bitmap;
    }

    private boolean isTransparent(Bitmap b){
        Bitmap bitmap=Bitmap.createBitmap(b.getWidth(),b.getHeight(), b.getConfig());

        if(b.sameAs(bitmap))
        {

            System.out.println("Revealed");
            return true;
        }
        else
        {
            System.out.println("Not Revealed");
            return false;
        }
    }

    public void success(){
        text="Thief Revealed";
        Intent intent=new Intent(getContext(),Puzzle3.class);
        getContext().startActivity(intent);
    }
}

