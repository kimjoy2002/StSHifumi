package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.shop.GachaShop;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.cards.colorless.DeepBreath;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import javassist.CtBehavior;

import java.util.Iterator;

public class BuriedCardPatch {
    //이 카드만 덱에 있으면 무한루프가 되지않을까 걱정

    @SpirePatch(
            clz = EmptyDeckShuffleAction.class,
            method=SpirePatch.CLASS
    )
    public static class FieldPatcher {
        public static SpireField<Boolean> forCard = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = EmptyDeckShuffleAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class EmptyDeckShuffleConstructorPatcher {
        public static void Postfix(EmptyDeckShuffleAction __instance) {
            StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
            for(StackTraceElement trace : stackTraces) {
                if(trace.getClassName().equals(DeepBreath.class.getName())){
                    FieldPatcher.forCard.set(__instance, true);
                    break;
                }
            }
        }

    }


    @SpirePatch(
            clz = DrawCardAction.class,
            method = "update"
    )
    public static class DrawCardActionPatch {

        public static SpireReturn Prefix(DrawCardAction __instance) {
            int deckSize = AbstractDungeon.player.drawPile.size();
            int discardSize = 0;
            for(AbstractCard c: AbstractDungeon.player.discardPile.group) {
                if(!c.hasTag(EnumPatch.BURIED)) {
                    discardSize++;
                }

            }
            if (!SoulGroup.isActive()) {
                if (deckSize + discardSize == 0) {
                    __instance.isDone = true;
                    AbstractGameAction followUpAction = (AbstractGameAction) ReflectionHacks.getPrivate(__instance, EmptyDeckShuffleAction.class, "followUpAction");
                    if (followUpAction != null) {
                        AbstractDungeon.actionManager.addToTop(followUpAction);
                    }
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = EmptyDeckShuffleAction.class,
            method = "update"
    )
    public static class BuriedCardShuffle {

        @SpireInsertPatch(
                locator= Locator.class,
                localvars = {"c"}
        )
        public static SpireReturn Insert(EmptyDeckShuffleAction __instance, @ByRef Iterator<AbstractCard>[] c_) {
            if(FieldPatcher.forCard.get(__instance) == true) {
                return SpireReturn.Continue();
            }
            if(c_ != null) {
                Iterator<AbstractCard> test = AbstractDungeon.player.discardPile.group.iterator();
                while(c_[0].hasNext() && test.hasNext()){
                    AbstractCard testCard = (AbstractCard)test.next();
                    if(testCard.hasTag(EnumPatch.BURIED)) {
                        c_[0].next();
                    } else{
                        break;
                    }
                }
                if(c_[0].hasNext()) {
                    return SpireReturn.Continue();
                } else {
                    ReflectionHacks.setPrivate(__instance, EmptyDeckShuffleAction.class, "vfxDone", true);
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(Iterator.class, "next");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }


}
