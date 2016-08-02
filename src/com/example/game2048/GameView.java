package com.example.game2048;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
	private Card[][] cardsMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();
	
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		initGameView();
	}
	
	private void initGameView(){
		setColumnCount(4);
		setBackgroundColor(0xffbbada0);
		
		setOnTouchListener(new View.OnTouchListener() {
			private float startX,startY,offsetX,offsetY;
			
			public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				startX = event.getX();
				startY = event.getY();
				break;
				
			case MotionEvent.ACTION_UP:
				offsetX = event.getX()-startX;
				offsetY = event.getY()-startY;
				
				
				if (Math.abs(offsetX)>Math.abs(offsetY)) {
					if (offsetX<-5) {
						swipeLeft();
					}else if (offsetX>5) {
						swipeRight();
					}
				}else{
					if (offsetY<-5) {
						swipeUp();
					}else if (offsetY>5) {
						swipeDown();
					}
				}
				
				break;
			}
			return true;
		}
	});
}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		int cardWidth = (Math.min(w,h)-10)/4;
		addCards(cardWidth,cardWidth);
		startGame();
	}
	
	private void addCards(int cardWidth,int cardHeight) {
		Card card;
		for(int y = 0; y< 4; y++){
			for(int x = 0 ; x< 4;x++){
				card = new Card(getContext());
				card.setNum(0);
				addView(card, cardWidth, cardHeight);
				cardsMap[x][y]= card; 
			}
		}
	}
	
	private void startGame() {
		MainActivity.getMainActivity().clearScore();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0); 
			}
		}
		addRandomNumber();
		addRandomNumber();
		
	}
	
	
	private void addRandomNumber() {
		emptyPoints.clear();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if(cardsMap[x][y].getNum() <= 0){
					emptyPoints.add(new Point(x,y));
				}
			}
		}
		
		//随机去取出一个点；
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);

	}
	
	private void swipeLeft() {
		 boolean merge  = false;
		
		for (int y = 0; y < 4; y++) {
			
			for(int x = 0 ; x < 4; x++){
				
				for (int x1 = x+1; x1 < 4 ; x1++) {
					
					if (cardsMap[x1][y].getNum() > 0) {
						
						if (cardsMap[x][y].getNum() <=0) {
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x--;
							merge = true;
							break;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y]) ) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
							break;
						}
						
					}
				}
			}
		}
		if(merge){
			addRandomNumber();
			checkComplete();
		}

	}
	
	private void swipeRight() {
		 boolean merge  = false;
		 
		for (int y = 0 ; y < 4 ; y++) {
		
			for (int x = 3; x >= 0; x--) {

				for (int x1 = x - 1; x1 >= 0; x1--) {

					if (cardsMap[x1][y].getNum() > 0) {
						if (cardsMap[x][y].getNum() <= 0) {
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x++;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}
		if(merge){
			addRandomNumber();
			checkComplete();
		}
	}
	
	private void swipeUp() {
		boolean merge = false;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {

				for (int y1 = y + 1; y1 < 4; y1++) {
					if (cardsMap[x][y1].getNum() > 0) {

						if (cardsMap[x][y].getNum() <= 0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y--;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;

					}
				}
			}
		}
		if(merge){
			addRandomNumber();
			checkComplete();
		}
	}
	
	private void swipeDown() {
		boolean merge = false;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >=0; y--) {
				
				for (int y1 = y-1; y1 >=0; y1--) {
					if (cardsMap[x][y1].getNum()>0) {
						
						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							
							y++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						
						break;
					}
				}
			}
		}
		if(merge){
			addRandomNumber();
			checkComplete();
		}
	}
	
	private void checkComplete() {
		boolean complete = true;
		
		All:
		for (int y = 0; y <4 ; y++) {
			for (int x = 0; x < 4; x++) {
				if(cardsMap[x][y].getNum() == 0 ||
						(x > 0 &&cardsMap[x][y].equals(cardsMap[x-1][y]))||
						(x < 3 &&cardsMap[x][y].equals(cardsMap[x+1][y]))||
						(y > 0 &&cardsMap[x][y].equals(cardsMap[x][y-1]))|
						(y < 3 &&cardsMap[x][y].equals(cardsMap[x][y+1]))){
					complete = false;
					break All;
				}
			}
		}
		
		if(complete){
			new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重来",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startGame();
				}
			}).show();
		}

	}

	
}
	
