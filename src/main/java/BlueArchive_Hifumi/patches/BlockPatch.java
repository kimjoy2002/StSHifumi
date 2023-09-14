package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.cards.RingOfFire;
import BlueArchive_Hifumi.relics.BlockRelic;
import BlueArchive_Hifumi.relics.ReisaRelic;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;

public class BlockPatch {


    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowersToBlock"
    )
    public static class applyBlockCardPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"tmp"}

        )
        public static void Insert(AbstractCard __instance,  @ByRef float[] tmp) {
            if(tmp != null) {
                for(AbstractRelic r : AbstractDungeon.player.relics) {
                    if(r instanceof BlockRelic) {
                        tmp[0] = ((BlockRelic)r).modifyBlock(tmp[0], __instance);
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class RingOfFirePatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"tmp"}

        )
        public static void Insert(AbstractCard __instance,  @ByRef float[] tmp) {
            if(tmp != null && __instance.dontTriggerOnUseCard == false && __instance.type == AbstractCard.CardType.ATTACK && __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
                for(AbstractCard card : AbstractDungeon.player.hand.group) {
                    if(card instanceof RingOfFire) {
                        tmp[0] += card.magicNumber;
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageGive");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class RingOfFireMultiPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"tmp"}

        )
        public static void Insert(AbstractCard __instance,  @ByRef float[][] tmp) {
            if(tmp != null && __instance.dontTriggerOnUseCard == false && __instance.type == AbstractCard.CardType.ATTACK && __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
                for(AbstractCard card : AbstractDungeon.player.hand.group) {
                    if(card instanceof RingOfFire) {
                        for(int i = 0; i < tmp[0].length; ++i) {
                            tmp[0][i] += card.magicNumber;
                        }
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "multiDamage");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage",
            paramtypez= {
                    AbstractMonster.class
            }
    )
    public static class RingOfFire2Patcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"tmp"}

        )
        public static void Insert(AbstractCard __instance, AbstractMonster mo,  @ByRef float[] tmp) {
            if(tmp != null && __instance.dontTriggerOnUseCard == false && __instance.type == AbstractCard.CardType.ATTACK && __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
                for(AbstractCard card : AbstractDungeon.player.hand.group) {
                    if(card instanceof RingOfFire) {
                        tmp[0] += card.magicNumber;
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageGive");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage",
            paramtypez= {
                    AbstractMonster.class
            }
    )
    public static class RingOfFire2MultiPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"tmp"}

        )
        public static void Insert(AbstractCard __instance, AbstractMonster mo, @ByRef float[][] tmp) {
            if(tmp != null && __instance.dontTriggerOnUseCard == false && __instance.type == AbstractCard.CardType.ATTACK && __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
                for(AbstractCard card : AbstractDungeon.player.hand.group) {
                    if(card instanceof RingOfFire) {
                        for(int i = 0; i < tmp[0].length; ++i) {
                            tmp[0][i] += card.magicNumber;
                        }
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "multiDamage");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
