package BlueArchive_Hifumi.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;

public class TimeOnTargetEffect extends AbstractGameEffect {
    private int cannonCount;
    private boolean flipped = false;

    public TimeOnTargetEffect(int frostCount, boolean flipped) {
        this.cannonCount = 5 + frostCount;
        this.flipped = flipped;
        if (this.cannonCount > 50) {
            this.cannonCount = 50;
        }

    }

    public void update() {
        CardCrawlGame.sound.playA("SCENE_TORCH_EXTINGUISH", -0.25F - (float)this.cannonCount / 200.0F);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);
        AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.RED));

        for(int i = 0; i < this.cannonCount; ++i) {
            AbstractDungeon.effectsQueue.add(new FallingFireEffect(this.cannonCount, this.flipped));
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
