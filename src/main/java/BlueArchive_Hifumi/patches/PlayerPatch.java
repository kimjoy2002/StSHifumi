package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.cards.PerfectCollector;
import BlueArchive_Hifumi.cards.PeroroOmamori;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

public class PlayerPatch {

    static public boolean hasPerfectCollecter = false;
    @SpirePatch(
            clz = AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class PlayerPatcher {
        public static SpireField<Integer> omamoriVal = new SpireField<>(() -> 0);
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class PreBattlePrepPatcher {
        public static void Postfix(AbstractPlayer __instance) {
            PlayerPatcher.omamoriVal.set(__instance, 0);
            hasPerfectCollecter = false;
            for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if(c instanceof PerfectCollector) {
                    hasPerfectCollecter = true;
                }
            }
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onCardDrawOrDiscard"
    )
    public static class OnCardDrawOrDiscardPatcher {
        public static void Postfix(AbstractPlayer __instance) {
            int currentVal = 0;
            for(AbstractCard card : __instance.discardPile.group) {
                if(card instanceof PeroroOmamori) {
                    currentVal += card.magicNumber;
                }
            }
            if(PlayerPatcher.omamoriVal.get(__instance) != currentVal)  {
                int magicNumber = currentVal - PlayerPatcher.omamoriVal.get(__instance);
                PlayerPatcher.omamoriVal.set(__instance, currentVal);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, magicNumber), magicNumber));
            }
        }
    }
}
