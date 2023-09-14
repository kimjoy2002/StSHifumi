package BlueArchive_Hifumi.powers;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;

import java.util.Iterator;

import static BlueArchive_Hifumi.DefaultMod.getModID;
import static BlueArchive_Hifumi.DefaultMod.makePowerPath;

public class PerorodzillaPower extends AbstractPower implements RenderPower, CloneablePowerInterface {
    public AbstractCreature source;

    private static final Texture PEORORODZILLA_IMAGE =  TextureLoader.getTexture(getModID() + "Resources/images/char/other/Perorodzilla.png");

    public static final String POWER_ID = DefaultMod.makeID("PerorodzillaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("PerorodzillaPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("PerorodzillaPower32.png"));

    Color color;
    float scale;
    float duration;
    public PerorodzillaPower(final int amount) {
        name = NAME;
        ID = POWER_ID;
        color = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        scale = 0.7f;
        duration = 1.0f;

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
        this.addToBot(new SFXAction("ATTACK_DEFECT_BEAM"));
        this.addToBot(new VFXAction(owner, new SweepingBeamEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractDungeon.player.flipHorizontal), 0.4F));
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature)null, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE, true));
    }

    public void update(int slot) {
        super.update(slot);
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.duration = 0.0F;
        }

        this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, 1.0f- this.duration);

    }
    public void render(SpriteBatch sb) {
        sb.setColor(color);

        sb.draw(PEORORODZILLA_IMAGE, AbstractDungeon.player.hb.x - (float)PEORORODZILLA_IMAGE.getWidth() / 2.0F, AbstractDungeon.player.hb.y,
                (float)PEORORODZILLA_IMAGE.getWidth() / 2.0F, 0,
                (float)PEORORODZILLA_IMAGE.getWidth(), (float)PEORORODZILLA_IMAGE.getHeight(),
                scale, scale, 0, 0, 0,  PEORORODZILLA_IMAGE.getWidth(), PEORORODZILLA_IMAGE.getHeight(), true, false);

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PerorodzillaPower(amount);
    }
}
