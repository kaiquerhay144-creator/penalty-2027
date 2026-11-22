package com.rhay.penalty2027;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.RectF;
import java.util.Random;

public class MainActivity extends Activity {
  @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().getDecorView().setSystemUiVisibility(5894|1024|512);setContentView(new Game());}
  class Game extends View {
    Paint p=new Paint(3); Random rng=new Random();
    String[] clubs={"CORINTHIANS","REAL MADRID","BARCELONA","FLAMENGO","BAYERN","LIVERPOOL","PSG","MAN. CITY"};
    int club=0,round=0,shot=0,ours=0,theirs=0,aim=1,power=2,phase=0,keeper=1; boolean scored=false; String msg="ESCOLHA SEU TIME"; int[] wins=new int[3];
    Game(){super(MainActivity.this);}
    void rect(Canvas c,int color,float a,float b,float d,float e){p.setColor(color);c.drawRect(a,b,d,e,p);}
    void txt(Canvas c,String s,float x,float y,int size,int color){p.setColor(color);p.setTextSize(size);p.setTypeface(android.graphics.Typeface.create("sans-serif-condensed",1));c.drawText(s,x,y,p);}
    @Override protected void onDraw(Canvas raw){
      super.onDraw(raw);Canvas c=raw;float w=getWidth(),h=getHeight();c.save();c.scale(w/1000f,h/560f);
      rect(c,Color.rgb(7,32,24),0,0,1000,560);
      for(int i=0;i<9;i++)rect(c,i%2==0?0xff145e38:0xff176940,0,170+i*46,1000,170+(i+1)*46);
      rect(c,0xff12222c,0,0,1000,172);
      for(int i=0;i<85;i++){p.setColor(i%3==0?0xffe4e9f0:0xff3c7b98);c.drawCircle((i*73)%1000,40+(i*31)%105,2.4f,p);}
      txt(c,"PENALTY 2027",30,44,35,Color.WHITE);
      if(phase==0){
        txt(c,"SELECIONE O CLUBE",310,115,35,Color.YELLOW);
        txt(c,clubs[club],320,245,48,Color.WHITE);
        button(c,110,305,310,390,"< ANTERIOR");button(c,690,305,890,390,"PROXIMO >");
        button(c,320,435,680,530,"INICIAR TORNEIO");
      } else {
        txt(c,clubs[club]+"  "+ours+"  X  "+theirs+"  "+clubs[(club+round+1)%clubs.length],35,95,29,Color.WHITE);
        txt(c,"FASE "+(round+1)+"/3  |  COBRANCA "+Math.min(5,shot+1)+"/5",35,135,24,Color.YELLOW);
        rect(c,Color.WHITE,300,170,700,181);rect(c,Color.WHITE,300,170,311,310);rect(c,Color.WHITE,689,170,700,310);
        for(int i=0;i<5;i++)rect(c,0xffe6e6e6,305+i*78,178,308+i*78,307);
        p.setColor(0xffffdf00);c.drawCircle(500,377,15,p);
        rect(c,0xff121a20,480+(keeper-1)*115,250,520+(keeper-1)*115,306);
        txt(c,"GOLEIRO",461+(keeper-1)*115,238,17,Color.WHITE);
        txt(c,msg,340,412,26,Color.WHITE);
        if(phase==1){
          button(c,65,448,255,530,"ESQUERDA");button(c,275,448,465,530,"CENTRO");button(c,485,448,675,530,"DIREITA");
          button(c,695,448,935,530,"CHUTAR");
          txt(c,"DIRECAO: "+(aim==0?"ESQUERDA":aim==1?"CENTRO":"DIREITA"),30,424,20,Color.YELLOW);
        }else if(phase==2){button(c,320,448,680,530,round==2?"NOVO TORNEIO":"PROXIMA FASE");}
      }
      c.restore();
    }
    void button(Canvas c,float l,float t,float r,float b,String s){
      p.setColor(0xffe8bc2b);c.drawRoundRect(new RectF(l,t,r,b),13,13,p);
      p.setColor(0xff13272b);p.setTextSize(23);p.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
      c.drawText(s,(l+r-p.measureText(s))/2,(t+b)/2+8,p);
    }
    void shoot(){
      keeper=rng.nextInt(3);boolean goal=aim!=keeper || rng.nextInt(4)==0;
      if(goal)ours++;
      boolean rival=rng.nextInt(100)<65;if(rival)theirs++;
      shot++;msg=goal?"GOL!":"DEFESA DO GOLEIRO!";
      if(shot>=5 || (ours>theirs+(5-shot)) || (theirs>ours+(5-shot))){
        if(ours==theirs && shot>=5){msg="EMPATE! COBRANCAS EXTRAS";shot=4;}
        else if(ours>theirs){phase=2;msg=round==2?"VOCE E O CAMPEAO!":"CLASSIFICADO!";}
        else {phase=2;msg="ELIMINADO! TENTE NOVAMENTE";round=2;}
      }
    }
    @Override public boolean onTouchEvent(MotionEvent e){
      if(e.getAction()!=MotionEvent.ACTION_UP)return true;
      float x=e.getX()*1000/getWidth(),y=e.getY()*560/getHeight();
      if(phase==0){
        if(y>305&&y<400){if(x<320)club=(club+7)%8;else if(x>680)club=(club+1)%8;}
        if(y>430){phase=1;round=0;shot=0;ours=0;theirs=0;msg="ESCOLHA A DIRECAO E CHUTE";}
      }else if(phase==1 && y>448){
        if(x<680)aim=x<265?0:x<475?1:2;else shoot();
      }else if(phase==2&&y>440){
        if(round>=2){round=0;phase=0;msg="ESCOLHA SEU TIME";}
        else {round++;phase=1;shot=0;ours=0;theirs=0;msg="NOVA FASE!";}
      }
      invalidate();return true;
    }
  }
}