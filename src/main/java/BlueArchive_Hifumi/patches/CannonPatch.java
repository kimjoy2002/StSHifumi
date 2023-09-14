package BlueArchive_Hifumi.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class CannonPatch {
    @SpirePatch(
            clz = DamageInfo.class,
            method=SpirePatch.CLASS
    )
    public static class CannonPatcher {
        public static SpireField<Boolean> isCannon = new SpireField<>(() -> false);
    }


    @SpirePatch(
            clz = ThornsPower.class,
            method = "onAttacked",
            paramtypez = {DamageInfo.class, int.class}
    )
    public static class ThornsPatch {
        public static SpireReturn Prefix(ThornsPower __instance, DamageInfo info, int damageAmount) {
            boolean isCannon = CannonPatcher.isCannon.get(info);
            if(isCannon) {
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue();
        }
    }
}
