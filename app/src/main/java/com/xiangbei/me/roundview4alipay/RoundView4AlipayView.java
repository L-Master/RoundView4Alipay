package com.xiangbei.me.roundview4alipay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by dell on 2016/11/30.
 */

public class RoundView4AlipayView extends View {
    private  Context mContext;
    private int mMaxNum;
    private int mStartAngle;
    private int mSweepAngle;
    private int mSweepInWidth;
    private int mSweepOutwidth;
    private Paint paint_2;
    private Paint paint_3;
    private Paint paint_4;
    private Paint mPaint;
    private int wSize;
    private int hSize;
    private int wMode;
    private int hMode;
    private int mWidth;
    private int mHeight;
    private int radius;
    private String[] text ={"较差","中等","良好","优秀","极好"};

    public RoundView4AlipayView(Context context) {
        this(context,null);
    }

    public RoundView4AlipayView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

//1,构造函数初始化attrs和画笔paint
    public RoundView4AlipayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0xFFFF6347);
        this.mContext = context;
        initAttrs(attrs);
        initPaint();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RoundView4Alipay);
        mMaxNum = typedArray.getInt(R.styleable.RoundView4Alipay_maxNum, 500);
        mStartAngle = typedArray.getInt(R.styleable.RoundView4Alipay_startAngle, 160);
        mSweepAngle = typedArray.getInt(R.styleable.RoundView4Alipay_sweepangle, 220);

        //内外圆的宽度，
        mSweepInWidth = dp2px(8);
        mSweepOutwidth = dp2px(3);
        //释放资源
        typedArray.recycle();

    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xffffffff);
        paint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wSize = MeasureSpec.getSize(widthMeasureSpec);
        hSize = MeasureSpec.getSize(heightMeasureSpec);
        wMode = MeasureSpec.getMode(widthMeasureSpec);
        hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wSize == MeasureSpec.EXACTLY){
            mWidth = wSize;
        }else{
            mWidth = dp2px(400);
        }
         if (hSize == MeasureSpec.EXACTLY){
            mHeight = hSize;
        }else{
            mHeight = dp2px(300);
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化半径
        radius = getMeasuredWidth() / 4;
        Log.d("jiajia", "onDraw: "+getWidth()+"w===h"+getHeight()+"==="+radius);
        canvas.save();
        canvas.translate(getWidth()/2,getHeight()/2);//移动画布原点到中心位置。

        drawRound(canvas);//画内外圆
        drawScale(canvas);

        //回收资源
        canvas.restore();

    }

    private void drawScale(Canvas canvas) {
        //1.一共画30个刻度，计算出每个刻度的间隔。
        int angle = mSweepAngle/30;
        //2.旋转到起始点。
        canvas.rotate(-270+mStartAngle); //将起始刻度点旋转到正上方（270)
        //3、循环30次画上30个刻度。
        for (int i = 0; i <= 30; i++) {
            //1、每6个刻度就画一个粗刻度
            if(i%6 == 0){ //画粗刻度和刻度值
                mPaint.setStrokeWidth(dp2px(2));//设置线宽
                mPaint.setAlpha(0x70);
                //画线
                canvas.drawLine(0, -radius-mSweepOutwidth/2,0, -radius+mSweepInWidth/2+dp2px(1), mPaint);
                //画线下文字
                drawText(canvas,i*mMaxNum/30+"",mPaint);
            }else {         //2.画细刻度
                mPaint.setStrokeWidth(dp2px(1));
                mPaint.setAlpha(0x50);
                canvas.drawLine(0,-radius-mSweepOutwidth/2,0, -radius+mSweepInWidth/2, mPaint);
            }
            if(i==3 || i==9 || i==15 || i==21 || i==27){  //3.画刻度区间文字
                mPaint.setStrokeWidth(dp2px(2));
                mPaint.setAlpha(0x50);
                drawText(canvas,text[(i-3)/6], mPaint);
            }
            canvas.rotate(angle); //4.逆时针旋转一下。继续画。
        }
        //4.画完后旋转会原来的样子。
        canvas.restore();

    }

    /**
     * 画文字
     * @param canvas
     * @param text
     * @param paint
     */
    private void drawText(Canvas canvas, String text, Paint paint) {
//        画笔类型
        paint.setStyle(Paint.Style.FILL);
//        画笔大小
        paint.setTextSize(sp2px(8));

        float width = paint.measureText(text); //相比getTextBounds来说，这个方法获得的类型是float，更精确些
//        Rect rect = new Rect();
//        paint.getTextBounds(text,0,text.length(),rect);
        canvas.drawText(text,-width/2 , -radius + dp2px(15),paint);
        paint.setStyle(Paint.Style.STROKE);

    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        //内圆
        mPaint.setAlpha(0x40);
        mPaint.setStrokeWidth(mSweepInWidth);
        //RectF rectF3 = new RectF(-radius, -radius, radius, radius);
        //canvas.drawRect(rectF3,mPaint);
        RectF rectf = new RectF(-radius,-radius,radius,radius);
        canvas.drawArc(rectf,mStartAngle,mSweepAngle,false,mPaint);
        //外圆
        mPaint.setStrokeWidth(mSweepOutwidth);
        int w = dp2px(10);
        RectF rectf2 = new RectF(-radius-w , -radius-w , radius+w , radius+w);
        canvas.drawArc(rectf2,mStartAngle,mSweepAngle,false,mPaint);
        canvas.restore();
    }



    /**
     *
     * 将dp转成px的util方法
     * @param dp
     * @return
     */
    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, getResources().getDisplayMetrics());
    }
    public  int  sp2px(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }












































}
