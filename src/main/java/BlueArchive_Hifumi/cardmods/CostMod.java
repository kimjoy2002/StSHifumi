package BlueArchive_Hifumi.cardmods;

import BlueArchive_Hifumi.cards.LuckyPunch;
import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.commons.lang3.StringUtils;

public class CostMod extends AbstractCardModifier {
    public static String ID = "BlueArchive_Hifumi:CostModifier";
    int amtcost;
    public CostMod(int amtcost) {
        this.amtcost = amtcost;
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription;
    }

    public boolean shouldApply(AbstractCard card) {
        return card instanceof LuckyPunch && card.cost-amtcost>=0;
    }

    public void onInitialApplication(AbstractCard card) {
        if(card instanceof LuckyPunch) {
            ((LuckyPunch)card).upgradeBaseCoseLucky(card.cost - amtcost);
        }
    }

    public void onRemove(AbstractCard card) {
        if(card instanceof LuckyPunch) {
            ((LuckyPunch)card).upgradeBaseCoseLucky(card.cost + amtcost);
        }
    }

    public AbstractCardModifier makeCopy() {
        return new CostMod(amtcost);
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}

