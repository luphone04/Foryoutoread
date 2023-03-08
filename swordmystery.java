package com.myjavaproject.swordmystery;
import com.badlogic.gdx.Game;
import com.myjavaproject.swordmystery.logic.GameProgress;
import com.myjavaproject.swordmystery.screens.CharacterSelectionScreen;
public class SwordMystery extends Game { //Game is used to switch between screens (it is a class, not an interface)
	public Resources res;
	@Override
	public void create () {
		res = new Resources(); //we call it here because we need to load resources before we can load game progress
		GameProgress.Load();
		SoundManager.LoadSounds();
		setScreen(new CharacterSelectionScreen(this)); //this refer to the current instance of the class SwordMystery. this is used to pass the current instance of the class SwordMystery to the constructor of the class CharacterSelectionScreen
		//By passing this() as an argument, the CharacterSelectionScreen can access the methods and properties of the SwordMystery class.
	}
	@Override
	public void dispose () {
		GameProgress.Save();
		res.dispose();
		SoundManager.ReleaseSounds();
	}
}



package com.myjavaproject.swordmystery;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.myjavaproject.swordmystery.logic.GameProgress;
public class SoundManager {
    /*
        swing.wav -> swing0.ogg
        swing2.wav -> swing1.ogg
        swing3.wav -> swing2.ogg
        coin.wav -> coin.ogg
        bite-small.wav -> heal.ogg
        fantozzi-sandl1 -> walk0.ogg
        fantozzi-sandl2 -> walk1.ogg
        Fantozzi-SandR1 -> walk2.ogg

        Battle Music: http://opengameart.org/content/8-bit-music-pack-loopable#comment-55568 by CodeManu
        Footsteps: http://opengameart.org/content/fantozzis-footsteps-grasssand-stone by Fantozzi (submitted by qubodup)
     */
    public static AssetManager assets = new AssetManager(); //AssetManager is used to load and manage assets
    private static Music bMusic = null; //bMusic is the battle music
    public static void LoadSounds()
    {
        for (int i = 0; i < 3; i++)
        {
            assets.load(Gdx.files.internal("music/swing" + i + ".ogg").path(), Sound.class); //of two parameter, the first is the path of the file, the second is the type of the file
            assets.load(Gdx.files.internal("music/walk" + i + ".ogg").path(), Sound.class);
        }

        assets.load(Gdx.files.internal("music/coin.ogg").path(), Sound.class);
        assets.load(Gdx.files.internal("music/heal.ogg").path(), Sound.class);

        assets.finishLoading();
    }
    public static void ReleaseSounds()
    {
        assets.dispose();
    }

    private static void playSoundRandomVolume(Sound sound, float min, float max)
    {
        if (sound != null)
        {
            sound.play(MathUtils.random(min, max) * GameProgress.soundVolume / GameProgress.MAX_SOUND_VOLUME);
        }
    }
    public static void PlaySwingSound()
    {
        Sound s = assets.get("music/swing" + MathUtils.random(2) + ".ogg", Sound.class); //Sound.class is the type of the file we want to get from the AssetManager (assets)
        playSoundRandomVolume(s, 0.9f, 1.0f);
    }

    public static void PlayWalkSound()
    {
        Sound s = assets.get("music/walk" + MathUtils.random(2) + ".ogg", Sound.class);
        playSoundRandomVolume(s, 0.5f, 0.6f);
    }

    public static void PlayCoinSound()
    {
        Sound coin = assets.get("music/coin.ogg", Sound.class);
        playSoundRandomVolume(coin, 0.9f, 1.0f);
    }

    public static void PlayHealSound()
    {
        Sound heal = assets.get("music/heal.ogg", Sound.class);
        playSoundRandomVolume(heal, 0.9f, 1.0f);
    }

    public static void StopBattleMusic()
    {
        if (bMusic != null)
        {
            bMusic.stop();
            bMusic = null;
        }
    }
    public static void PlayBattleMusic()
    {
        bMusic = Gdx.audio.newMusic(Gdx.files.internal("music/music" + MathUtils.random(5) + ".mp3"));
        bMusic.setLooping(true);
        bMusic.setVolume((float)GameProgress.soundVolume / GameProgress.MAX_SOUND_VOLUME);
        bMusic.play();
    }
    public static void AdjustVolume()
    {
        GameProgress.ToggleVolume(); //what it does is to change the value of the variable soundVolume
        if (bMusic != null)
        {
            bMusic.setVolume((float)GameProgress.soundVolume / GameProgress.MAX_SOUND_VOLUME);
        }
    }
}



package com.myjavaproject.swordmystery;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.myjavaproject.swordmystery.logic.objects.Character;
import com.myjavaproject.swordmystery.logic.objects.CharacterRecord;
import java.util.HashMap;
public class Resources {


    // enumerate the enemy types
    //Depending on type, we’re going to load the appropriate enemy sprite
    public static final int ENEMY_VERTICAL = 0; // spider
    public static final int ENEMY_HORIZONTAL = 1; // ghost
    public static final int ENEMY_DIAGONAL = 2; // bat
    public static final int ENEMY_RANDOM = 3; // slime
    public static final int ENEMY_UNIVERSAL = 4; // skeleton
    TextureAtlas gameSprites;
    public BitmapFont gamefont;
    public TextureRegion ground;
    public TextureRegion wall;
    public Sprite player;
    public HashMap<Integer, Sprite> enemySprites;
    public HashMap<String, Sprite> playerSprites;
    public TextureRegion base;
    public TextureRegion warning;
    public Sprite attackBonus;
    public Sprite healthBonus;
    public Sprite coinBonus;
    public TextureRegionDrawable soundBtn[];
    public TextureRegionDrawable leftArrowBtn;
    public TextureRegionDrawable rightArrowBtn;
    public TextureRegionDrawable upArrowBtn;
    public TextureRegionDrawable downArrowBtn;
    public static final int TILE_SIZE = 16;
    public Resources()
    {
        gamefont = new BitmapFont(Gdx.files.internal("gamefont.fnt"), Gdx.files.internal("gamefont.png"), false);

        gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
        ground = gameSprites.findRegion("ground");
        wall = gameSprites.findRegion("wall");

        player = new Sprite(gameSprites.findRegion("player"));

        enemySprites = new HashMap<Integer, Sprite>();
        enemySprites.put(ENEMY_VERTICAL, gameSprites.createSprite("spider"));
        enemySprites.put(ENEMY_HORIZONTAL, gameSprites.createSprite("ghost"));
        enemySprites.put(ENEMY_DIAGONAL, gameSprites.createSprite("bat"));
        enemySprites.put(ENEMY_RANDOM, gameSprites.createSprite("slime"));
        enemySprites.put(ENEMY_UNIVERSAL, gameSprites.createSprite("skeleton"));

        playerSprites = new HashMap<String, Sprite>();
        playerSprites.put(CharacterRecord.CHAR_NAME_HUMAN, gameSprites.createSprite("player"));
        playerSprites.put(CharacterRecord.CHAR_NAME_SPIDER, gameSprites.createSprite("spider"));
        playerSprites.put(CharacterRecord.CHAR_NAME_SKELETON, gameSprites.createSprite("skeleton"));
        playerSprites.put(CharacterRecord.CHAR_NAME_GHOST, gameSprites.createSprite("ghost"));
        playerSprites.put(CharacterRecord.CHAR_NAME_SLIME, gameSprites.createSprite("slime"));

        base = gameSprites.findRegion("base");
        warning = gameSprites.findRegion("warning");

        attackBonus = gameSprites.createSprite("attack");
        healthBonus = gameSprites.createSprite("health");
        coinBonus = gameSprites.createSprite("coin");

        soundBtn = new TextureRegionDrawable[4];
        for (int i = 0; i < 4; i++)
        {
            soundBtn[i] = new TextureRegionDrawable(gameSprites.findRegion("sound" + i));
        }

        leftArrowBtn = new TextureRegionDrawable(gameSprites.findRegion("larrow"));
        rightArrowBtn = new TextureRegionDrawable(gameSprites.findRegion("rarrow"));
        upArrowBtn = new TextureRegionDrawable(gameSprites.findRegion("uarrow"));
        downArrowBtn = new TextureRegionDrawable(gameSprites.findRegion("darrow"));
    }

    public void dispose()
    {
        gameSprites.dispose();
    }
}




package com.myjavaproject.swordmystery.screens;

import com.badlogic.gdx.Screen;
import com.myjavaproject.swordmystery.SwordMystery;
public class DefaultScreen implements Screen { //Screen is interface, not class


    protected SwordMystery game; //protected means that it can be accessed by classes in the same package and by subclasses

    DefaultScreen(SwordMystery _game) //connect to the game class (SwordMystery) to be able to switch between screens (setScreen)
    {
        game = _game;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}




package com.myjavaproject.swordmystery.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.myjavaproject.swordmystery.SwordMystery;
import com.myjavaproject.swordmystery.Resources;
import com.myjavaproject.swordmystery.SoundManager;
import com.myjavaproject.swordmystery.graph.Background;
import com.myjavaproject.swordmystery.graph.SizeEvaluator;
import com.myjavaproject.swordmystery.logic.GameLogic;
import com.myjavaproject.swordmystery.logic.GameProgress;
import com.myjavaproject.swordmystery.logic.objects.Bonus;
import com.myjavaproject.swordmystery.logic.objects.Player;
public class GameScreen extends DefaultScreen implements GameLogic.GameEventListener {
    SpriteBatch batch;

    // 8 height
    // 12 width
    public static final int SCREEN_W = 12 * Resources.TILE_SIZE; // 192 //we can use resources here
    // because we have already loaded the resources in the SwordMystery class (see the create() method) and
    // we can access the resources from the SwordMystery class because we have passed the current instance of the
    // SwordMystery class to the constructor of the GameScreen class (see the setScreen() method in the SwordMystery class)
    public static final int SCREEN_H = 8 * Resources.TILE_SIZE; // 128

    private static final float SHAKE_TIME_ON_DMG = 0.3f;
    private static final float SHAKE_DIST = 4.0f;

    private SizeEvaluator sizeEvaluator;

    private Stage gameStage; //we use Stage to draw the game objects on the screen (Stage is a class, not an interface) (Stage is a container for Actors)
    private Background bg; //we use Background to draw the background on the screen (Background is a class, not an interface)
    private GameLogic logic;

    private Player player;
    private ImageButton sndBtn;

    private Group controlGroup;

    public static final float GAME_END_FADEOUT = 0.5f;
    public static final float GAME_START_FADEIN = 0.25f;

    boolean endgame = false;

    public GameScreen(SwordMystery _game) {
        super(_game);
        batch = new SpriteBatch(); //we write this so that we can draw the Actors on the screen

        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H); //we use ExtendViewport because we want to keep the aspect ratio of the screen
        gameStage = new Stage(viewport, batch); //viewport to determine the size and position of the Actors and batch is responsible for drawing the Actors on the screen
        bg = new Background();
        SoundManager.PlayBattleMusic(); //we access soundmanager by using the SoundManager class (we have already created the SoundManager class in the com.myjavaproject.swordmystery package)

        sizeEvaluator = new SizeEvaluator(gameStage,
                game.res,
                GameLogic.MAX_BASE_X,
                GameLogic.MAX_BASE_Y,
                gameStage.getWidth());

        logic = new GameLogic(game, this); //we create a new instance of the GameLogic class and pass the current instance of the SwordMystery class to the constructor of the GameLogic class
        player = logic.getPlayer(); //we get the player from the logic class

        Gdx.input.setInputProcessor(gameStage); //we set the input processor to the gameStage so that we can use the keyboard to move the player

        gameStage.addAction( //we use addAction() method to add an action to the gameStage
                new Action() { //we create a new instance of the Action class
                    float time = 0;
                    @Override
                    public boolean act(float delta) {
                        time += delta;
                        float t = time / GAME_START_FADEIN;
                        t *= t;
                        if (t > 1.0f) //1.0f is the maximum value of the float variable
                        {
                            t = 1.0f;
                        }

                        batch.setColor(1, 1, 1, t); //we set the color of the batch to white and the alpha value to t
                        return time >= GAME_START_FADEIN;
                    }
                }
        );

        sndBtn = new ImageButton(game.res.soundBtn[GameProgress.soundVolume]);
        sndBtn.setPosition(gameStage.getWidth() - sndBtn.getWidth() - 10, 10);
        sndBtn.addListener(new ClickListener()
        {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                SoundManager.AdjustVolume();
                sndBtn.getStyle().imageUp = game.res.soundBtn[GameProgress.soundVolume];
                super.touchUp(event, x, y, pointer, button);
            }
        });
        gameStage.addActor(sndBtn);
        gameStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode)
                {
                    case Input.Keys.W:
                        AttemptMove(0, 1);
                        break;
                    case Input.Keys.S:
                        AttemptMove(0, -1);
                        break;
                    case Input.Keys.A:
                        AttemptMove(-1, 0);
                        break;
                    case Input.Keys.D:
                        AttemptMove(1, 0);
                        break;
                };
                return false;
            }
        });

        gameStage.getCamera().update();
        batch.setProjectionMatrix(gameStage.getCamera().combined);

        controlGroup = new Group();
        gameStage.addActor(controlGroup);
        //if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            prepareDirectionButtons();
        }
    }

    private void prepareDirectionButton(final int dx,
                                        final int dy,
                                        TextureRegionDrawable img,
                                        float x,
                                        float y)
    {
        ImageButton btn = new ImageButton(img);
        btn.setPosition(x, y);
        btn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                AttemptMove(dx, dy);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        controlGroup.addActor(btn);
    }

    private void prepareDirectionButtons() {

        // up-down
        prepareDirectionButton(0, 1, game.res.upArrowBtn, 2, gameStage.getHeight() / 2 + 2);
        prepareDirectionButton(0, -1, game.res.downArrowBtn, 2, gameStage.getHeight() / 2 - 16);

        // left-right
        prepareDirectionButton(-1, 0, game.res.leftArrowBtn, gameStage.getWidth() - 36, gameStage.getHeight() / 2 - 9);
        prepareDirectionButton(1, 0, game.res.rightArrowBtn, gameStage.getWidth() - 18, gameStage.getHeight() / 2 - 9);
    }

    public void update(float delta)
    {
        gameStage.act(delta);
        logic.update(delta);
    }

    public void drawBases()
    {
        batch.begin();

        // draw 4 x 4 bases:
        for (int x = 0; x <= GameLogic.MAX_BASE_X; x++)
        {
            for (int y = 0; y <= GameLogic.MAX_BASE_Y; y++)
            {
                batch.draw(game.res.base,
                        sizeEvaluator.getBaseScreenX(x),
                        sizeEvaluator.getBaseScreenY(y));
            }
        }

        batch.end();
    }

    private void DrawShadowed(String str, float x, float y, float width, int align, Color color)
    {
        game.res.gamefont.setColor(Color.BLACK);

        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                game.res.gamefont.draw(batch, str, x + i, y + j, width, align, false);
            }
        }

        game.res.gamefont.setColor(color);
        game.res.gamefont.draw(batch, str, x, y, width, align, false);
        game.res.gamefont.setColor(Color.WHITE);
    }

    private void ShowGameResult(String result)
    {
        DrawShadowed(result,
                0,
                gameStage.getHeight() / 2,
                gameStage.getWidth(),
                Align.center,
                Color.RED);
    }

    private void DrawUI() //this is for drawing the UI elements on the screen (lives, coins, etc.)
    {
        batch.begin();
        DrawShadowed("LIVES:" + player.getLives(),
                5,
                gameStage.getHeight() - 7,
                gameStage.getWidth(),
                Align.left,
                Color.WHITE);

        DrawShadowed("ENEMY:" + logic.getEnemy().getLives(),
                0,
                gameStage.getHeight() - 7,
                gameStage.getWidth() - 5,
                Align.right,
                Color.WHITE);

        batch.draw(game.res.coinBonus,
                gameStage.getViewport().getScreenX() + 2,
                gameStage.getViewport().getScreenY() + 5);

        DrawShadowed("" + GameProgress.currentGold,
                gameStage.getViewport().getScreenX() + game.res.coinBonus.getWidth() + 4,
                gameStage.getViewport().getScreenY() + 8 + game.res.coinBonus.getHeight() / 2,
                gameStage.getWidth() - 4,
                Align.left,
                Color.WHITE);

        if (player.getLives() <= 0)
        {
            ShowGameResult("DEFEAT!");
        }
        else if (logic.getEnemy().getLives() <= 0)
        {
            ShowGameResult("VICTORY!");
        }
        batch.end();
    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bg.draw(gameStage, game.res);
        drawBases();

        batch.begin();
        for (Bonus bonus : logic.getBonuses())
        {
            bonus.draw(batch, sizeEvaluator);
        }
        player.draw(batch, sizeEvaluator);
        logic.getEnemy().draw(batch, sizeEvaluator);
        batch.end();

        logic.getEffectEngine().draw(batch, sizeEvaluator); //Drawing Warning Effect

        gameStage.getCamera().position.set(gameStage.getWidth() / 2, gameStage.getHeight() / 2, 0);
        if (player.getLives() > 0 &&
            player.getTimeAlive() - player.getTimeOfDmgTaken() < SHAKE_TIME_ON_DMG)
        {
            gameStage.getCamera().translate(-(SHAKE_DIST/2) + MathUtils.random(SHAKE_DIST),
                    -(SHAKE_DIST/2) + MathUtils.random(SHAKE_DIST), 0);
        }
        gameStage.getCamera().update();

        DrawUI();
        gameStage.draw();

        if (endgame)
        {
            dispose();
            if (playerWon)
            {
                game.setScreen(new GameScreen(game));
            }
            else
            {
                game.setScreen(new CharacterSelectionScreen(game));
            }
        }
    }

    @Override
    public void dispose()
    {
        SoundManager.StopBattleMusic();
        super.dispose();
        batch.dispose();
        gameStage.dispose();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        gameStage.getViewport().update(width, height, true);
        sizeEvaluator.setRightSideX(gameStage.getWidth());
    }

    public void AttemptMove(int dx, int dy)
    {
        if (player.getLives() > 0 &&
            logic.getEnemy().getLives() > 0 &&
            logic.CanMove(player.getFieldX() + dx, player.getFieldY() + dy))
        {
            SoundManager.PlayWalkSound();
            logic.AssignPlayerPosition(player.getFieldX() + dx, player.getFieldY() + dy);
        }
    }

    boolean playerWon;
    @Override
    public void OnGameEnd(final boolean playerWon) {
        controlGroup.remove();
        this.playerWon = playerWon;
        gameStage.addAction(
            Actions.sequence(
                new Action() {
                    float time = 0;
                    @Override
                    public boolean act(float delta) {
                        time += delta;

                        float t = time / GAME_END_FADEOUT; // 0 .. 1
                        t *= t;
                        batch.setColor(1, 1, 1, 1 - t);
                        return time >= GAME_END_FADEOUT;
                    }
                },
                new Action()
                {
                    @Override
                    public boolean act(float delta) {
                        endgame = true;
                        return true;
                    }
                }
            )
        );
    }

    @Override
    public void OnBonusPickup(byte bonusType) {
        if (bonusType == Bonus.BONUS_TYPE_COIN)
        {
            SoundManager.PlayCoinSound();
        }
        else if (bonusType == Bonus.BONUS_TYPE_HEALTH)
        {
            SoundManager.PlayHealSound();
        }
    }
}




package com.myjavaproject.swordmystery.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myjavaproject.swordmystery.SwordMystery;
import com.myjavaproject.swordmystery.logic.GameProgress;
import com.myjavaproject.swordmystery.logic.objects.CharacterRecord;



public class CharacterSelectionScreen extends DefaultScreen {

    Stage uiStage; // a stage that is going to process player input / display UI elements.

    private Label prepareStatLabel(String text, float x, float y, Label.LabelStyle textStyle)
    {
        Label lbl = new Label(text, textStyle);
        lbl.setAlignment(Align.left);
        lbl.setPosition(x, y);
        uiStage.addActor(lbl);
        return lbl;
    }

    void prepareUi()
    {
        uiStage.clear(); // clear the stage to remove all previous actors from it.

        Label.LabelStyle textStyle = new Label.LabelStyle(game.res.gamefont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.res.gamefont;
        buttonStyle.fontColor = Color.WHITE;

        if (GameProgress.levels[GameProgress.currentCharacter] == 0) // char is locked
        {
            TextButton upgradeBtn = new TextButton("Unlock(100 Gold)", buttonStyle);
            upgradeBtn.setPosition((uiStage.getWidth() - upgradeBtn.getWidth()) / 2,
                    uiStage.getHeight() / 6);
            upgradeBtn.addListener(new ClickListener()
            {
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    if (GameProgress.currentGold >= GameProgress.CHARACTER_PRICE)
                    {
                        GameProgress.currentGold -= GameProgress.CHARACTER_PRICE;
                        GameProgress.levels[GameProgress.currentCharacter] = 1;
                        prepareUi();
                    }
                }
            });

            uiStage.addActor(upgradeBtn);
        }
        else
        {
            TextButton startButton = new TextButton("START", buttonStyle);
            startButton.setPosition((uiStage.getWidth() - startButton.getWidth()) / 2,
                    uiStage.getHeight() * 5 / 6);
            startButton.addListener(new ClickListener()
            {
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    GameProgress.Reset(false);
                    dispose();
                    game.setScreen(new GameScreen(game));
                }
            });
            uiStage.addActor(startButton);

            TextButton upgradeBtn = new TextButton("LvlUp(" +
                    GameProgress.getNextUpgradeCost(GameProgress.currentCharacter) + ")",
                    buttonStyle);
            upgradeBtn.setPosition((uiStage.getWidth() - upgradeBtn.getWidth()) / 2,
                    uiStage.getHeight() / 6);
            upgradeBtn.addListener(new ClickListener()
            {
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    if (GameProgress.currentGold >= GameProgress.getNextUpgradeCost(GameProgress.currentCharacter))
                    {
                        GameProgress.currentGold -= GameProgress.getNextUpgradeCost(GameProgress.currentCharacter);
                        GameProgress.levels[GameProgress.currentCharacter] += 1;
                        prepareUi();
                    }
                }
            });
            uiStage.addActor(upgradeBtn);
        }

        Image heroSprite = new Image(
            game.res.playerSprites.get(CharacterRecord.CHARACTERS[GameProgress.currentCharacter].name)
        );

        heroSprite.setPosition((uiStage.getWidth() - heroSprite.getWidth()) / 4,
                (uiStage.getHeight() - heroSprite.getHeight()) / 2);
        uiStage.addActor(heroSprite);

        Label stat = prepareStatLabel("DMG:" + GameProgress.getPlayerDamage(),
                uiStage.getWidth() / 2,
                heroSprite.getY() + heroSprite.getHeight(),
                textStyle);

        stat = prepareStatLabel("HP:" + GameProgress.getPlayerMaxHp(),
                uiStage.getWidth() / 2,
                stat.getY() - 10,
                textStyle);

        stat = prepareStatLabel("HEAL:" + GameProgress.getPlayerHealthRestored(),
                uiStage.getWidth() / 2,
                stat.getY() - 10,
                textStyle);

        prepareStatLabel("BNS:" + GameProgress.getBonusReductionValue(),
                uiStage.getWidth() / 2,
                stat.getY() - 10,
                textStyle);

        int lvl = GameProgress.levels[GameProgress.currentCharacter];
        Label statusText = new Label(lvl > 0 ? "LVL: " + lvl : "LOCKED", textStyle);
        statusText.setPosition(heroSprite.getX() + (heroSprite.getWidth() - statusText.getWidth()) / 2,
                heroSprite.getY() - statusText.getHeight() - 5);
        uiStage.addActor(statusText);

        TextButton nextButton = new TextButton(">>>", buttonStyle);
        nextButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameProgress.currentCharacter += 1;
                if (GameProgress.currentCharacter == CharacterRecord.CHARACTERS.length)
                {
                    GameProgress.currentCharacter = 0;
                }

                GameProgress.Reset(false);
                prepareUi();
            }
        });
        nextButton.setPosition(uiStage.getWidth() * 5 / 6 - nextButton.getWidth() /2,
                uiStage.getHeight() * 5 / 6);
        uiStage.addActor(nextButton);

        TextButton prevButton = new TextButton("<<<", buttonStyle);
        prevButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameProgress.currentCharacter -= 1;
                if (GameProgress.currentCharacter < 0)
                {
                    GameProgress.currentCharacter = CharacterRecord.CHARACTERS.length - 1;
                }

                GameProgress.Reset(false);
                prepareUi();
            }
        });
        prevButton.setPosition(uiStage.getWidth() / 6, uiStage.getHeight() * 5 / 6);
        uiStage.addActor(prevButton);

        // draw the image of coins
        Image coinImage = new Image(game.res.coinBonus);
        coinImage.setPosition(1, 1);
        uiStage.addActor(coinImage);

        // amount of coins
        Label coinAmntLbl = new Label("" + GameProgress.currentGold, textStyle);

        coinAmntLbl.setPosition(coinImage.getX() + coinImage.getWidth() + 3,
                coinImage.getY() + (coinImage.getHeight() - coinAmntLbl.getHeight()) / 2);
        uiStage.addActor(coinAmntLbl);
    }

    public CharacterSelectionScreen(SwordMystery _game) {
        super(_game);

        FitViewport viewport = new FitViewport(160, 120);//create a stage using fit viewport to always maintain the resolution
        uiStage = new Stage(viewport);
        Gdx.input.setInputProcessor(uiStage); //use our stage as inputProcessor, because it would allow stage to capture the inputs
        prepareUi();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.act(delta); //update the stage
        uiStage.draw();
    }

    @Override
    public void dispose()
    {
        Gdx.input.setInputProcessor(null);
        uiStage.dispose();
        super.dispose();
    }

    @Override
    public void resize(int w, int h)
    {
        super.resize(w, h);
        uiStage.getViewport().update(w, h, true);
    }
}




package com.myjavaproject.swordmystery.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.myjavaproject.swordmystery.logic.objects.CharacterRecord;


public class GameProgress {

    public static final int MAX_SOUND_VOLUME = 3;
    public static int playerLives = 3;
    public static int currentCharacter = 0;
    public static int currentGold = 0; // separate variable that would store the coins.
    public static int soundVolume = MAX_SOUND_VOLUME;
    public static final int CHARACTER_PRICE = 100;
    public static int levels[]; // level of each character, 0 = locked
    public static int stages[]; // how far the character has progressed
    private static final String PROGRESS_SAVE_NAME = "progress";
    private static final String SAVE_KEY_LIVES = "lives";
    private static final String SAVE_KEY_PLAYER_LEVEL = "playerlevel";
    private static final String SAVE_KEY_PLAYER_STAGE = "playerstage";
    private static final String SAVE_KEY_PLAYER_GOLD = "playergold"; //saving/loading
    private static final String SAVE_KEY_SOUND_VOLUME = "soundvolume";

    public static int getEnemyLives()
    {
        return 3 + stages[currentCharacter] * 2; // 3 lives on lvl0, 5 lives on lvl1, etc
    }

    public static int getEnemyDamage()
    {
        return 1 + stages[currentCharacter] / 10;
    }

    public static void Save()
    {
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        prefs.putInteger(SAVE_KEY_LIVES, playerLives);

        prefs.putInteger(SAVE_KEY_PLAYER_GOLD, currentGold);
        prefs.putInteger(SAVE_KEY_SOUND_VOLUME, soundVolume);

        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i++)
        {
            prefs.putInteger(SAVE_KEY_PLAYER_LEVEL + i, levels[i]);
            prefs.putInteger(SAVE_KEY_PLAYER_STAGE + i, stages[i]);
        }

        prefs.flush();
    }

    public static void Load()
    {
        levels = new int[CharacterRecord.CHARACTERS.length];
        stages = new int[CharacterRecord.CHARACTERS.length];

        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        playerLives = prefs.getInteger(SAVE_KEY_LIVES, 3);
        currentGold = prefs.getInteger(SAVE_KEY_PLAYER_GOLD, 0); //0 is the default value if the key is not found in the preferences file. Preferences file is created when the game is first run.
        soundVolume = prefs.getInteger(SAVE_KEY_SOUND_VOLUME, MAX_SOUND_VOLUME);

        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i++)
        {
            levels[i] = prefs.getInteger(SAVE_KEY_PLAYER_LEVEL + i, i == 0 ? 1 : 0);
            stages[i] = prefs.getInteger(SAVE_KEY_PLAYER_STAGE + i, 0);
        }
    }

    public static void Reset(boolean resetProgress) {
        if (resetProgress)
        {
            stages[currentCharacter] -= 5;
            if (stages[currentCharacter] < 0)
            {
                stages[currentCharacter] = 0;
            }
        }

        playerLives = getPlayerMaxHp();
    }

    public static int getNextUpgradeCost(int currentCharacter) {
        return levels[currentCharacter] * 2;
    }

    public static int getPlayerMaxHp() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getMaxHp(levels[currentCharacter]);
    }

    public static int getPlayerDamage() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getDmg(levels[currentCharacter]);
    }

    public static int getPlayerHealthRestored() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getHpRestored(levels[currentCharacter]);
    }

    public static float getPlayerBonusReduction() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return currentChar.getBonusSpawnReduction(levels[currentCharacter]);
    }

    public static int getBonusReductionValue() {
        CharacterRecord currentChar = CharacterRecord.CHARACTERS[currentCharacter];
        return levels[currentCharacter] / currentChar.levelsForBonusSpawnUpgrade;
    }

    public static void increaseStage() {
        currentGold += 1 + stages[currentCharacter] / 4;
        stages[currentCharacter]++;
    }

    public static void ToggleVolume()
    {
        soundVolume += 1;
        if (soundVolume > MAX_SOUND_VOLUME)
        {
            soundVolume = 0;
        }
    }
}




package com.myjavaproject.swordmystery.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.myjavaproject.swordmystery.SwordMystery;
import com.myjavaproject.swordmystery.Resources;
import com.myjavaproject.swordmystery.graph.effects.EffectEngine;
import com.myjavaproject.swordmystery.graph.effects.WarningEffect;
import com.myjavaproject.swordmystery.logic.objects.Bonus;
import com.myjavaproject.swordmystery.logic.objects.Enemy;
import com.myjavaproject.swordmystery.logic.objects.Player;

import java.util.ArrayList;


public class GameLogic implements Enemy.EnemyAttackListener, WarningEffect.WarningEffectListener {

    public static final int MAX_BASE_X = 3;
    public static final int MAX_BASE_Y = 3;
    private final float BONUS_SPAWN_INTERVAL; // spawn bonus every 2 seconds
    private static final int MAX_BONUSES_ON_FIELD = 3;

    public interface GameEventListener
    {
        void OnGameEnd(final boolean playerWon);
        void OnBonusPickup(byte bonusType);
    }

    Player player;
    Enemy enemy;

    EffectEngine effectEngine;
    SwordMystery game;

    ArrayList<Bonus> bonuses;
    float gameTime;
    float lastBonusSpawnTime;

    GameEventListener eventListener;
    public GameLogic(SwordMystery _game, GameEventListener _listener) {
        BONUS_SPAWN_INTERVAL = 2.0f * (1 - GameProgress.getPlayerBonusReduction());
        eventListener = _listener;
        game = _game;
        player = new Player(
                MathUtils.random(MAX_BASE_X),
                MathUtils.random(MAX_BASE_Y),
                game.res,
                GameProgress.playerLives
        ); // 0..3

        enemy = new Enemy(game.res, this, MathUtils.random(Resources.ENEMY_UNIVERSAL)); //the enemy is going to be chosen randomly
        effectEngine = new EffectEngine();

        bonuses = new ArrayList<Bonus>();
        gameTime = 0;
        lastBonusSpawnTime = 0;
    }
    public Player getPlayer()
    {
        return player;
    }

    public Enemy getEnemy()
    {
        return enemy;
    }

    private void SpawnRandomBonus()
    {
        int fx = 0;
        int fy = 0;
        boolean targetNonEmpty = true;
        do {
            fx = MathUtils.random(MAX_BASE_X);
            fy = MathUtils.random(MAX_BASE_Y);
            targetNonEmpty = player.getFieldX() == fx || fy == player.getFieldY();

            for (int i = 0; i < bonuses.size() && (targetNonEmpty == false); i++)
            {
                if (bonuses.get(i).getFieldX() == fx &&
                        bonuses.get(i).getFieldY() == fy)
                {
                    targetNonEmpty = true;
                }
            }
        } while (targetNonEmpty);


        byte activeBonus = Bonus.BONUS_TYPE_ATTACK; //use it by default , this is for case when no bonus is spawned
        int rnd = MathUtils.random(7); // 0 .. 7 //reason for this is that we want to have 1/8 chance to get health, 1/4 to get gold and 3/4 to get attack
        // 1/8 chance to get health, 1/4 to get gold
        if (rnd > 6)
        {
            activeBonus = Bonus.BONUS_TYPE_HEALTH;
        }
        else if (rnd > 4)
        {
            activeBonus = Bonus.BONUS_TYPE_COIN;
        }

        bonuses.add(Bonus.Create(fx, fy,
                activeBonus,
                game.res));
        lastBonusSpawnTime = gameTime;
    }

    public void update(float delta)
    {
        gameTime += delta;
        player.update(delta);

        if (player.getLives() > 0 && enemy.getLives() > 0) {
            effectEngine.update(delta);
            enemy.update(delta);

            if (lastBonusSpawnTime + BONUS_SPAWN_INTERVAL < gameTime &&
                    bonuses.size() < MAX_BONUSES_ON_FIELD) {
                SpawnRandomBonus();
            }
        }
    }

    public boolean CanMove(int fx, int fy)
    {
        return (fx >= 0 && fx <= MAX_BASE_X) &&
                (fy >= 0 && fy <= MAX_BASE_Y);
    }

    public void AssignPlayerPosition(int fx, int fy)
    {
        player.setFieldX(fx);
        player.setFieldY(fy);

        for (int i = bonuses.size() - 1; i >= 0; i--)
        {
            Bonus currentBonus = bonuses.get(i);
            if (currentBonus.getFieldX() == fx &&
                    currentBonus.getFieldY() == fy)
            {
                eventListener.OnBonusPickup(currentBonus.getBonusType());
                if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_HEALTH)
                {
                    player.addLives(GameProgress.getPlayerHealthRestored());
                }
                else if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_ATTACK)
                {
                    enemy.takeDamage(GameProgress.getPlayerDamage());
                    if (enemy.getLives() <= 0)
                    {
                        GameProgress.increaseStage();
                        GameProgress.playerLives = player.getLives();
                        player.markVictorious();
                        eventListener.OnGameEnd(true);
                    }
                }
                else if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_COIN) //currentBonus.getBonusType() represent the type of bonus that we picked up (health, attack, gold) and we can use it to determine what to do with it
                {
                    GameProgress.currentGold += 1;
                }

                currentBonus.release();
                bonuses.remove(i);
                break;
            }
        }
    }

    public EffectEngine getEffectEngine() //a way to draw the effects. Make a getter inside GameLogic that would
    // return a pointer to our EffectEngine.
    {
        return effectEngine;
    }

    @Override
    public void OnAttack(boolean[][] tiles) {
        for (int x = 0; x < tiles.length; x++)
        {
            for (int y = 0; y < tiles[x].length; y++)
            {
                if (tiles[x][y])
                {
                    WarningEffect.Create(x, y, effectEngine, game.res, this);
                }
            }
        }
    }

    @Override
    public void OnEffectOver(WarningEffect effect) {
        if (effect.getFieldX() == player.getFieldX() &&
                effect.getFieldY() == player.getFieldY())
        {
            player.takeDamage(GameProgress.getEnemyDamage());
            if (player.getLives() <= 0)
            {
                eventListener.OnGameEnd(false);
                GameProgress.Reset(true);
            }
        }
    }

    public ArrayList<Bonus> getBonuses()
    {
        return bonuses;
    }
}





package com.myjavaproject.swordmystery.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myjavaproject.swordmystery.Resources;
import com.myjavaproject.swordmystery.graph.SizeEvaluator;
import com.myjavaproject.swordmystery.logic.GameProgress;
public class Player extends Character {
    private int fieldX;
    private int fieldY;
    private boolean winning = false;
    private float winTime = 0; //represents the time at which the player wins the game
    private final int max_lives;
    public static final float APPROACH_TIME = 0.5f;

    public Player(int fx, int fy, Resources res, int _lives)
    {
        super(_lives);
        fieldX = fx;
        fieldY = fy;
        //set(res.player);
        set(res.playerSprites.get(CharacterRecord.CHARACTERS[GameProgress.currentCharacter].name));
        max_lives = GameProgress.getPlayerMaxHp();
    }

    public int getFieldX()
    {
        return fieldX;
    }

    public void setFieldX(int fx)
    {
        fieldX = fx;
    }

    public int getFieldY()
    {
        return fieldY;
    }

    public void setFieldY(int fy)
    {
        fieldY = fy;
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        preDraw(); //sets the color to white and alpha to 1 if the character is not invulnerable or
        // if it is invulnerable but the time is up for the invulnerability to end
        if (timeAlive < APPROACH_TIME) //meaning the player is still approaching the field from the left side of the screen
        {
            float t = timeAlive / APPROACH_TIME; // 0..1
            t = t * t;
            setPosition(
                    t * sizeEvaluator.getBaseScreenX(fieldX),
                    sizeEvaluator.getBaseScreenY(fieldY));
        }
        else if (winning)
        {
            float t = 1;
            if (timeAlive - winTime < APPROACH_TIME)
            {
                t = (timeAlive - winTime) / APPROACH_TIME;
                t = t * t;
            }

            float fx = sizeEvaluator.getBaseScreenX(fieldX);
            setPosition(fx + t * (sizeEvaluator.getRightSideX() - fx),
                    sizeEvaluator.getBaseScreenY(fieldY));
        }
        else
        {
            setPosition(sizeEvaluator.getBaseScreenX(fieldX),
                    sizeEvaluator.getBaseScreenY(fieldY));
        }
        super.draw(batch);
        postDraw();
    }

    public void addLives(int amount) {
        lives += amount;
        if (lives > max_lives)
        {
            lives = max_lives;
        }
    }

    public void markVictorious()
    {
        winning = true;
        winTime = timeAlive;
        GameProgress.playerLives = lives;
    }
}



package com.myjavaproject.swordmystery.logic.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.myjavaproject.swordmystery.Resources;
import com.myjavaproject.swordmystery.graph.SizeEvaluator;
import com.myjavaproject.swordmystery.logic.GameLogic;
import com.myjavaproject.swordmystery.logic.GameProgress;

public class Enemy extends Character {
    private static final float BASE_ATTACK_TIME = 1.0f;
    private static final float WARM_UP_TIME = 2.0f;
    private float timeSinceAttack;
    private float nextAttackTime;
    private int lives;
    private static float SCALE_TIME = 0.5f;

    private boolean targetTiles[][];

    public interface EnemyAttackListener
    {
        void OnAttack(boolean[][] tiles);
    }

    private EnemyAttackListener attackListener;

    private int type;

    public Enemy(Resources res, EnemyAttackListener listener, int _type)
    {
        super(GameProgress.getEnemyLives());
        type = _type;
        set(res.enemySprites.get(type));

        resetAttackTime();
        attackListener = listener;

        targetTiles = new boolean[GameLogic.MAX_BASE_X + 1][];
        for (int i = 0; i < GameLogic.MAX_BASE_Y + 1; i++)
        {
            targetTiles[i] = new boolean[GameLogic.MAX_BASE_Y + 1];
        }
    }

    public void resetAttackTime()
    {
        timeSinceAttack = 0;
        nextAttackTime = BASE_ATTACK_TIME + MathUtils.random(2);
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        preDraw();
        setPosition(sizeEvaluator.getEnemyX(this), sizeEvaluator.getEnemyY(this));
        if (timeAlive < SCALE_TIME)
        {
            float t = timeAlive / SCALE_TIME; // 0..1
            t = t * t;
            setScale(t);
        }
        else
        {
            setScale(1);
        }
        super.draw(batch);
        postDraw();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        timeSinceAttack += delta;
        if (timeAlive > WARM_UP_TIME && timeSinceAttack > nextAttackTime) //meaning the enemy is ready to attack
        {
            switch (type)
            {
                case Resources.ENEMY_VERTICAL:
                    performVerticalLineAttack();
                    break;
                case Resources.ENEMY_HORIZONTAL:
                    performHorizontalLineAttack();
                    break;
                case Resources.ENEMY_DIAGONAL:
                    performDiagonalAttack();
                    break;
                case Resources.ENEMY_RANDOM:
                    performRandomAttack();
                    break;
                default:
                    performUltimateAttack();
                    break;
            }
            attackListener.OnAttack(targetTiles);
            resetAttackTime();
        }
    }

    private void performVerticalLineAttack()
    {
        int col1 = MathUtils.random(GameLogic.MAX_BASE_X);
        int col2 = 0;
        do {
            col2 = MathUtils.random(GameLogic.MAX_BASE_X);
        } while (col1 == col2);

        for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
        {
            for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
            {
                targetTiles[x][y] = (x == col1 || x == col2);
            }
        }
    }

    private void performHorizontalLineAttack()
    {
        int row1 = MathUtils.random(GameLogic.MAX_BASE_Y);
        int row2 = 0;
        do {
            row2 = MathUtils.random(GameLogic.MAX_BASE_Y);
        } while (row1 == row2);

        for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
        {
            for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
            {
                targetTiles[x][y] = (y == row1 || y == row2);
            }
        }
    }

    private void fillDiagonal(int xstart, int dx)
    {
        for (int i = 0; i <= GameLogic.MAX_BASE_Y; i++)
        {
            int nx = xstart + i * dx;
            if (nx > GameLogic.MAX_BASE_X)
            {
                nx = nx - GameLogic.MAX_BASE_X - 1;
            }

            if (nx < 0)
            {
                nx = nx + GameLogic.MAX_BASE_X + 1;
            }

            targetTiles[nx][i] = true;
        }
    }

    private void performDiagonalAttack()
    {
        int dx1 = -1 + MathUtils.random(1) * 2; // 1 .. -1
        int dx2 = -1 + MathUtils.random(1) * 2; // 1 .. -1

        int col1 = MathUtils.random(GameLogic.MAX_BASE_X);
        int col2 = 0;
        do {
            col2 = MathUtils.random(GameLogic.MAX_BASE_X);
        } while (col1 == col2);

        for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
        {
            for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
            {
                targetTiles[x][y] = false;
            }
        }

        fillDiagonal(col1, dx1);
        fillDiagonal(col2, dx2);
    }

    private void performRandomAttack()
    {
        for (int x = 0; x < GameLogic.MAX_BASE_X + 1; x++)
        {
            for (int y = 0; y < GameLogic.MAX_BASE_Y +1; y++)
            {
                targetTiles[x][y] = false;
            }
        }

        for (int i = 0; i < 10; i++)
        {
            int nx = MathUtils.random(GameLogic.MAX_BASE_X);
            int ny = MathUtils.random(GameLogic.MAX_BASE_Y);

            targetTiles[nx][ny] = true;
        }
    }

    private void performUltimateAttack()
    {
        int rnd = MathUtils.random(3);
        switch (rnd)
        {
            case 0:
                performVerticalLineAttack();
                break;
            case 1:
                performHorizontalLineAttack();
                break;
            case 2:
                performDiagonalAttack();
                break;
            default:
                performRandomAttack();
                break;
        }
    }

}

package com.myjavaproject.swordmystery.logic.objects;

public class CharacterRecord {

    public final int levelsForHpUpgrade;
    public final int levelsForHpRegenUpgrade;
    public final int levelsForAttackUpgrade;
    public final int levelsForBonusSpawnUpgrade;
    public final String name;

    public CharacterRecord(int lvlHp, int lvlRegen, int lvlAttack, int lvlBonus, String _name)
    {
        levelsForHpUpgrade = lvlHp;
        levelsForHpRegenUpgrade = lvlRegen;
        levelsForAttackUpgrade = lvlAttack;
        levelsForBonusSpawnUpgrade = lvlBonus;
        name = _name;
    }

    public static String CHAR_NAME_HUMAN = "Human";
    public static String CHAR_NAME_SPIDER = "Spider";
    public static String CHAR_NAME_SKELETON = "Mr.Skeletal";
    public static String CHAR_NAME_GHOST = "Ghost";
    public static String CHAR_NAME_SLIME = "Slimey";

    public static CharacterRecord CHARACTERS[] = { // contains instances of the CharacterRecord class for all the characters in the game
        new CharacterRecord(2, 2, 4, 4, CHAR_NAME_HUMAN),
        new CharacterRecord(3, 6, 3, 3, CHAR_NAME_SPIDER),
        new CharacterRecord(6, 12, 1, 3, CHAR_NAME_SKELETON),
        new CharacterRecord(4, 4, 2, 4, CHAR_NAME_GHOST),
        new CharacterRecord(30, 3, 4, 1, CHAR_NAME_SLIME)
    };

    // return the maximum health points, damage, health points restored, and bonus spawn reduction
    public int getMaxHp(int level)
    {
        return 3 + level / levelsForHpUpgrade;
    }

    public int getDmg(int level)
    {
        return 1 + level / levelsForAttackUpgrade;
    }

    public int getHpRestored(int level)
    {
        return 1 + level / levelsForHpRegenUpgrade;
    }

    public float getBonusSpawnReduction(int level)
    {
        int bonusSpawnLvl = level / levelsForBonusSpawnUpgrade;
        return bonusSpawnLvl / (30 + bonusSpawnLvl); // 30 enables diminishing returns, x / ( x + 30 ) < 1
    }

}


package com.myjavaproject.swordmystery.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.myjavaproject.swordmystery.SoundManager;

public class Character extends Sprite {
    protected int lives;
    protected float timeAlive;
    private float timeOfDmgTaken;

    public static final float BLINK_TIME_AFTER_DMG = 0.25f;

    public Character(int _lives)
    {
        lives = _lives;
        timeAlive = 0;
        timeOfDmgTaken = -1;
    }


    public int getLives()
    {
        return lives;
    }

    public void preDraw()
    {
        if (timeAlive < timeOfDmgTaken + BLINK_TIME_AFTER_DMG)
        {
            float t = (timeAlive - timeOfDmgTaken) / BLINK_TIME_AFTER_DMG; // 0..1
            t = t * t;
            setColor(1, 1, 1, t);
        }
    }

    public void postDraw()
    {
        setColor(1, 1, 1, 1);
    }


    public void takeDamage(int amount) {
        SoundManager.PlaySwingSound();
        timeOfDmgTaken = timeAlive;
        lives -= amount;
        if (lives < 0)
        {
            lives = 0;
        }
    }

    public void update(float delta)
    {
        timeAlive += delta;
    }

    public float getTimeOfDmgTaken()
    {
        return timeOfDmgTaken;
    }

    public float getTimeAlive()
    {
        return timeAlive;
    }
}



package com.myjavaproject.swordmystery.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.myjavaproject.swordmystery.Resources;
import com.myjavaproject.swordmystery.graph.SizeEvaluator;
public class Bonus extends Sprite implements Pool.Poolable {
    public static byte BONUS_TYPE_ATTACK = 0;
    public static byte BONUS_TYPE_HEALTH = 1;
    public static byte BONUS_TYPE_COIN = 2;
    private int fieldX;
    private int fieldY;
    private byte bonusType;

    public Bonus()
    {

    }

    public void setup(int fx, int fy, byte bType, Resources res)
    {
        fieldX = fx;
        fieldY = fy;
        bonusType = bType;
        if (bType == BONUS_TYPE_ATTACK)
        {
            set(res.attackBonus);
        }
        else if (bType == BONUS_TYPE_HEALTH)
        {
            set(res.healthBonus);
        }
        else if (bType == BONUS_TYPE_COIN)
        {
            set(res.coinBonus);
        }
    }


    @Override
    public void reset() {

    }

    static final Pool<Bonus> bonusPool = new Pool<Bonus>() {
        @Override
        protected Bonus newObject() {
            return new Bonus();
        }
    };

    public void release()
    {
        bonusPool.free(this);
    }

    public static Bonus Create(int fx, int fy, byte bType, Resources res)
    {
        Bonus bonus = bonusPool.obtain();
        bonus.setup(fx, fy, bType, res);
        return bonus;
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        setPosition(sizeEvaluator.getBaseScreenX(fieldX),
                sizeEvaluator.getBaseScreenY(fieldY));
        super.draw(batch);
    }

    public int getFieldX()
    {
        return fieldX;
    }

    public int getFieldY()
    {
        return fieldY;
    }

    public byte getBonusType()
    {
        return bonusType;
    }

}



package com.myjavaproject.swordmystery.graph;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.myjavaproject.swordmystery.Resources;


public class SizeEvaluator {
    private Stage measuredStage;
    private Resources resources;
    public static final int BASE_MARGIN = 3;
    // 4x4
    private final int maxTileBaseX;
    private final int maxTileBaseY;
    private float rightSideX;

    public SizeEvaluator(Stage _stage,
                         Resources _res,
                         int maxBaseX,
                         int maxBaseY,
                         float _rightSideX)
    {
        measuredStage = _stage;
        resources = _res;
        maxTileBaseX = maxBaseX;
        maxTileBaseY = maxBaseY;
        rightSideX = _rightSideX;
    }

    // 4x4
    // x (0 -> 3), 0 at the left, maxTileBaseX at the right
    // y (0 -> 3), 0 at the bottom, maxTileBaseY at the top

    public float getBaseScreenX(int baseX) // 0 .. 3
    {
        return measuredStage.getWidth() / 2
                - (resources.TILE_SIZE + BASE_MARGIN)
                * (maxTileBaseX + 1 - baseX);
        // TILE SIZE = 16 px;
        // baseX = 0
    }

    public float getBaseScreenY(int baseY)
    {
        return measuredStage.getHeight() / 2
                - ((resources.TILE_SIZE + BASE_MARGIN) * 2 / 3)
                * ((maxTileBaseY + 1) / 2 - baseY);
    }

    public float getEnemyX(Sprite enemy)
    {
        return (measuredStage.getWidth() * 3 / 4) - enemy.getWidth() / 2;
    }

    public float getEnemyY(Sprite enemy)
    {
        return measuredStage.getHeight() / 2 - enemy.getHeight() / 2;
    }

    public void setRightSideX(float value)
    {
        rightSideX = value;
    }

    public float getRightSideX() {
        return rightSideX;
    }
}




package com.myjavaproject.swordmystery.graph;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.myjavaproject.swordmystery.Resources;


public class Background {
    public Background()
    {

    }
    public void draw(Stage stage, Resources res) //make a floor background with a wall behind. stage is the stage that the background is drawn on. res is the resources class.
    {
        stage.getBatch().begin(); //start drawing the background and wall.

        for (int y = 0; y <= stage.getHeight(); y += Resources.TILE_SIZE) //stage.getHeight() is the height of the stage. stage.getWidth() is the width of the stage.
        {
            for (int x = 0; x <= stage.getWidth(); x += Resources.TILE_SIZE)
            {
                stage.getBatch().draw(res.ground,
                        x, y,
                        0, 0,
                        Resources.TILE_SIZE, Resources.TILE_SIZE,
                        1.01f,
                        1.01f,
                        0);
            }
        }

        for (int x = 0; x <= stage.getWidth(); x += Resources.TILE_SIZE)
        {
            stage.getBatch().draw(res.wall,
                    x, stage.getHeight() - Resources.TILE_SIZE,
                    0, 0,
                    Resources.TILE_SIZE, Resources.TILE_SIZE,
                    1.01f,
                    1.01f,
                    0);
        }

        stage.getBatch().end();
    }
}




package com.myjavaproject.swordmystery.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.myjavaproject.swordmystery.graph.SizeEvaluator;


public abstract class Effect implements Pool.Poolable {
    protected boolean isAlive; //to see if effect is alive
    protected float timeAlive; //protected mean they will be accessible by derived classes
    public Effect() //constructor won't do much apart from setting initial values
    {
        isAlive = false;
        timeAlive = 0;
    }
    @Override
    public void reset() {

    }
    public void init(EffectEngine parent) //this is from the pool to iniatie the effect
    {
        isAlive = true;
        timeAlive = 0;
        parent.add(this); //this refer to the effect that is being created and added to the parent effect engine
    }

    public void update(float delta)//will be responsible for checking if our effect has expired //delta is the time between frames in seconds
    {

        timeAlive += delta; //timeAlive is the time the effect has been alive
    }

    public abstract void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator);
    //Since we do not plan to instantiate the effect class, declare draw function abstract.


    public boolean isAlive() // to check whether effect is alive by adding isAlive method
    {
        return isAlive; //by default it is alive
    }

    public abstract void release(); //incase we need extra disposal
}




package com.myjavaproject.swordmystery.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myjavaproject.swordmystery.graph.SizeEvaluator;

import java.util.ArrayList;
import java.util.List;

public class EffectEngine {

    List<Effect> effects; //composition ,  the EffectEngine class contains a list of Effect objects as a member variable,
    // rather than inheriting from the Effect class. This allows the
    // EffectEngine class to manage a collection of Effect objects without directly inheriting their implementation.


    public EffectEngine()
    {
        effects = new ArrayList<Effect>();
    }
    //Draw will be calling draw() on all existing effects,
    //add will be used to add a new effect to the list,
    //clear will clear all the effects (in case we exit the gamescreen) and
    //update will do the necessary updates

    public void add(Effect effect)
    {
        effects.add(effect);
    }

    public void update(float delta)
    {
        int i = 0;
        while (i < effects.size()) //call the update function of every separate effect.
        {
            effects.get(i).update(delta);
            if (effects.get(i).isAlive()) //check if the effect is alive.
            {
                i++;
            }
            else
            {
                effects.get(i).release();
                effects.remove(i);
            }
        }
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator)
    {
        for (int i = 0; i < effects.size(); i++)
        {
            effects.get(i).draw(batch, sizeEvaluator);
        }
    }

    public void clear()
    {
        while (effects.size() > 0)
        {
            effects.get(0).release();
            effects.remove(0);
        }
    }
    //The idea behind it is to mark the effects reusable (released) so that the next time (in case we restart the GameScreen)
    // our effect pool won’t need to create any new elements, but reuse the old ones.
    //the point of reusing is to avoid creating new objects and to avoid garbage collection (which is a costly operation).
}




package com.myjavaproject.swordmystery.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.myjavaproject.swordmystery.Resources;
import com.myjavaproject.swordmystery.graph.SizeEvaluator;

public class WarningEffect extends Effect {

    private static final float WARNING_TIME = 0.75f;
    private int fieldX;
    private int fieldY;
    private Resources resources;

    public interface WarningEffectListener
    {
        void OnEffectOver(WarningEffect effect);
    };

    private WarningEffectListener listener;

    public static WarningEffect Create(int fx,
                                       int fy,
                                       EffectEngine engine,
                                       Resources res,
                                       WarningEffectListener _listener) //returning a new effect instance and setting the base value
    {
        WarningEffect effect = warningPool.obtain();
        effect.init(fx, fy, engine, res, _listener);
        return effect;
    }

    public WarningEffect()//Create a constructor that won’t do anything but call super(), constructor of the parent
    {

    }

    public void init(int fx,
                     int fy,
                     EffectEngine parent,
                     Resources res,
                     WarningEffectListener _listener)
    {
        listener = _listener;
        fieldX = fx;
        fieldY = fy;
        resources = res;
        super.init(parent);
    }

    @Override
    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        batch.begin();
        batch.draw(resources.warning,
                sizeEvaluator.getBaseScreenX(fieldX),
                sizeEvaluator.getBaseScreenY(fieldY));
        batch.end();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);//call the update method of the parent class which is Effect class and pass the delta value
        if (timeAlive > WARNING_TIME && isAlive) //if the timeAlive is greater than the WARNING_TIME and isAlive is true
        {
            isAlive = false;
            if (listener != null)
            {
                listener.OnEffectOver(this);
            }
        }
    }

    public int getFieldX()
    {
        return fieldX;
    }

    public int getFieldY()
    {
        return fieldY;
    }

    @Override
    public void release() {
        warningPool.free(this); //indicate that the current object is available for reuse
    }



    //since every type of effect requires it’s own pool, I make a static Pool in every separate class.
    static Pool<WarningEffect> warningPool = new Pool<WarningEffect>() {
        @Override
        protected WarningEffect newObject() {
            return new WarningEffect();
        }
    };
}

