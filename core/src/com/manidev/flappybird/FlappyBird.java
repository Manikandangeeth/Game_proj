
package com.manidev.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


 public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture toptube;
	Texture bottomtube;
	Texture gameover;
	BitmapFont font;
	//ShapeRenderer shapeRenderer;

	Circle birdsCircle;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	int gamestate = 0;
	float gravity = 2;
	int gap = 400;
	int score = 0;
	int scoringtube = 0;
	float maxTubeOffSet;
	float tubeVelocity = 4;
	Random randomGenerator;
	int numberOfTubes = 4;
	float[] tubeOffset = new float[numberOfTubes];
	float[] tubex = new float[numberOfTubes];
	float distanceBtTubes;
	Rectangle[] toptubeRectangle;
	Rectangle[] bottomtubeRectangle;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		gameover = new Texture("gameoveranimation.gif");
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);


		maxTubeOffSet = Gdx.graphics.getHeight() / 2 - gap / 2 -100;
		randomGenerator = new Random();
		distanceBtTubes = Gdx.graphics.getWidth() * 3/4;
		birdsCircle = new Circle();
        //shapeRenderer = new ShapeRenderer();
		toptubeRectangle = new Rectangle[numberOfTubes];
		bottomtubeRectangle = new Rectangle[numberOfTubes];

		startGame();
	}

	public void startGame(){
        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

        for(int i = 0; i < numberOfTubes; i++){
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubex[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBtTubes;
            toptubeRectangle[i] = new Rectangle();
            bottomtubeRectangle[i] = new Rectangle();
        }
    }

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gamestate == 1) {
			if(tubex[scoringtube] < Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("score", String.valueOf(score));
				if(scoringtube < numberOfTubes - 1) {
					scoringtube++;
				}else{
					scoringtube = 0;
				}
			}

			if(Gdx.input.isTouched()){
				velocity = -30;
			}

			for (int i = 0; i < numberOfTubes; i++) {
				if(tubex[i] < - toptube.getWidth()){
					tubex[i] += numberOfTubes * distanceBtTubes;
				}else{
					tubex[i] = tubex[i] - tubeVelocity;

				}

				batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);

				toptubeRectangle[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], toptube.getWidth(),toptube.getHeight());
				bottomtubeRectangle[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(),bottomtube.getHeight());
			}

			if(birdY > 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}else{
			    gamestate = 2;
            }

		}else if(gamestate == 0){

			if(Gdx.input.isTouched()){
				gamestate = 1;
			}
		}else if(gamestate == 2){
            batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2,gameover.getWidth(),gameover.getHeight());
            try {
                gameover.wait(10l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Gdx.input.isTouched()){
                gamestate = 1;
                startGame();
                score = 0;
                velocity =0;
                scoringtube = 0;
            }
        }

		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}
		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdY);

		font.draw(batch,String.valueOf(score),100,200);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		birdsCircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);
		//shapeRenderer.circle(birdsCircle.x, birdsCircle.y, birdsCircle.radius);

		for(int i = 0; i < numberOfTubes; i++){
			//shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(),bottomtube.getHeight());

			if(Intersector.overlaps(birdsCircle, toptubeRectangle[i]) || Intersector.overlaps(birdsCircle, bottomtubeRectangle[i])){
				gamestate = 2;

			}
		}
		batch.end();
		//shapeRenderer.end();
	}

}
