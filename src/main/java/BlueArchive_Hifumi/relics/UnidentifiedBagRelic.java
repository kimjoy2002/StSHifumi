package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.CollectAction;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class UnidentifiedBagRelic extends CustomRelic implements BlockRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("UnidentifiedBagRelic");

    public static final int AMOUNT = 4;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("unidentified_bag.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("unidentified_bag.png"));

    public UnidentifiedBagRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }


    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if(card.hasTag(EnumPatch.BURIED))
            blockAmount += (float)AMOUNT;
        return blockAmount;
    }

    @Override
    public float atDamageModify(float damage, AbstractCard card) {
        return card.hasTag(EnumPatch.BURIED) ? damage + (float)AMOUNT : damage;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1] ;
    }
}
