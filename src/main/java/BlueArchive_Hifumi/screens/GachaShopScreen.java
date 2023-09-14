package BlueArchive_Hifumi.screens;

import BlueArchive_Hifumi.relics.peroro.*;
import BlueArchive_Hifumi.shop.PeroroStoreRelic;
import BlueArchive_Hifumi.shop.StoreGachaPon;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;
import com.megacrit.cardcrawl.vfx.FloatyEffect;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.overlayMenu;

public class GachaShopScreen extends CustomScreen {
    private static final Logger logger = LogManager.getLogger(GachaShopScreen.class.getName());
    private static final CharacterStrings characterStrings;
    public static final String[] NAMES;
    public static final String[] TEXT;
    private static Texture rugImg;
    private static Texture handImg;
    private ArrayList<String> idleMessages;
    private ArrayList<PeroroStoreRelic> relics;
    private ArrayList<StoreGachaPon> gachapons;
    private float speechTimer;
    private float rugY;
    private static final float RUG_SPEED = 5.0F;
    private static final float DRAW_START_X;
    private static final float TOP_ROW_Y;
    private static final float BOTTOM_ROW_Y;
    private static final float SPEECH_TEXT_R_X;
    private static final float SPEECH_TEXT_L_X;
    private static final float SPEECH_TEXT_Y;
    private static final float GOLD_IMG_WIDTH;
    private static final float GOLD_IMG_OFFSET_X;
    private static final float GOLD_IMG_OFFSET_Y;
    private static final float PRICE_TEXT_OFFSET_X;
    private static final float PRICE_TEXT_OFFSET_Y;
    private static final String WELCOME_MSG;
    private ShopSpeechBubble speechBubble;
    private SpeechTextEffect dialogTextEffect;
    private float handTimer;
    private float handX;
    private float handY;
    private float handTargetX;
    private float handTargetY;
    private boolean saidWelcome;
    private boolean somethingHovered;
    private static final float HAND_SPEED = 6.0F;
    private static float HAND_W;
    private static float HAND_H;
    private FloatyEffect f_effect;
    private float notHoveredTimer;
    public ConfirmButton confirmButton;
    public PeroroStoreRelic touchRelic;
    public StoreGachaPon touchGacha;
    public static class Enum
    {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen GACHA_SHOP;
    }

    public GachaShopScreen() {
        this.rugY = (float) Settings.HEIGHT / 2.0F + 540.0F * Settings.yScale;
        this.speechTimer = 0.0F;
        this.speechBubble = null;
        this.dialogTextEffect = null;
        this.idleMessages = new ArrayList();
        this.saidWelcome = false;
        this.somethingHovered = false;
        this.relics = new ArrayList();
        this.gachapons = new ArrayList();
        this.f_effect = new FloatyEffect(20.0F, 0.1F);
        this.handTimer = 1.0F;
        this.handX = (float)Settings.WIDTH / 2.0F;
        this.handY = (float)Settings.HEIGHT;
        this.handTargetX = 0.0F;
        this.handTargetY = (float)Settings.HEIGHT;
        this.notHoveredTimer = 0.0F;
        this.confirmButton = new ConfirmButton();
        this.touchRelic = null;
        this.touchGacha = null;
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen()
    {
        return Enum.GACHA_SHOP;
    }


    public void init() {
        relics.clear();
        gachapons.clear();
        this.idleMessages.clear();
        if (AbstractDungeon.id.equals("TheEnding")) {
            Collections.addAll(this.idleMessages, Merchant.ENDING_TEXT);
        } else {
            Collections.addAll(this.idleMessages, TEXT);
        }

        if (rugImg == null) {
            switch (Settings.language) {
                case DEU:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/deu.png");
                    break;
                case EPO:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/epo.png");
                    break;
                case FIN:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/fin.png");
                    break;
                case FRA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/fra.png");
                    break;
                case ITA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ita.png");
                    break;
                case JPN:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/jpn.png");
                    break;
                case KOR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/kor.png");
                    break;
                case RUS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/rus.png");
                    break;
                case THA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/tha.png");
                    break;
                case UKR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ukr.png");
                    break;
                case ZHS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/zhs.png");
                    break;
                default:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/eng.png");
            }

            handImg = ImageMaster.loadImage("images/npcs/merchantHand.png");
        }

        HAND_W = (float)handImg.getWidth() * Settings.scale;
        HAND_H = (float)handImg.getHeight() * Settings.scale;

        initGoods();
    }


    public static String getCantBuyMsg() {
        ArrayList<String> list = new ArrayList();
        list.add(NAMES[1]);
        list.add(NAMES[2]);
        list.add(NAMES[3]);
        list.add(NAMES[4]);
        list.add(NAMES[5]);
        list.add(NAMES[6]);
        return (String)list.get(MathUtils.random(list.size() - 1));
    }

    public static String getBuyMsg() {
        ArrayList<String> list = new ArrayList();
        list.add(NAMES[7]);
        list.add(NAMES[8]);
        list.add(NAMES[9]);
        list.add(NAMES[10]);
        list.add(NAMES[11]);
        return (String)list.get(MathUtils.random(list.size() - 1));
    }

    private void resetTouchscreenVars() {
        if (Settings.isTouchScreen) {
            this.confirmButton.hide();
            this.confirmButton.isDisabled = false;
            this.touchRelic = null;
        }

    }
    private void open()
    {

        reopen();
    }

    public static AbstractRelic getRandomCommonPeroro(AbstractRelic except) {
        ArrayList<AbstractRelic> list = new ArrayList();
        if(!(except instanceof CommonPeroroDamageRelic))
            list.add(new CommonPeroroDamageRelic());
        if(!(except instanceof CommonPeroroMultiDamageRelic))
            list.add(new CommonPeroroMultiDamageRelic());
        if(!(except instanceof CommonPeroroBlockRelic))
            list.add(new CommonPeroroBlockRelic());
        return (AbstractRelic)list.get(AbstractDungeon.merchantRng.random(list.size() - 1));
    }
    public static AbstractRelic getRandomRarePeroro() {
        ArrayList<AbstractRelic> list = new ArrayList();
        list.add(new RarePeroroDamageRelic());
        list.add(new RarePeroroBlockRelic());
        list.add(new RarePeroroEnergyRelic());
        return (AbstractRelic)list.get(AbstractDungeon.merchantRng.random(list.size() - 1));
    }
    private void initGoods() {
        this.relics.clear();
        this.relics = new ArrayList();
        AbstractRelic firstCommon = getRandomCommonPeroro(null);
        relics.add(new PeroroStoreRelic(firstCommon, 150, 0, this));
        relics.add(new PeroroStoreRelic(getRandomCommonPeroro(firstCommon), 150, 1, this));
        {
            ArrayList<AbstractRelic> cost = new ArrayList();
            cost.add(new CommonPeroroBlockRelic());
            cost.add(new CommonPeroroDamageRelic());
            relics.add(new PeroroStoreRelic(new UncommonPeroroBlockRelic(), cost, 2, this));
        }

        {
            ArrayList<AbstractRelic> cost = new ArrayList();
            cost.add(new CommonPeroroDamageRelic());
            cost.add(new CommonPeroroMultiDamageRelic());
            relics.add(new PeroroStoreRelic(new UncommonPeroroDamageRelic(), cost, 3, this));
        }
        {
            ArrayList<AbstractRelic> cost = new ArrayList();
            cost.add(new CommonPeroroMultiDamageRelic());
            cost.add(new CommonPeroroBlockRelic());
            relics.add(new PeroroStoreRelic(new UncommonPeroroSpecialRelic(), cost, 4, this));
        }
        {
            AbstractRelic relic = getRandomRarePeroro();
            ArrayList<AbstractRelic> cost = new ArrayList();
            if(!(relic instanceof RarePeroroDamageRelic))
                cost.add(new UncommonPeroroBlockRelic());
            if(!(relic instanceof RarePeroroEnergyRelic))
                cost.add(new UncommonPeroroDamageRelic());
            if(!(relic instanceof RarePeroroBlockRelic))
                cost.add(new UncommonPeroroSpecialRelic());

            relics.add(new PeroroStoreRelic(relic, cost, 5, this));
        }


        {
            ArrayList<AbstractRelic> relic = new ArrayList();
            relic.add(new CommonPeroroBlockRelic());
            relic.add(new CommonPeroroDamageRelic());
            relic.add(new CommonPeroroMultiDamageRelic());
            gachapons.add(new StoreGachaPon(relic, 0, AbstractRelic.RelicTier.COMMON, this));
        }
        {
            ArrayList<AbstractRelic> relic = new ArrayList();
            relic.add(new CommonPeroroBlockRelic());
            relic.add(new CommonPeroroDamageRelic());
            relic.add(new CommonPeroroMultiDamageRelic());
            gachapons.add(new StoreGachaPon(relic, 1, AbstractRelic.RelicTier.UNCOMMON, this));
        }
        {
            ArrayList<AbstractRelic> relic = new ArrayList();
            relic.add(new RarePeroroDamageRelic());
            relic.add(new RarePeroroEnergyRelic());
            relic.add(new RarePeroroBlockRelic());
            gachapons.add(new StoreGachaPon(relic, 2, AbstractRelic.RelicTier.RARE, this));
        }
    }

    @Override
    public void reopen()
    {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE)
            AbstractDungeon.previousScreen = AbstractDungeon.screen;

        this.resetTouchscreenVars();
        CardCrawlGame.sound.play("SHOP_OPEN");
        //this.setStartingCardPositions();

        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
        overlayMenu.showBlackScreen();
        overlayMenu.proceedButton.hide();
        overlayMenu.cancelButton.show(NAMES[12]);

        this.rugY = (float)Settings.HEIGHT;
        this.handX = (float)Settings.WIDTH / 2.0F;
        this.handY = (float)Settings.HEIGHT;
        this.handTargetX = this.handX;
        this.handTargetY = this.handY;
        this.handTimer = 1.0F;
        this.speechTimer = 1.5F;
        this.speechBubble = null;
        this.dialogTextEffect = null;
    }

    @Override
    public void close() {
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
        CardCrawlGame.sound.play("SHOP_CLOSE");
        genericScreenOverlayReset();
        overlayMenu.cancelButton.hide();
    }

    @Override
    public void update() {

        if (Settings.isTouchScreen) {
            this.confirmButton.update();
            if (InputHelper.justClickedLeft && this.confirmButton.hb.hovered) {
                this.confirmButton.hb.clickStarted = true;
            }

            if (InputHelper.justReleasedClickLeft && !this.confirmButton.hb.hovered) {
                this.resetTouchscreenVars();
            } else if (this.confirmButton.hb.clicked) {
                this.confirmButton.hb.clicked = false;
                if (this.touchRelic != null) {
                    this.touchRelic.purchaseRelic();
                } else if(this.touchGacha != null) {
                    this.touchGacha.purchaseGacha();
                }

                this.resetTouchscreenVars();
            }
        }

        if (this.handTimer != 0.0F) {
            this.handTimer -= Gdx.graphics.getDeltaTime();
            if (this.handTimer < 0.0F) {
                this.handTimer = 0.0F;
            }
        }

        this.f_effect.update();
        this.somethingHovered = false;
        this.updateControllerInput();
        this.updateRelics();
        this.updateGachapons();
        this.updateRug();
        this.updateSpeech();
        this.updateHand();
        if (!this.somethingHovered) {
            this.notHoveredTimer += Gdx.graphics.getDeltaTime();
            if (this.notHoveredTimer > 1.0F) {
                this.handTargetY = (float)Settings.HEIGHT;
            }
        } else {
            this.notHoveredTimer = 0.0F;
        }
    }

    private void updateControllerInput() {

    }

    private void updateRelics() {
        Iterator<PeroroStoreRelic> i = this.relics.iterator();

        while(i.hasNext()) {
            PeroroStoreRelic r = (PeroroStoreRelic)i.next();
            if (Settings.isFourByThree) {
                r.update(this.rugY + 50.0F * Settings.yScale);
            } else {
                r.update(this.rugY);
            }
            if (r.isPurchased) {
                i.remove();
                break;
            }
        }

    }
    private void updateGachapons() {
        Iterator<StoreGachaPon> i = this.gachapons.iterator();

        while(i.hasNext()) {
            StoreGachaPon r = (StoreGachaPon)i.next();
            if (Settings.isFourByThree) {
                r.update(this.rugY + 50.0F * Settings.yScale);
            } else {
                r.update(this.rugY);
            }
        }

    }
    private void updateRug() {
        if (this.rugY != 0.0F) {
            this.rugY = MathUtils.lerp(this.rugY, (float)Settings.HEIGHT / 2.0F - 540.0F * Settings.yScale, Gdx.graphics.getDeltaTime() * 5.0F);
            if (Math.abs(this.rugY - 0.0F) < 0.5F) {
                this.rugY = 0.0F;
            }
        }

    }

    public void moveHand(float x, float y) {
        this.handTargetX = x - 50.0F * Settings.xScale;
        this.handTargetY = y + 90.0F * Settings.yScale;
    }
    private void updateHand() {
        if (this.handTimer == 0.0F) {
            if (this.handX != this.handTargetX) {
                this.handX = MathUtils.lerp(this.handX, this.handTargetX, Gdx.graphics.getDeltaTime() * 6.0F);
            }

            if (this.handY != this.handTargetY) {
                if (this.handY > this.handTargetY) {
                    this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * 6.0F);
                } else {
                    this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * 6.0F / 4.0F);
                }
            }
        }

    }
    private void updateSpeech() {
        if (this.speechBubble != null) {
            this.speechBubble.update();
            if (this.speechBubble.hb.hovered && this.speechBubble.duration > 0.3F) {
                this.speechBubble.duration = 0.3F;
                this.dialogTextEffect.duration = 0.3F;
            }

            if (this.speechBubble.isDone) {
                this.speechBubble = null;
            }
        }

        if (this.dialogTextEffect != null) {
            this.dialogTextEffect.update();
            if (this.dialogTextEffect.isDone) {
                this.dialogTextEffect = null;
            }
        }

        this.speechTimer -= Gdx.graphics.getDeltaTime();
        if (this.speechBubble == null && this.dialogTextEffect == null && this.speechTimer <= 0.0F) {
            this.speechTimer = MathUtils.random(40.0F, 60.0F);
            if (!this.saidWelcome) {
                this.createSpeech(WELCOME_MSG);
                this.saidWelcome = true;
                this.welcomeSfx();
            } else {
                this.playMiscSfx();
                this.createSpeech(this.getIdleMsg());
            }
        }

    }
    public void createSpeech(String msg) {
        boolean isRight = MathUtils.randomBoolean();
        float x = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
        float y = (float)Settings.HEIGHT - 380.0F * Settings.scale;
        this.speechBubble = new ShopSpeechBubble(x, y, 4.0F, msg, isRight);
        float offset_x = 0.0F;
        if (isRight) {
            offset_x = SPEECH_TEXT_R_X;
        } else {
            offset_x = SPEECH_TEXT_L_X;
        }

        this.dialogTextEffect = new SpeechTextEffect(x + offset_x, y + SPEECH_TEXT_Y, 4.0F, msg, DialogWord.AppearEffect.BUMP_IN);
    }
    private void welcomeSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_3A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_3B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_3C");
        }

    }
    private void playMiscSfx() {
        int roll = MathUtils.random(5);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_MA");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_MB");
        } else if (roll == 2) {
            CardCrawlGame.sound.play("VO_MERCHANT_MC");
        } else if (roll == 3) {
            CardCrawlGame.sound.play("VO_MERCHANT_3A");
        } else if (roll == 4) {
            CardCrawlGame.sound.play("VO_MERCHANT_3B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_3C");
        }

    }

    public void playBuySfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_KA");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_KB");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_KC");
        }

    }

    public void playCantBuySfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_2A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_2B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_2C");
        }

    }

    private String getIdleMsg() {
        return (String)this.idleMessages.get(MathUtils.random(this.idleMessages.size() - 1));
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(rugImg, 0.0F, this.rugY, (float) Settings.WIDTH, (float)Settings.HEIGHT);
        renderRelics(sb);
        renderGacahpons(sb);
        sb.draw(handImg, this.handX + this.f_effect.x, this.handY + this.f_effect.y, HAND_W, HAND_H);
        if (this.speechBubble != null) {
            this.speechBubble.render(sb);
        }

        if (this.dialogTextEffect != null) {
            this.dialogTextEffect.render(sb);
        }

        if (Settings.isTouchScreen) {
            this.confirmButton.render(sb);
        }
    }

    private void renderRelics(SpriteBatch sb) {
        Iterator var2 = this.relics.iterator();

        while(var2.hasNext()) {
            PeroroStoreRelic r = (PeroroStoreRelic)var2.next();
            r.render(sb);
        }
    }

    @Override
    public boolean allowOpenMap() {
        return true;
    }
    @Override
    public boolean allowOpenDeck() {
        return true;
    }

    private void renderGacahpons(SpriteBatch sb) {
        Iterator var2 = this.gachapons.iterator();

        while(var2.hasNext()) {
            StoreGachaPon r = (StoreGachaPon)var2.next();
            r.render(sb);
        }
    }

    @Override
    public void openingSettings()
    {
        AbstractDungeon.previousScreen = curScreen();
    }

    static {
        characterStrings = CardCrawlGame.languagePack.getCharacterString("Shop Screen");
        NAMES = characterStrings.NAMES;
        TEXT = characterStrings.TEXT;
        rugImg = null;
        handImg = null;
        DRAW_START_X = (float)Settings.WIDTH * 0.16F;
        TOP_ROW_Y = 760.0F * Settings.yScale;
        BOTTOM_ROW_Y = 337.0F * Settings.yScale;
        SPEECH_TEXT_R_X = 164.0F * Settings.scale;
        SPEECH_TEXT_L_X = -166.0F * Settings.scale;
        SPEECH_TEXT_Y = 126.0F * Settings.scale;
        WELCOME_MSG = NAMES[0];
        GOLD_IMG_WIDTH = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;
        GOLD_IMG_OFFSET_X = -50.0F * Settings.scale;
        GOLD_IMG_OFFSET_Y = -215.0F * Settings.scale;
        PRICE_TEXT_OFFSET_X = 16.0F * Settings.scale;
        PRICE_TEXT_OFFSET_Y = -180.0F * Settings.scale;
    }
}