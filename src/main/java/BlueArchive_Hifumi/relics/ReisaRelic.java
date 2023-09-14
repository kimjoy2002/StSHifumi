package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.PeroroTicketUncommon;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class ReisaRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ReisaRelic");

    public static final int AMOUNT = 1;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("reisa_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("reisa_relic.png"));

    public ReisaRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }


    public void onEquip() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        ArrayList<AbstractCard> cards = new ArrayList<>();
        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.hasTag(EnumPatch.FRIEND)) {
                cards.add(c);
            }
        }

        for(AbstractCard c : cards) {
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c));
            AbstractDungeon.player.masterDeck.removeCard(c);
        }
        ++AbstractDungeon.player.energy.energyMaster;
    }

    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
