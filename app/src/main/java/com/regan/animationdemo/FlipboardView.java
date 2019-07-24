package com.regan.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class FlipboardView extends View implements Animator.AnimatorListener{
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    int degree;
    int rotateXY;
    int halfX;
    ObjectAnimator frist = ObjectAnimator.ofInt(this, "degreeY", 0, -45);
    final int fristCode = frist.hashCode();

//    PropertyValuesHolder propertX = PropertyValuesHolder.ofInt("degreeX", 0, 45);
//    PropertyValuesHolder propertY = PropertyValuesHolder.ofInt("degreeY", 45, 0);
//    ObjectAnimator secend = ObjectAnimator.ofPropertyValuesHolder(this, propertX, propertY);
    ObjectAnimator second = ObjectAnimator.ofInt(this, "rotateXY", 0, -270);
    ObjectAnimator third = ObjectAnimator.ofInt(this, "halfX", 0, 45);

    AnimatorSet animatorSet = new AnimatorSet();

    int state = 0;//0为初始到右边翘起，1为旋转到下面翘起，2为上方也翘起
    public FlipboardView(Context context) {
        super(context);
    }

    public FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        frist.setDuration(1000);
        frist.setInterpolator(new LinearInterpolator());
        frist.setRepeatCount(0);
        frist.addListener(this);
        Log.i("wdx", "fristCode : " + fristCode);

        second.setDuration(2000);
        second.setInterpolator(new LinearInterpolator());
        second.setRepeatCount(0);
        second.addListener(this);

        third.setDuration(1000);
        third.setInterpolator(new LinearInterpolator());
        third.setRepeatCount(0);
        third.addListener(this);

        animatorSet.playSequentially(frist, second, third);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animatorSet.end();
    }

    @SuppressWarnings("unused")
    public void setRotateXY(int rotateXY) {
        this.rotateXY = rotateXY;
//        Log.i("wdx", "rotateXY : " + rotateXY);
        invalidate();
    }
    @SuppressWarnings("unused")
    public int getRotateXY(){
        return this.rotateXY;
    }

    @SuppressWarnings("unused")
    public void setDegreeY(int degree) {
        this.degree = degree;
        invalidate();
    }
    @SuppressWarnings("unused")
    public int getDegreeY(){
        return this.degree;
    }

    @SuppressWarnings("unused")
    public void setHalfX(int halfX) {
        this.halfX = halfX;
        invalidate();
    }
    @SuppressWarnings("unused")
    public int getHalfX(){
        return this.halfX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;


        if(state == 0){
            canvas.save();
            canvas.clipRect(0, 0, centerX, getHeight());
            canvas.drawBitmap(bitmap, x, y, paint);
            canvas.restore();

            canvas.save();
            camera.save();
            canvas.clipRect(centerX, 0, getWidth(), getHeight());
            camera.rotateY(degree);
            canvas.translate(centerX, centerY);
            camera.applyToCanvas(canvas);
            canvas.translate(-centerX, -centerY);
            camera.restore();

            canvas.drawBitmap(bitmap, x, y, paint);
            canvas.restore();
        }else if(state == 1){
            canvas.save();
            camera.save();
            canvas.rotate(rotateXY, centerX, centerY);
            canvas.clipRect(centerX, 0, getWidth(), getHeight());
            camera.rotateY(degree);
            canvas.translate(centerX, centerY);
            camera.applyToCanvas(canvas);
            canvas.translate(-centerX, -centerY);
            camera.restore();
            canvas.rotate(-rotateXY, centerX, centerY);

            canvas.drawBitmap(bitmap, x, y, paint);
            canvas.restore();

            canvas.save();
            canvas.rotate(rotateXY, centerX, centerY);
            canvas.clipRect(0, 0, centerX, getHeight());
            canvas.rotate(-rotateXY, centerX, centerY);

            canvas.drawBitmap(bitmap, x, y, paint);
            canvas.restore();
        }else if(state == 2){
            canvas.save();
            camera.save();
            canvas.clipRect(0, 0, getWidth(), centerY);
            camera.rotateX(-halfX);
            canvas.translate(centerX, centerY);
            camera.applyToCanvas(canvas);
            canvas.translate(-centerX, -centerY);
            camera.restore();

            canvas.drawBitmap(bitmap, x, y, paint);
            canvas.restore();

            canvas.save();
            camera.save();
            canvas.clipRect(0, centerY, getWidth(), getHeight());
            camera.rotateX(-degree);
            canvas.translate(centerX, centerY);
            camera.applyToCanvas(canvas);
            canvas.translate(-centerX, -centerY);
            camera.restore();

            canvas.drawBitmap(bitmap, x, y, paint);
            canvas.restore();
        }



    }

    @Override
    public void onAnimationStart(Animator animation) {
//        Log.i("wdx", "code : " + animation.hashCode());
        if(frist.hashCode() == animation.hashCode()){
            state = 0;
        }
        if(second.hashCode() == animation.hashCode()){
            state = 1;
        }
        if(third.hashCode() == animation.hashCode()){
            state = 2;
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
