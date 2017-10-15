package com.example.ishaangrover.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ishaangrover on 7/17/17.
 */

public class DrawingView extends View {
    private Paint mPaint;
    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;


    public DrawingView(Context c, AttributeSet attrs) {

        super(c, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(80);
        context=c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        setDrawingCacheEnabled( true );



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


    public byte[] getDrawing()
    {
        Bitmap whatTheUserDrewBitmap = getDrawingCache();
        // don't forget to clear it (see above) or you just get duplicates

        // almost always you will want to reduce res from the very high screen res
        whatTheUserDrewBitmap =
                ThumbnailUtils.extractThumbnail(whatTheUserDrewBitmap, 256, 256);
        // NOTE that's an incredibly useful trick for cropping/resizing squares
        // while handling all memory problems etc
        // http://stackoverflow.com/a/17733530/294884

        // these days you often need a "byte array". for example,
        // to save to parse.com or other cloud services


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        whatTheUserDrewBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] yourByteArray;
        yourByteArray = baos.toByteArray();
        Log.e("MyApp", yourByteArray.length + "");
//        String mainPath = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/ExperimentData/";
//        final Handler handler = new Handler();
//        File dir = new File(mainPath);
//        dir.mkdirs();
//        try {
//            writeFile(yourByteArray, mainPath + "picture.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return yourByteArray;
    }

    public void writeFile(byte[] data, String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);

        out.write(data);
        out.close();
    }

}