package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.cardmods.WashMod;
import BlueArchive_Hifumi.cards.Wanted;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.powers.ThornsPower;
import javassist.CtBehavior;

public class CardGroupPatch {

    @SpirePatch(
            clz = CardGroup.class,
            method = "getPurgeableCards"
    )
    public static class GetPurgeablePatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"c"}

        )
        public static void Insert(CardGroup __instance, @ByRef AbstractCard[] retVal) {
            if(retVal != null && retVal.length > 0) {
                if(retVal[0] instanceof Wanted) {
                    retVal[0] = new Necronomicurse();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(String.class, "equals");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
