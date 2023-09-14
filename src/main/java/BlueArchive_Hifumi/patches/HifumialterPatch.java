package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.characters.Hifumi;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;

public class HifumialterPatch {

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "loadAnimation",
            paramtypez= {
                    String.class,
                    String.class,
                    float.class
            }
    )
    public static class LoadAnimationPatch {
        public static void Prefix(AbstractCreature __instance, @ByRef String[] atlasUrl, @ByRef String[] skeletonUrl, float scale)
        {
            if(AbstractDungeon.player != null && AbstractDungeon.player instanceof Hifumi &&
                    atlasUrl != null && atlasUrl[0].equals("BlueArchive_HoshinoResources/images/monsters/Hifumi.atlas") &&
                    skeletonUrl != null && skeletonUrl[0].equals("BlueArchive_HoshinoResources/images/monsters/Hifumi.json") ){
                atlasUrl[0] = "BlueArchive_HifumiResources/images/char/hifumi/Hifumi3.atlas";
                skeletonUrl[0] = "BlueArchive_HifumiResources/images/char/hifumi/Hifumi3.json";
            }
        }
    }

}
