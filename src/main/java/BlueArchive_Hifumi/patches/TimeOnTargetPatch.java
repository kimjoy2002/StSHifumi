package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.actions.TimeOnTargetAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

import java.util.ArrayList;

public class TimeOnTargetPatch {


    @SpirePatch(
            clz = AbstractRoom.class,
            method = "applyEndOfTurnPreCardPowers"
    )
    public static class PreBattlePrepPatcher {
        public static void Postfix(AbstractRoom __instance) {
            AbstractDungeon.actionManager.addToBottom(new TimeOnTargetAction());
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class GetNextActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"c"}

        )
        public static void Insert(GameActionManager __instance, @ByRef AbstractCard[] c) {
             if(c != null && c[0].hasTag(EnumPatch.CANNON) && c[0].dontTriggerOnUseCard)
                 c[0].target = AbstractCard.CardTarget.SELF;
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "freeToPlay");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
