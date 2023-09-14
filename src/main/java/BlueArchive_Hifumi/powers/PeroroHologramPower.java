package BlueArchive_Hifumi.powers;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hifumi.DefaultMod.*;

public class PeroroHologramPower extends AbstractPower implements CloneablePowerInterface, RenderPower {
    public AbstractCreature source;
    private static final Texture PERORO_IMAGE =  TextureLoader.getTexture(getModID() + "Resources/images/char/other/Peroro.png");

    private static final Texture PEROROHOLOGRAM_IMAGE =  TextureLoader.getTexture(getModID() + "Resources/images/char/other/Hologram.png");

    public static final String POWER_ID = DefaultMod.makeID("PeroroHologramPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("PeroroHologramPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("PeroroHologramPower32.png"));

    Color color;
    Color color2;
    float scale;
    float duration;
    float duration2;
    public PeroroHologramPower(final int amount) {
        name = NAME;
        ID = POWER_ID;
        color = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        color2 = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        scale = 1.0f;
        duration = 1.0f;
        duration2 = 0.0f;

        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.source = AbstractDungeon.player;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.flash();
        this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
    }


    public void update(int slot) {
        super.update(slot);
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.duration = 0.0F;
        }
        duration2 += Gdx.graphics.getDeltaTime()*0.2f;
        while (this.duration2 > 1.0F) {
            this.duration2 -= 1.0F;
        }
        this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, 1.0f- this.duration);
        this.color2.a = Interpolation.linear.apply(0.6F, 0.8F, this.duration2);

    }
    public void render(SpriteBatch sb) {
        sb.setColor(color2);
        sb.setBlendFunction(770, 1);
        sb.draw(PERORO_IMAGE, AbstractDungeon.player.hb.x - (float) PERORO_IMAGE.getWidth() / 2.0F + 350.0f * Settings.scale, AbstractDungeon.player.hb.y,
                (float) PERORO_IMAGE.getWidth() / 2.0F, 0,
                (float) PERORO_IMAGE.getWidth(), (float) PERORO_IMAGE.getHeight(),
                scale, scale, 0, 0, 0,  PERORO_IMAGE.getWidth(), PERORO_IMAGE.getHeight(), true, false);

        sb.setColor(color);
        sb.setBlendFunction(770, 771);
        sb.draw(PEROROHOLOGRAM_IMAGE, AbstractDungeon.player.hb.x - (float) PEROROHOLOGRAM_IMAGE.getWidth() / 2.0F + 350.0f * Settings.scale, AbstractDungeon.player.hb.y,
                (float) PEROROHOLOGRAM_IMAGE.getWidth() / 2.0F, 0,
                (float) PEROROHOLOGRAM_IMAGE.getWidth(), (float) PEROROHOLOGRAM_IMAGE.getHeight(),
                scale, scale, 0, 0, 0,  PEROROHOLOGRAM_IMAGE.getWidth(), PEROROHOLOGRAM_IMAGE.getHeight(), true, false);

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PeroroHologramPower(amount);
    }
}
