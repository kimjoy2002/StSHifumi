package BlueArchive_Hifumi.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic.obtainCommonPeroroGoods;

public class ObtainRelicEffect extends AbstractGameEffect {

    public ObtainRelicEffect() {
    }

    public void update() {
        obtainCommonPeroroGoods();
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    public void dispose() {
    }
}