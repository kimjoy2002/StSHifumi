package BlueArchive_Hifumi.potions;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.UsePeroroGoodsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomPotion;

import static BlueArchive_Hifumi.DefaultMod.DEFAULT_YELLOW;

public class PeroroPotion  extends CustomPotion  {
    public static final String POTION_ID = DefaultMod.makeID("PeroroPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public PeroroPotion() {
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.M, PotionColor.SMOKE);
        this.labOutlineColor = DEFAULT_YELLOW;
        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = false;

    }


    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new UsePeroroGoodsAction(AbstractRelic.RelicTier.SPECIAL,1, false, potency));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new PeroroPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 1;
    }

    public void initializeData() {
        this.potency = this.getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        this.tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
