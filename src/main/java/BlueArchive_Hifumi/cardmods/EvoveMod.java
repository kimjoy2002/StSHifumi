package BlueArchive_Hifumi.cardmods;

import BlueArchive_Hifumi.cards.EvolvingPeroro;
import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.commons.lang3.StringUtils;

public class EvoveMod extends AbstractCardModifier {
    public static String ID = "BlueArchive_Hifumi:EvoveModifier";
    public static final String[] TEXT;
    public enum EvoveEnum {
        ADD_DAMAGE,
        ADD_BLOCK,
        ADD_VULUN,
        ADD_WEAK,
        ADD_MULTI,
        COST_DOWN,
        COST_UP,
        ADD_CANNON,
        ADD_ALL_ENEMY,
        ADD_EXHAUST,
        ADD_DRAW,
        ADD_COLLECT,
        ADD_RETAIN,
        ADD_CURSE,
        ADD_BURIED
    }
    public EvoveEnum type;
    public EvoveMod(EvoveEnum type) {
        this.type = type;
        priority = -1;
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription;
    }

    public boolean shouldApply(AbstractCard card) {
        return card instanceof EvolvingPeroro;
    }

    public void onInitialApplication(AbstractCard card) {
        if(card instanceof EvolvingPeroro) {
            ((EvolvingPeroro)card).applyMod();
        }
        //card.tags.add(EnumPatch.BURIED);
    }

    public void onRemove(AbstractCard card) {
        //card.tags.remove(EnumPatch.BURIED);
    }

    public AbstractCardModifier makeCopy() {
        return new EvoveMod(type);
    }

    public String identifier(AbstractCard card) {
        return ID;
    }


    static {
        TEXT = EvolvingPeroro.cardStrings.EXTENDED_DESCRIPTION;
    }
}

