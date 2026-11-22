package com.rhay.penalty2027;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.*;
import java.util.Random;

public class MainActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().getDecorView().setSystemUiVisibility(5894|1024|512);setContentView(new Game());}
 class Game extends View {
  final Paint p=new Paint(3); final Random rand=new Random();
  final String[] teams={"CORINTHIANS","FLAMENGO","PALMEIRAS","SAO PAULO","SANTOS","VASCO","FLUMINENSE","BOTAFOGO","GREMIO","INTERNACIONAL","CRUZEIRO","ATLETICO-MG","BAHIA","VITORIA","FORTALEZA","SPORT","REAL MADRID","BARCELONA","ATLETICO MADRID","SEVILLA","VALENCIA","VILLARREAL","MAN CITY","MAN UNITED","LIVERPOOL","ARSENAL","CHELSEA","TOTTENHAM","NEWCASTLE","ASTON VILLA","BAYERN","DORTMUND","LEVERKUSEN","RB LEIPZIG","PSG","MARSEILLE","LYON","MONACO","JUVENTUS","INTER","MILAN","NAPOLI","ROMA","LAZIO","ATALANTA","BENFICA","PORTO","SPORTING","AJAX","PSV","FEYENOORD","CELTIC","RANGERS","BOCA JUNIORS","RIVER PLATE","RACING","INDEPENDIENTE","PEÑAROL","NACIONAL","AL HILAL","AL NASSR","AL ITTIHAD","INTER MIAMI","LA GALAXY"};
  int club=0,round=0,attempt=0,goals=0,opGoals=0,aim=1,power=2,keeper=1,screen=0,tournament=16,opponent=1; boolean playerFirst=true;
  String msg="ESCOLHA SEU CLUBE",playerName="JOGADOR",editName="JOGADOR"; int shotX=500,shotY=360; boolean showBall=false;
  Game(){super(MainActivity.this);}
  void fill(Canvas c,int color,float x,float y,float w,float h){p.setColor(color);p.setStyle(Paint.Style.FILL);c.drawRect(x,y,x+w,y+h,p);}
  void circle(Canvas c,int color,float x,float y,float r){p.setColor(color);p.setStyle(Paint.Style.FILL);c.drawCircle(x,y,r,p);}
  void line(Canvas c,int color,float x,float y,float xx,float yy,int sw){p.setColor(color);p.setStrokeWidth(sw);c.drawLine(x,y,xx,yy,p);}
  void text(Canvas c,String s,float x,float y,int sz,int color){p.setColor(color);p.setTypeface(Typeface.create("sans-serif-condensed",Typeface.BOLD));p.setTextSize(sz);c.drawText(s,x,y,p);}
  void button(Canvas c,int x,int y,int w,int h,String s){p.setColor(0xffffcf42);c.drawRoundRect(new RectF(x,y,x+w,y+h),12,12,p);p.setColor(0xff162a32);p.setTextSize(23);p.setTypeface(Typeface.DEFAULT_BOLD);c.drawText(s,x+(w-p.measureText(s))/2f,y+h/2f+8,p);}
  int teamColor(int i){int[] colors={0xffeeeeee,0xffd41d27,0xff12613a,0xffe9e9e9,0xffe9e9e9,0xff151515,0xff901b2c,0xff222222,0xff53a9de,0xffc82b31,0xff2871e4,0xff202020,0xff248bd3,0xffce242c,0xff245ac4,0xffda2429,0xffeeeeee,0xffc72b42,0xffd32730,0xffe9e9e9,0xffeeeeee,0xffffd130,0xff91c9e8,0xffe52b2b,0xffdb252c,0xffe93a35,0xffd9232e,0xffeeeeee,0xff202020,0xff8b1e3f,0xffdf2525,0xffffd122,0xffdc2525,0xffeeeeee,0xff2738ac,0xffeaeaea,0xffeeeeee,0xffe52c30,0xffeeeeee,0xff2864bc,0xffe22c31,0xff60a5df,0xffa3222c,0xff8fc2df};return colors[i%colors.length];}
  void person(Canvas c,float x,float y,int color,boolean keeperPlayer){
    circle(c,0xffd3a077,x,y-53,13);fill(c,color,x-15,y-39,30,38);line(c,0xffd3a077,x-14,y-32,x-27,y-5,9);line(c,0xffd3a077,x+14,y-32,x+27,y-5,9);line(c,0xff222831,x-8,y,x-13,y+36,10);line(c,0xff222831,x+8,y,x+13,y+36,10);
    text(c,keeperPlayer?"1":"9",x-7,y-11,17,0xff111111);
  }
  void stadium(Canvas c){
    fill(c,0xff081823,0,0,1000,560);fill(c,0xff142a3a,0,0,1000,155);
    for(int i=0;i<150;i++)circle(c,i%4==0?0xffffd75a:i%3==0?0xffb8c4cb:0xff447c94,(i*97)%1000,55+(i*43)%95,2.5f);
    for(int i=0;i<10;i++)fill(c,i%2==0?0xff166b3a:0xff1c7741,0,160+i*45,1000,45);
    line(c,0xffc9e3d2,150,415,850,415,2);line(c,0xffc9e3d2,250,155,250,415,2);line(c,0xffc9e3d2,750,155,750,415,2);
    fill(c,0xfff0f0f0,288,177,424,8);fill(c,0xffeeeeee,288,177,8,150);fill(c,0xffeeeeee,704,177,8,150);
    for(int i=0;i<8;i++)line(c,0x668fffff,295+i*58,185,295+i*58,325,1);
    for(int j=0;j<6;j++)line(c,0x668fffff,295,185+j*28,705,185+j*28,1);
  }
  void scoreboard(Canvas c){
    fill(c,0xcc07121c,0,0,1000,74);
    text(c,teams[club],22,31,24,0xffffffff);text(c,goals+" - "+opGoals,450,40,34,0xffffd044);text(c,teams[opponent],700,31,24,0xffffffff);
    text(c,"FASE "+(round+1)+" / "+(tournament==8?3:tournament==16?4:5)+"     COBRANCA "+(attempt+1),23,62,18,0xffcbdde0);
  }
  @Override protected void onDraw(Canvas canvas){super.onDraw(canvas);Canvas c=canvas;c.save();c.scale(getWidth()/1000f,getHeight()/560f);stadium(c);
    if(screen==0){
      fill(c,0xe5112430,155,86,690,385);text(c,"PENALTY 2027",348,130,43,0xffffd044);text(c,"ESCOLHA SEU CLUBE  ("+(club+1)+"/"+teams.length+")",285,178,26,0xffffffff);
      text(c,teams[club],305,250,39,teamColor(club)==0xffeeeeee?0xffffffff:teamColor(club));
      button(c,190,288,240,70,"< ANTERIOR");button(c,570,288,240,70,"PROXIMO >");
      text(c,"TORNEIO: "+tournament+" CLUBES",355,399,24,0xffffffff);
      button(c,180,430,205,65,"8 TIMES");button(c,398,430,205,65,"16 TIMES");button(c,615,430,205,65,"32 TIMES");
      button(c,345,500,310,52,"JOGAR");
    }else{
      scoreboard(c);person(c,500+(keeper-1)*95,281,0xffe6d039,true);person(c,500,454,teamColor(club),false);circle(c,0xffffffff,500,427,10);
      if(showBall){circle(c,0xffffffff,shotX,shotY,10);circle(c,0xff242424,shotX,shotY,3);}
      fill(c,0xaa091e24,0,463,1000,97);
      text(c,msg,320,447,24,0xffffe063);
      if(screen==1){button(c,12,478,215,70,"ESQUERDA");button(c,238,478,170,70,"CENTRO");button(c,419,478,210,70,"DIREITA");button(c,640,478,160,70,"FORCA "+power);button(c,810,478,178,70,"CHUTAR");}
      else {button(c,335,478,330,70,screen==3?"NOVO TORNEIO":"CONTINUAR");}
    }
    c.restore();
  }
  void nextOpponent(){opponent=rand.nextInt(teams.length-1);if(opponent>=club)opponent++;}
  void shoot(){
    keeper=rand.nextInt(3);boolean score=aim!=keeper||rand.nextInt(4)==0;
    shotX=500+(aim-1)*125;shotY=220+rand.nextInt(70);showBall=true;
    if(score)goals++;boolean rival=rand.nextInt(100)<65;if(rival)opGoals++;
    attempt++;msg=(score?"GOOOL!":"DEFENDEU!")+"  ADVERSARIO: "+(rival?"GOL":"ERROU");
    if(attempt>=5 && goals==opGoals){msg="EMPATE! PENALTIS ALTERNADOS";attempt=4;}
    else if((attempt>=5 || goals>opGoals+(5-attempt) || opGoals>goals+(5-attempt)) && goals!=opGoals){
      if(goals>opGoals){if(round+1>=(tournament==8?3:tournament==16?4:5)){screen=3;msg="CAMPEAO DO TORNEIO!";}else{screen=2;msg="CLASSIFICADO PARA PROXIMA FASE!";}}
      else{screen=3;msg="ELIMINADO! TENTE NOVAMENTE";}
    }
  }
  @Override public boolean onTouchEvent(MotionEvent e){if(e.getAction()!=MotionEvent.ACTION_UP)return true;float x=e.getX()*1000/getWidth(),y=e.getY()*560/getHeight();
    if(screen==0){if(y>288&&y<370){if(x<450)club=(club+teams.length-1)%teams.length;else club=(club+1)%teams.length;}
      else if(y>425&&y<500){tournament=x<390?8:x<610?16:32;}
      else if(y>=500){round=0;attempt=0;goals=0;opGoals=0;nextOpponent();screen=1;msg="ESCOLHA A DIRECAO E CHUTE";showBall=false;}}
    else if(screen==1&&y>=478){if(x<230)aim=0;else if(x<415)aim=1;else if(x<635)aim=2;else if(x<805)power=power%3+1;else shoot();}
    else if(screen==2&&y>=478){round++;attempt=0;goals=0;opGoals=0;screen=1;nextOpponent();showBall=false;msg="NOVA FASE!";}
    else if(screen==3&&y>=478){screen=0;msg="ESCOLHA SEU CLUBE";showBall=false;}
    invalidate();return true;}
 }
}