package com.example.viewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.animatetest.R;
import com.example.animatetest.Square;
import com.example.render.OpenGLRender;
import com.example.render.OpenGLRender.IOpenGLDemo;

/**
 * 类描述：
 * 
 * @Package com.example.viewport
 * @ClassName: OpenGLBookShowView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-28 下午11:13:55
 */
public class OpenGLBookShowView extends GLSurfaceView implements IOpenGLDemo, Runnable {
    private int angle;
    private Square square;
    private Square square1;
    private Square square2;
    private boolean isOntouch = false;
    private boolean isLeft;
    private int d;
    private boolean isplus;
    private float mPreviousX;
    
    public OpenGLBookShowView(Context context) {
        this(context, null);
    }

    public OpenGLBookShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_model();
        setRenderer(new OpenGLRender(this));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); //
    }

    /**
     * 方法描述：初始化数据
     * @author 尤洋
     * @Title: init_model
     * @return void
     * @date 2015-3-30 上午3:56:34
     */
    private void init_model() {
        angle = 90;
        square = new Square();
        square1 = new Square();
        square2 = new Square();
        Bitmap bitmap = decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu1, 384, 512);
        Bitmap bitmap1 = decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu2, 384, 512);
        Bitmap bitmap2 = decodeSampledBitmapFromResource
                (getResources(), R.drawable.qian, 384, 512);
        square.loadBitmap(bitmap);
        square1.loadBitmap(bitmap1);
        square2.loadBitmap(bitmap2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            removeCallbacks(this);
            isOntouch = false;
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); //
            break;
        case MotionEvent.ACTION_MOVE:
            isOntouch = false;
            float dx = x - mPreviousX;
            if (dx > 0) {
                isLeft = true;
                angle = angle - 3;
            } else {
                isLeft = false;
                angle = angle + 3;
            }

            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            isOntouch = false;
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 设置为当数据变化时才更新界面

            int a = Math.abs(angle % 360 - 90);
            int b = Math.abs(angle % 360 - 210);
            int c = Math.abs(angle % 360 - 330);
            post(this);
            d = getMiin(a, b, c);

            if (d == a && angle % 360 - 90 < 0) {
                isplus = true;
            } else if (d == b && angle % 360 - 210 < 0) {
                isplus = true;
            } else if (d == c && angle % 360 - 330 < 0) {
                isplus = true;
            } else {
                isplus = false;
            }
            Log.i("youyang", "-----isOntouch=true;-----------");
            break;

        default:
            break;
        }
        mPreviousX = x;


        return true;
    }

    /**
     * 方法描述：获取三个数字钟的最小值
     * 
     * @author 尤洋
     * @Title: getMax
     * @param a
     * @param b
     * @param c
     * @return
     * @return int
     * @date 2015-3-29 下午2:31:44
     */
    private int getMiin(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
        
    }

    @Override
    public void drawScene(GL10 gl) {
        if (isOntouch) {
            if (isLeft) {
                angle--;
            } else {
                angle++;
            }
        }
        // 测试材质效果
        // drawMateril(gl);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -6);
        gl.glPushMatrix();
        gl.glRotatef(-angle, 0, 1, 0);
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(-angle, 0, -1, 0);
        gl.glScalef(.5f, .5f, .5f);
        square.draw(gl);

        /*
         * 要理解（先旋转再平移） 和 （先平移再旋转） 的区别 角度递增的情况下 先旋转再平移的话 其实是以平移的距离为半径 进行旋转 而先平移再旋转 其实是自转 原因是 永远都按照 旋转之后的坐标系进行平移
         */
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(120 - angle, 0, 1, 0);
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(120 - angle, 0, -1, 0);
        gl.glScalef(.5f, .5f, .5f);
        square1.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(240 - angle, 0, 1, 0);
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(240 - angle, 0, -1, 0);
        gl.glScalef(.5f, .5f, .5f);
        square2.draw(gl);
        gl.glPopMatrix();
    }

    /**
     * 方法描述：绘制材质
     * 
     * @author 尤洋
     * @Title: drawMateril
     * @return void
     * @date 2015-3-30 上午12:23:30
     */
    private void drawMateril(GL10 gl) {
        float[] mat_amb = { 0.2f * 0.4f, 0.2f * 0.4f,
                0.2f * 1.0f, 1.0f, };
        float[] mat_diff = { 0.4f, 0.4f, 1.0f, 1.0f, };
        float[] mat_spec = { 1.0f, 1.0f, 1.0f, 1.0f, };

        ByteBuffer mabb = ByteBuffer.allocateDirect(mat_amb.length * 4);
        mabb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_ambBuf = mabb.asFloatBuffer();
        mat_ambBuf.put(mat_amb);
        mat_ambBuf.position(0);

        ByteBuffer mdbb = ByteBuffer.allocateDirect(mat_diff.length * 4);
        mdbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();
        mat_diffBuf.put(mat_diff);
        mat_diffBuf.position(0);

        ByteBuffer msbb = ByteBuffer.allocateDirect(mat_spec.length * 4);
        msbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_specBuf = msbb.asFloatBuffer();
        mat_specBuf.put(mat_spec);
        mat_specBuf.position(0);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,
                GL10.GL_AMBIENT, mat_ambBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,
                GL10.GL_DIFFUSE, mat_diffBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,
                GL10.GL_SPECULAR, mat_specBuf);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK,
                GL10.GL_SHININESS, 64.0f);
    }

    @Override
    public void run() {
        for (int i = 0; i < d; i++) {
            if (isplus) {
                angle++;
            } else {
                angle--;
            }
            SystemClock.sleep(10);
            requestRender();
        }
    }

    /**
     * 初始化光源
     */
    @Override
    public void initScene(GL10 gl) {
        float[] mat_amb = { 0.2f * 1.0f, 0.2f * 0.4f, 0.2f * 0.4f, 1.0f, };
        float[] mat_diff = { 1.0f, 0.4f, 0.4f, 1.0f, };
        float[] mat_spec = { 1.0f, 1.0f, 0.5f, 1.0f, };

        ByteBuffer mabb = ByteBuffer.allocateDirect(mat_amb.length * 4);
        mabb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_ambBuf = mabb.asFloatBuffer();
        mat_ambBuf.put(mat_amb);
        mat_ambBuf.position(0);

        ByteBuffer mdbb = ByteBuffer.allocateDirect(mat_diff.length * 4);
        mdbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();
        mat_diffBuf.put(mat_diff);
        mat_diffBuf.position(0);

        ByteBuffer msbb = ByteBuffer.allocateDirect(mat_spec.length * 4);
        msbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_specBuf = msbb.asFloatBuffer();
        mat_specBuf.put(mat_spec);
        mat_specBuf.position(0);

        gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_specBuf);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64.0f);

        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0.0f, 0.0f, 10.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);

    }

    
    /**
     * google文档上提供的计算图片 宽高最大是你要想的宽高的 2的几次冥
     *  Calculate the largest inSampleSize value that is a power of 2 and keeps both
        height and width larger than the requested height and width.
     * @author 尤洋
     * @Title: calculateInSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     * @return int
     * @date 2015-3-30 上午2:14:50
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 
     * 方法描述： 保持不失真的情况下 压缩图片
     * @author 尤洋
     * @Title: decodeSampledBitmapFromResource
     * @param res    资源对象
     * @param resId    资源id
     * @param reqWidth  需要的图片的宽度
     * @param reqHeight 需要的图片的高度
     * @return
     * @return Bitmap
     * @date 2015-3-30 上午2:16:44
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
            int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
