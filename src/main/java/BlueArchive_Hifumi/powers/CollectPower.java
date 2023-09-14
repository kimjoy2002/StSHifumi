package BlueArchive_Hifumi.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface CollectPower {
    void onCardCollect(AbstractCard c);
    void triggerWhenCollect(int amount);
}
