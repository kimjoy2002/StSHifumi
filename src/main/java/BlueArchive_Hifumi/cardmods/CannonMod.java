package BlueArchive_Hifumi.cardmods;

import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.commons.lang3.StringUtils;

public class CannonMod extends AbstractCardModifier {
    public static String ID = "BlueArchive_Hifumi:CannonModifier";
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public CannonMod() {
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return StringUtils.capitalize(TEXT[0]) + (Settings.lineBreakViaCharacter ? " " : "") + LocalizedStrings.PERIOD + " NL " + rawDescription;
    }

    public boolean shouldApply(AbstractCard card) {
        return !card.isInnate && !card.hasTag(EnumPatch.CANNON);
    }

    public void onInitialApplication(AbstractCard card) {
        card.tags.add(EnumPatch.CANNON);
    }

    public void onRemove(AbstractCard card) {
        card.tags.remove(EnumPatch.CANNON);
    }

    public AbstractCardModifier makeCopy() {
        return new CannonMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }


    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:CannonModifier");
        TEXT = uiStrings.TEXT;
    }
}

