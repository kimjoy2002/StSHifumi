package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.cardmods.CostMod;
import BlueArchive_Hifumi.cards.LuckyPunch;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;

import java.util.Iterator;

public class LuckyPunchPatch {

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "endBattle"
    )
    public static class nextRoomTransitionPatcher {
        public static void Prefix(AbstractRoom __instance) {

            PlayerPatch.hasPerfectCollecter = false;
            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c instanceof LuckyPunch) {
                    if(c.misc == 1) {
                        c.misc = 0;
                    } else if (c.cost > 0) {
                        int newAmount = 4-c.cost;
                        CardModifierManager.removeModifiersById(c, CostMod.ID, true);
                        CardModifierManager.addModifier(c, new CostMod(newAmount));
                        c.initializeDescription();
                    }
                }
            }
        }
    }

}
