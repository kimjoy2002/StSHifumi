package BlueArchive_Hifumi.vfx;

import BlueArchive_Hifumi.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class VictoryPeroroGoodEffect extends AbstractGameEffect {

    // Settings

    // Textures.
    private static final ArrayList<Texture> peroroGoods = new ArrayList<Texture>() {
        {
            add(TextureLoader.getTexture(makeRelicPath("common_peroro_block_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("common_peroro_damage_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("common_peroro_multi_damage_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("uncommon_peroro_block_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("uncommon_peroro_damage_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("uncommon_peroro_special_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("rare_peroro_block_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("rare_peroro_damage_relic.png")));
            add(TextureLoader.getTexture(makeRelicPath("rare_peroro_energy_relic.png")));
        }
    };

    private Texture Texture;
    private Texture FinalTexture;

    // Duration.
    private float startingDuration;
    private float duration;

    // Position
    private float x;
    private float y;

    // Where go
    private float speed;
    private float direction;

    // How move
    private float rotation;
    private float rotation_speed;
    private float flip_speed;
    private float flip_counter;
    private float scale;
    private float totalscale;

    public VictoryPeroroGoodEffect() {
        this(MathUtils.random(0.0F, Settings.WIDTH),Settings.HEIGHT + 40.0F * Settings.scale);
    }

    public VictoryPeroroGoodEffect(float x, float y, float imgScale) {
        this(x, y);
        totalscale = imgScale;
    }

    public VictoryPeroroGoodEffect(float x, float y) {
        this.Texture = peroroGoods.get(MathUtils.random(0,peroroGoods.size()-1));

        this.FinalTexture = Texture;
        this.startingDuration = MathUtils.random(10.0F,15.0F);
        this.duration = this.startingDuration;
        this.startingDuration = this.duration;
        this.renderBehind = true;
        this.rotation = MathUtils.random(0.0F, 360.0F);
        this.direction = MathUtils.random(240.0F, 300.0F);
        this.rotation_speed = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        this.flip_speed = MathUtils.random(-0.2F, 0.2F) * Settings.scale;
        this.flip_counter = MathUtils.random(0.0F, 6.4F);
        this.speed = MathUtils.random(50.0F, 150.0F) * Settings.scale;
        this.scale = MathUtils.random(-1.0F,1.0F) * Settings.scale;
        this.totalscale = MathUtils.random(0.7F,1.0F);
        if (MathUtils.random(0,4) == 0 && this.totalscale >= 0.9F)
            this.totalscale = MathUtils.random(0.9F,2F);


        // Location
        this.y = y;
        this.x = x;

        this.color = new Color(1, 1, 1, 1F);
    }



    @Override
    public void render(SpriteBatch sb) {
        this.color.a = this.duration / this.startingDuration;
        sb.setColor(this.color);

        final int w = this.FinalTexture.getWidth();
        final int h = this.FinalTexture.getHeight();
        final int w2 = this.FinalTexture.getWidth();
        final int h2 = this.FinalTexture.getHeight();
        sb.draw(this.FinalTexture, x-w2/2f, y-h2/2f,
                w/2f, h/2f,
                w2, h2,
                this.scale *Settings.scale*this.totalscale, 1.0F*Settings.scale*this.totalscale,
                this.rotation,
                0, 0,
                w2, h2,
                false, false);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update() {
        final float dt = Gdx.graphics.getDeltaTime();

        this.y += MathUtils.sinDeg(this.direction) * this.speed * dt;
        this.x += MathUtils.cosDeg(this.direction) * this.speed * dt;

        this.rotation += this.rotation_speed * dt;
        this.flip_counter += this.flip_speed * dt;

        this.scale = MathUtils.sin(this.flip_counter);

        this.FinalTexture = this.Texture;

        this.duration -= dt;
        if (this.duration < 0.0F || y > Settings.HEIGHT + FinalTexture.getHeight() || y < -FinalTexture.getHeight()) {
            this.isDone = true;
        }
    }
}