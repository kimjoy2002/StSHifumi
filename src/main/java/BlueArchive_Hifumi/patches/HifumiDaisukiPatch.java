package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.cards.OnDiscardCard;
import BlueArchive_Hifumi.powers.HifumiDaisukiPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class HifumiDaisukiPatch {

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToTop",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToTopPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                    AbstractDungeon.getCurrRoom() != null &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
                if(__instance.type == CardGroup.CardGroupType.DISCARD_PILE &&
                        AbstractDungeon.player.hasPower(HifumiDaisukiPower.POWER_ID)) {
                    AbstractPower power = AbstractDungeon.player.getPower(HifumiDaisukiPower.POWER_ID);
                    power.flashWithoutSound();
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount, true));
                }
                if(__instance.type == CardGroup.CardGroupType.DISCARD_PILE && card instanceof OnDiscardCard) {
                    ((OnDiscardCard)card).triggerWhenDiscardAnyway();
                }
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToBottom",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToBottomPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                    AbstractDungeon.getCurrRoom() != null &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
                if(__instance.type == CardGroup.CardGroupType.DISCARD_PILE &&
                        AbstractDungeon.player.hasPower(HifumiDaisukiPower.POWER_ID)) {
                    AbstractPower power = AbstractDungeon.player.getPower(HifumiDaisukiPower.POWER_ID);
                    power.flashWithoutSound();
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount, true));
                }
                if(__instance.type == CardGroup.CardGroupType.DISCARD_PILE && card instanceof OnDiscardCard) {
                    ((OnDiscardCard)card).triggerWhenDiscardAnyway();
                }
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToRandomSpot",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToRandomSpotPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                    AbstractDungeon.getCurrRoom() != null &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
                if(__instance.type == CardGroup.CardGroupType.DISCARD_PILE &&
                        AbstractDungeon.player.hasPower(HifumiDaisukiPower.POWER_ID)) {
                    AbstractPower power = AbstractDungeon.player.getPower(HifumiDaisukiPower.POWER_ID);
                    power.flashWithoutSound();
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount, true));
                }
                if(__instance.type == CardGroup.CardGroupType.DISCARD_PILE && card instanceof OnDiscardCard) {
                    ((OnDiscardCard)card).triggerWhenDiscardAnyway();
                }
            }
        }
    }
}
