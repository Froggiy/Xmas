package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public final static int SCR_WIDTH = 1600;
    public final static int SCR_HEIGHT = 900;
    private SpriteBatch batch;
    private Texture imgBlFlake;
    private Texture imgPrFlake;
    private Texture imgBackground;
    public OrthographicCamera camera;
    private Vector3 touch;
    private Music mscInvierno;
    private Music mscGyat;
    private Flake[] flakes = new Flake[200];
    private int flakesCounter;
    private BitmapFont font;
    private String gameState;
    private long timeStartGame;
    private long currentTime;

    @Override
    public void create() {
        batch = new SpriteBatch();
        imgBlFlake = new Texture("flake.png");
        imgPrFlake = new Texture("flake_evil.png");
        imgBackground = new Texture("back.jpg");
        mscInvierno = Gdx.audio.newMusic(Gdx.files.internal("invierno.wav"));
        mscGyat = Gdx.audio.newMusic(Gdx.files.internal("label.mp3"));
        for (int i = 0; i < flakes.length; i++) {
            flakes[i] = new Flake(imgBlFlake, MathUtils.random(0f, SCR_WIDTH), MathUtils.random(0f, SCR_HEIGHT+100));
        }
        camera = new OrthographicCamera();
        camera.setToOrtho(false,SCR_WIDTH,SCR_HEIGHT);
        font = new BitmapFont(Gdx.files.internal("borel.fnt"));
        touch = new Vector3();
        timeStartGame = TimeUtils.millis();
    }

    @Override
    public void render() {

        //events
        touches();
        for (Flake f: flakes) {
            if (!f.isHit) {
                f.fly();
            }
            if (flakesCounter == 300){
                gameState = "gameOver";
            }
        }
        if (flakesCounter < 100) mscInvierno.play();

        //draw
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(imgBackground, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        if (!(gameState == "gameOver")) {
            currentTime = TimeUtils.millis() - timeStartGame;
            for (Flake f : flakes) {
                batch.draw(f.image, f.x, f.y, 50, 50);
            }
            font.draw(batch, "collected: " + flakesCounter, 10, SCR_HEIGHT - 10);
            font.draw(batch, showTime(currentTime), SCR_WIDTH - 125, SCR_HEIGHT - 10);
        }
        //game over
        else {
            font.draw(batch, showTime(currentTime), SCR_WIDTH/2-45, SCR_HEIGHT/2);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        imgBlFlake.dispose();
        imgPrFlake.dispose();
        imgBackground.dispose();
        mscInvierno.dispose();
        mscGyat.dispose();
        font.dispose();
    }
    private String showTime(long time){
        long ms = time%1000;
        long s = time/1000%60;
        long m = time/1000/60%60;
        return m/10+m%10+":"+s/10+s%10+":"+ms/100;
    }
    private void touches()
    {
        if (Gdx.input.justTouched())
        {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            for (Flake f: flakes)
            {
                if (f.hit(touch.x,touch.y))
                {
                    f.isHit = true;
                    f.image = imgPrFlake;
                    f.y =  MathUtils.random(SCR_HEIGHT, 2*SCR_HEIGHT);
                    flakesCounter ++;

                    //2 stage
                    if (flakesCounter == 100)
                    {
                        for (Flake y: flakes)
                        {
                            y.isHit = false;
                        }
                        mscInvierno.stop();
                        mscGyat.play();
                    }

                }
            }
        }
    }
}
