package BlueArchive_Hifumi.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic.obtainCommonPeroroGoods;

public class LostRelicEffect extends AbstractGameEffect {
    String relic_id;
    public LostRelicEffect(String relic_id) {
        this.relic_id= relic_id;
    }

    public void update() {
        AbstractDungeon.player.loseRelic(relic_id);
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    public void dispose() {
    }
}