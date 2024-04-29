package com.example.game1;
//Не смотрите пожалуйста я ещё не дорисовала все спрайты и кнопки, там вместо многих спрайтов мои старые иконки и рисунки с других проектов вы ничего не поймёте всё рано
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;
import java.util.Random;

import androidx.annotation.NonNull;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap b1, b2, b3, b4, happy, eat, done, normal, sad, r, confused, sleep, walk, m, water,back11,trees;//спрайт
    SurfaceHolder holder;
    SurfaceThread1 thread;//поток
    Bitmap back;//фон
    Paint paint;
    Matrix matrix;

    int j = 0;//короче это отвечает за кнопки
    int sost = 0;// состояние спрайта
    int live = 1000;//жизни
    int mm = 0;
    int drawzn = 0;
    float coordx, coordy;
    Bitmap images;
    Sprite sprite;
    float curX, curY,curXt, curYt; //текущие координаты картинки
    float touchX, touchY; //координаты точки касания
    float dx, dy; // смещения по осям

SpriteEnemy spriteEnemy,spriteEnemy1,spriteEnemy3,spriteEnemy2,spriteEnemy4,spriteEnemy5;


    public GameSurface(Context context) {
        super(context);
        confused = BitmapFactory.decodeResource(getResources(), R.drawable.confused);
        sleep = BitmapFactory.decodeResource(getResources(), R.drawable.sleep);
        walk = BitmapFactory.decodeResource(getResources(), R.drawable.walk);
        happy = BitmapFactory.decodeResource(getResources(), R.drawable.happy);
        normal = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        sad = BitmapFactory.decodeResource(getResources(), R.drawable.sad);
        done = BitmapFactory.decodeResource(getResources(), R.drawable.done);
        eat = BitmapFactory.decodeResource(getResources(), R.drawable.eat);
        back = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        b1 = BitmapFactory.decodeResource(getResources(), R.drawable.b1);
        b2 = BitmapFactory.decodeResource(getResources(), R.drawable.b2);
        b4 = BitmapFactory.decodeResource(getResources(), R.drawable.b4);
        r = BitmapFactory.decodeResource(getResources(), R.drawable.r);
        holder = getHolder();
        b3 = BitmapFactory.decodeResource(getResources(), R.drawable.b3);
        holder.addCallback(this);
        water = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        back11 = BitmapFactory.decodeResource(getResources(), R.drawable.back1);
        images = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);
        int x = back.getWidth(); int y=back.getHeight();
        curX=x/3;
        curY=(float)(y/1.5);
        curXt=x-100;
        curY=y-500;
        spriteEnemy =new SpriteEnemy(images,back.getWidth(),0);//верх   (чётные сверху, нечётные снизу)
        spriteEnemy1 =new SpriteEnemy(images,back.getWidth(),back.getHeight()-back.getHeight()*1/4);//низ

        spriteEnemy2 =new SpriteEnemy(images,back.getWidth()-back.getWidth()/2,0);//верх
        spriteEnemy3 =new SpriteEnemy(images,back.getWidth()-back.getWidth()/2,back.getHeight()-back.getHeight()*1/4);//низ

        Random random = new Random();
        sprite=new Sprite(images, curX, curY);

    }


    public void setWater(Bitmap water) {
        this.water = water;
    }

    //draw
    @Override
    public void draw(Canvas canvas) {
        //обьявление перемнных:
        super.draw(canvas);
        matrix = new Matrix();
        matrix.reset();
        float x = canvas.getWidth();
        float y = canvas.getHeight();
        float yb = back.getHeight();
        float xb = back.getWidth();
        float kx = x / xb;
        float ky = y / yb;// коэфиценты на которые будет растянута картинка в зависимости от экрана
        matrix.setScale(kx, ky, 0, 0);
        canvas.drawBitmap(back, matrix, paint);//рисуем фон
        matrix.reset();//матрицу резерт
        matrix.setScale(0.4F, 0.4F, 500, 1900);
        canvas.drawBitmap(r, matrix, paint);
        canvas.drawBitmap(b1, 25, 900, paint);//кнопка поесть
        canvas.drawBitmap(b2, 230, 900, paint);//кнопка помыть}
        canvas.drawBitmap(b3, 440, 900, paint);
        canvas.drawBitmap(b4, 670, 900, paint);
        canvas.drawBitmap(normal, 200, 400, paint);
        //изменение состояний спрайта
        if (sost == 0) {
            canvas.drawBitmap(happy, 200, 400, paint);
        }
        if (sost == 1) {
            canvas.drawBitmap(normal, 200, 400, paint);
        }
        if (sost == 2) {
            canvas.drawBitmap(sad, 200, 400, paint);
        }
        if (sost == 3) {
            canvas.drawBitmap(confused, 200, 400, paint);
        }
        if (sost == 4) {
            canvas.drawBitmap(sleep, 200, 400, paint);//вход сон и выключение кнопок
            try {
                Thread.sleep(3000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sost = 0;
        }
        if (sost == 5) {
            canvas.drawBitmap(walk, 200, 400, paint);
        }
        if (sost == 6) {
            canvas.drawBitmap(done, 200, 360, paint);
        }
        if (sost == 7) {
            canvas.drawBitmap(eat, 200, 400, paint);

        }
        //dearth
        if (live <=0) {//смерть спрайта, перерисовка интерфейса без кнопок с возбожностью только сбросить игру
            //игрок просто останется без каких-либо действий кроме как резертнуть игру
            //можно убрать эту кнопку, и при сметре спрайта нельзя будет резертнуть игру, а только переустнановить
            super.draw(canvas);
            paint.setStyle(Paint.Style.FILL);
            matrix.setScale(kx, ky, 0, 0);
            canvas.drawBitmap(back, matrix, paint);//рисуем фон
            matrix.reset();//матрицу резерт
            canvas.drawBitmap(r, matrix, paint);}




        //действия  при нажати кнопок
        if (j == 1) {// j - это сигнал для того чтобы спрайт поел
            canvas.drawBitmap(b1, 190, 560, paint);//отрисовка еды
            class DelayThread extends Thread {//режим ожидания для изменения спрайта

                public void run() {
                    sost = 7;
                    try {
                        Thread.sleep(2000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sost = 6;
                    j = 0;
                    try {
                        Thread.sleep(2000);
                        canvas.drawBitmap(b1, 25, 900, paint);//кнопка поесть
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sost = 0;}}
            DelayThread delayThread = new DelayThread();
            delayThread.start();
        }// спрайт ест

        if (mm >= 1 && mm < 100) {//мытьё
            Random rand = new Random();
            canvas.drawBitmap(water, rand.nextInt(canvas.getWidth()-500), rand.nextInt(canvas.getHeight()-800), paint);//
            canvas.drawBitmap(water, rand.nextInt(canvas.getWidth()-500), rand.nextInt(canvas.getHeight()-800), paint);//
            canvas.drawBitmap(water, rand.nextInt(canvas.getWidth()-500), rand.nextInt(canvas.getHeight()-800), paint);//
            sost=1;
            mm = mm + 1;

        }


if (j==7){///////////////////////// игра
    Paint paint = new Paint();
// Выбираем кисть
    paint.setStyle(Paint.Style.FILL);
// Белый цвет кисти
    paint.setColor(Color.GREEN);
// Закрашиваем холст
    canvas.drawPaint(paint);
    int by=b1.getHeight();
    int bx = b2.getWidth();
    matrix.setScale(kx,ky,0,0);
    matrix.setTranslate(bx/2, y-(by+by/2));
    canvas.drawBitmap(b4, matrix, paint);
    sprite.draw(canvas);
    matrix.reset();//матрицу резерт
    matrix.setScale(0.4F, 0.4F, 500, 1900);
    canvas.drawBitmap(r, matrix, paint);
    matrix.reset();//матрицу резерт
    matrix.setScale(kx,ky,0,0);
    matrix.setTranslate(x-bx-bx/2, y-(by+by/2));
    canvas.drawBitmap(b4, matrix, paint);

    spriteEnemy.draw(canvas);
    spriteEnemy1.draw(canvas);
    spriteEnemy2.draw(canvas);
    spriteEnemy3.draw(canvas);
    spriteEnemy1.setTx(0);
    spriteEnemy.setTx(0);
    spriteEnemy2.setTx(0);
    spriteEnemy3.setTx(0);

    if (spriteEnemy.getX() < 0){
        spriteEnemy=new SpriteEnemy(images, back.getWidth(), 0);
        spriteEnemy.draw(canvas);
    spriteEnemy.setTx(0);}
    if (spriteEnemy1.getX() < 0){
        spriteEnemy1=new SpriteEnemy(images, back.getWidth(), back.getHeight()-back.getHeight()*1/4);
        spriteEnemy1.draw(canvas);
        spriteEnemy1.setTx(0);}
    if (spriteEnemy2.getX() < 0){
        spriteEnemy2=new SpriteEnemy(images, back.getWidth(), 0);
        spriteEnemy2.draw(canvas);
        spriteEnemy2.setTx(0);}
    if (spriteEnemy3.getX() < 0){
        spriteEnemy3=new SpriteEnemy(images, back.getWidth(), back.getHeight()-back.getHeight()*1/4);
        spriteEnemy3.draw(canvas);
        spriteEnemy3.setTx(0);}


}


        if (j==3){//пока это просто перерисовка интерфейса, потом добавлю мини игру
            super.draw(canvas);
            matrix = new Matrix();
            matrix.reset();
            x = canvas.getWidth();
             y = canvas.getHeight();
             yb = back.getHeight();
            xb = back.getWidth();
             kx = x / xb;
             ky = y / yb;// коэфиценты на которые будет растянута картинка в зависимости от экрана
            matrix.setScale(kx, ky, 0, 0);
            canvas.drawBitmap(back, matrix, paint);//рисуем фон
            matrix.reset();//матрицу резерт
            matrix.setScale(0.4F, 0.4F, 500, 1900);
            canvas.drawBitmap(r, matrix, paint);
            canvas.drawBitmap(b1, 25, 900, paint);//кнопка поесть
            canvas.drawBitmap(b2, 230, 900, paint);//кнопка помыть}
            canvas.drawBitmap(b3, 440, 900, paint);
            canvas.drawBitmap(b4, 670, 900, paint);
            canvas.drawBitmap(happy, 200, 400, paint);
            sost=1;
        }
        if (drawzn==1){
            matrix.setScale(0.1F, 0.1F, coordx, coordy+100);
            canvas.drawBitmap(water, matrix, paint);
        }

    }



    @Override
    public boolean onTouchEvent(MotionEvent event)//кнопки
    {
        drawzn=1;
        coordx = event.getX();
        coordy = event.getY();
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float xe = b1.getWidth();
                float ye = b1.getHeight();
                float ys = happy.getHeight();
                float xs = happy.getWidth();
                float backX= back.getWidth();
                float backY = back.getHeight();

                if (x > 200 && x < 200 + xs && y > 400 && y < 400 + ys) {//на спрайт можно тыкать и он будет изменять состояние

                    class DelayThread extends Thread {

                        public void run() {
                            sost = 7;
                            try {
                                Thread.sleep(20000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sost = 0;}
                    }  DelayThread delayThread = new DelayThread();
                    delayThread.start();
                }


                    if (x > 25 && x < 25 + xe && y > 900 && y < 900 + ye) {
                        j = 1;
                    }
                    if (x > 230 && x < 230 + xe && y > 900 && y < 900 + ye) {
                        mm=1;
                        sost=0;
                    }
                    if (x > 440 && x < 440 + xe && y > 900 && y < 900 + ye) {
                        j=3;
                    }
                    if (x > 670 && x < 670 + xe && y > 900 && y < 900 + ye) {
                        j=7;

                    }
                    if (x>backX*2/6&&x<backX*4/6&&y>backY*9/10){
                        j=3;//резерт
                    }
                    int pby=b4.getHeight();
                    int pbx=b4.getWidth();
                if (x>pbx/2 && x<0+pby+pbx/2&& y>backY-pby ){
                    //////////
                    sprite.setTy(0);

                  }
                if (x>backX-pbx/2-pbx && x<backY-pbx/2&& y>backY-pby ){
                    //////////
                    sprite.setTy(backY);
                }
                    return true;
        }
        return false;
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread = new SurfaceThread1(holder, this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}