package BlueArchive_Hifumi.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class EnumPatch {


    @SpireEnum
    public static AbstractCard.CardTags BURIED;
    @SpireEnum
    public static AbstractCard.CardTags LEARNED;
    @SpireEnum
    public static AbstractCard.CardTags NONCOLLECTABLE;
    @SpireEnum
    public static AbstractCard.CardTags FRIEND;
    @SpireEnum
    public static AbstractCard.CardTags CANNON;
    @SpireEnum
    public static AbstractCard.CardTags PERORO;

    @SpireEnum
    public static RewardItem.RewardType REWORD_LEARNED;
    @SpireEnum
    public static RewardItem.RewardType REWORD_EVOVE;

    public EnumPatch() {
    }
}
