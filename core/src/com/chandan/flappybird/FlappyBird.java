package com.chandan.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture toptube , bottomtube;
	private int birdstate = 0 , flappingtime = 0 , gamestate = 0 , velocity = 0 , tubecreattime = 0 , score = 0;
	private float birdx , birdy , gravity = 1 , gap = 800 , startgap = 900;
	ArrayList<Integer> toptubex , toptubey;
	ArrayList<Rectangle> toptuberect , bottomtuberect ;
	Rectangle birdrect;
	Random random = new Random();
	private BitmapFont scoreText , gameovertext;

	@Override
	public void create () {
		batch = new SpriteBatch();
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		background = new Texture("bg.png");
		toptubex = new ArrayList<>();
		toptubey = new ArrayList<>();
		toptuberect = new ArrayList<>();
		bottomtuberect = new ArrayList<>();
		birdrect = new Rectangle();
		scoreText = new BitmapFont();
		gameovertext = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(10);
		gameovertext.setColor(Color.WHITE);
		gameovertext.getData().setScale(10);
		birdx = (float) (Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2);
	}
	public void tubecreate(){
		startgap = random.nextInt(900)+400;
		toptubex.add(Gdx.graphics.getWidth());
		toptubey.add((int) startgap);
	}

	@Override
	public void render () {
		batch.begin();
		toptuberect.clear();
		bottomtuberect.clear();
		batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(flappingtime < 8){
			flappingtime++;
			birdstate = 1;
		}else {
			birdstate = 0;
			flappingtime = 0;
		}

		if(gamestate == 0){//game is started
			birdy = (float) (Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2);
			if(Gdx.input.justTouched()){
				gamestate = 1;
			}
		}else if(gamestate == 1){//game is playing
			if(tubecreattime < 200){
				tubecreattime++;
			}else{
				tubecreate();
				tubecreattime = 0;
			}
			for (int i = 0 ; i < toptubex.size() ; i++){
				batch.draw(toptube, toptubex.get(i), toptubey.get(i) + gap);
				batch.draw(bottomtube, toptubex.get(i),  toptubey.get(i) - bottomtube.getHeight());
				toptubex.set(i,toptubex.get(i) - 6);
				toptuberect.add(new Rectangle(toptubex.get(i), toptubey.get(i) + gap, toptube.getWidth(), toptube.getHeight()));
				bottomtuberect.add(new Rectangle(toptubex.get(i), toptubey.get(i) - bottomtube.getHeight(), bottomtube.getWidth(), bottomtube.getHeight()));
				if(toptubex.get(i) == Gdx.graphics.getWidth() / 4)
					score++;
			}
			velocity += gravity;
			birdy -= velocity;
			if(Gdx.input.justTouched()){
				velocity = -20;
			}
			if(birdy <= 0)
				birdy = 0;
			else if( birdy >= Gdx.graphics.getHeight())
				birdy = Gdx.graphics.getHeight() - birds[0].getHeight();

			for (int i = 0 ; i < toptubex.size() ; i++){
				if(Intersector.overlaps(birdrect, toptuberect.get(i)) || Intersector.overlaps(birdrect, bottomtuberect.get(i))){
					gamestate = 2;
					score = 0;
				}
			}

		}else if(gamestate == 2){
			gameovertext.draw(batch, "Game Over", Gdx.graphics.getHeight()/9, Gdx.graphics.getHeight()/2);
			if(Gdx.input.justTouched()){
				toptuberect.clear();
				bottomtuberect.clear();
				toptubex.clear();
				toptubey.clear();
				gamestate = 1;
			}
		}
		scoreText.draw(batch, String.valueOf(score), 50, 120);
		batch.draw(birds[birdstate],birdx,birdy);
		birdrect.set(birdx, birdy, birds[0].getWidth(),birds[0].getHeight());

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		birds[0].dispose();
		birds[1].dispose();
		toptube.dispose();
		bottomtube.dispose();
	}
}
