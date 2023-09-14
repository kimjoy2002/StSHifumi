package BlueArchive_Hifumi.cardmods;

import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.KeywordStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.commons.lang3.StringUtils;

public class BuriedMod extends AbstractCardModifier {
    public static String ID = "BlueArchive_Hifumi:BuriedModifier";
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public BuriedMod() {
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return StringUtils.capitalize(TEXT[1]) + (Settings.lineBreakViaCharacter ? " " : "") + LocalizedStrings.PERIOD + " NL " + rawDescription;
    }

    public boolean shouldApply(AbstractCard card) {
        return !card.hasTag(EnumPatch.BURIED);
    }

    public void onInitialApplication(AbstractCard card) {

        card.tags.add(EnumPatch.BURIED);
    }

    public void onRemove(AbstractCard card) {
        card.tags.remove(EnumPatch.BURIED);
    }

    public AbstractCardModifier makeCopy() {
        return new BuriedMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }


    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:SpadeworkAction");
        TEXT = uiStrings.TEXT;
    }
}

