package BlueArchive_Hifumi.shop;


import BlueArchive_Hifumi.patches.ShopPatch;
import BlueArchive_Hifumi.screens.GachaShopScreen;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static BlueArchive_Hifumi.DefaultMod.getModID;

public class GachaShop implements Disposable {
    private static final CharacterStrings characterStrings;
    public static final String[] NAMES;
    public static final String[] TEXT;
    public static final String[] ENDING_TEXT;


    public static final Color HOVER_WHITE_COLOR = new Color(0.8F, 0.8F, 0.8F, 0.5F);
    public AnimatedNpc anim;
    public static final float DRAW_X;
    public static final float DRAW_Y;
    public Hitbox hb;
    protected float modX;
    protected float modY;
    boolean is_hover = false;
    public static final String GACHA_SKELETON_ATLAS = "BlueArchive_HifumiResources/images/char/other/GachaPon.atlas";
    public static final String GACHA_SKELETON_JSON = "BlueArchive_HifumiResources/images/char/other/GachaPon.json";

    public GachaShop() {
        this(0.0F, 0.0F, 1);
    }

    public GachaShop(float x, float y, int newShopScreen) {
        this.hb = new Hitbox(150.0F * Settings.scale, 250.0F * Settings.scale);
        this.anim = new AnimatedNpc(DRAW_X + 256.0F * Settings.scale, AbstractDungeon.floorY + 30.0F * Settings.scale,
                GACHA_SKELETON_ATLAS,
                GACHA_SKELETON_JSON,
                "idle_animation");


        this.modX = x;
        this.modY = y;
        this.hb.move(DRAW_X + (250.0F + x) * Settings.scale, DRAW_Y + (130.0F + 130 + y) * Settings.scale);
        CustomScreen shop = BaseMod.getCustomScreen(GachaShopScreen.Enum.GACHA_SHOP);
        if(shop instanceof GachaShopScreen) {
            ((GachaShopScreen)shop).init();
        }
    }

    public void update() {
        this.hb.update();
        if ((this.hb.hovered && InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) && !AbstractDungeon.isScreenUp && !AbstractDungeon.isFadingOut && !AbstractDungeon.player.viewingRelics) {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(NAMES[0]);
            BaseMod.openCustomScreen(GachaShopScreen.Enum.GACHA_SHOP);
            this.hb.hovered = false;
        }
    }

    public void setAnimation(String stateName) {
        AnimationState animationState = (AnimationState) ReflectionHacks.getPrivate(this.anim, AnimatedNpc.class, "state");

        animationState.setAnimation(0, stateName, true);
    }


    public void render(SpriteBatch sb) {
        if (this.anim != null) {
            sb.setColor(Color.WHITE);
            if (this.hb.hovered && !is_hover) {
                setAnimation("hover_animation");
                is_hover = true;
            } else if(!this.hb.hovered && is_hover) {
                setAnimation("idle_animation");
                is_hover = false;
            }
            this.anim.render(sb);
        }

        if (Settings.isControllerMode) {
            sb.setColor(Color.WHITE);
            sb.draw(CInputActionSet.select.getKeyImg(), DRAW_X - 32.0F + 150.0F * Settings.scale, DRAW_Y - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        this.hb.render(sb);
    }

    public void dispose() {
        if (this.anim != null) {
            this.anim.dispose();
        }

    }

    static {
        characterStrings = CardCrawlGame.languagePack.getCharacterString("Merchant");
        NAMES = characterStrings.NAMES;
        TEXT = characterStrings.TEXT;
        ENDING_TEXT = characterStrings.OPTIONS;
        DRAW_X = (float)Settings.WIDTH * 0.5F + 34.0F * Settings.xScale-(360.0F+150.0F)/2 * Settings.scale;
        DRAW_Y = AbstractDungeon.floorY - 109.0F * Settings.scale;
    }
}