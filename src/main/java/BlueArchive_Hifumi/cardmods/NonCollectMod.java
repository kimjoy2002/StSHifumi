package BlueArchive_Hifumi.cardmods;

import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.commons.lang3.StringUtils;

public class NonCollectMod extends AbstractCardModifier {
    public static String ID = "BlueArchive_Hifumi:NonCollectModifier";
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public NonCollectMod() {
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return StringUtils.capitalize(TEXT[3]) + (Settings.lineBreakViaCharacter ? " " : "") + LocalizedStrings.PERIOD + " NL " + rawDescription;
    }

    public boolean shouldApply(AbstractCard card) {
        return !card.hasTag(EnumPatch.NONCOLLECTABLE);
    }

    public void onInitialApplication(AbstractCard card) {

        card.tags.add(EnumPatch.NONCOLLECTABLE);
    }

    public void onRemove(AbstractCard card) {
        card.tags.remove(EnumPatch.NONCOLLECTABLE);
    }

    public AbstractCardModifier makeCopy() {
        return new NonCollectMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }


    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:SpadeworkAction");
        TEXT = uiStrings.TEXT;
    }
}

