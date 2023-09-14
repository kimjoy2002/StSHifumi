package BlueArchive_Hifumi.cardmods;

import BlueArchive_Hifumi.cards.AbstractDynamicCard;
import BlueArchive_Hifumi.cards.CarWash;
import BlueArchive_Hifumi.cards.EvolvingPeroro;
import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

public class WashMod extends AbstractCardModifier {
    public static String ID = "BlueArchive_Hifumi:WashModifier";
    public static final String[] TEXT;
    public int amount;
    public WashMod(int amount) {
        this.amount = amount;
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + StringUtils.capitalize(TEXT[0]) + (amount+1) + StringUtils.capitalize(TEXT[1]);
    }
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }
    public boolean shouldApply(AbstractCard card) {
        return card.cost != -2;
    }

    public void onInitialApplication(AbstractCard card) {
    }
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.purgeOnUse && this.amount > 0) {
            for(int i = 0 ; i < amount; i++) {
                AbstractMonster m = null;
                if (target != null) {
                    m = (AbstractMonster)target;
                }

                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }

                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);

            }
        }
    }

    public void onRemove(AbstractCard card) {

    }

    public AbstractCardModifier makeCopy() {
        return new WashMod(amount);
    }

    public String identifier(AbstractCard card) {
        return ID;
    }

    public Color getGlow(AbstractCard card) {
        return Color.GOLD.cpy();
    }

    static {
        TEXT = CarWash.cardStrings.EXTENDED_DESCRIPTION;
    }
}

