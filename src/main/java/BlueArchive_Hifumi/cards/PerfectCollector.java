package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.patches.PlayerPatch;
import BlueArchive_Hifumi.powers.BetrayerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;
import static BlueArchive_Hifumi.patches.GameActionManagerPatch.collectedCardSet;
import static org.apache.commons.lang3.math.NumberUtils.max;

public class PerfectCollector extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(PerfectCollector.class.getSimpleName());
    public static final String IMG = makeCardPath("PerfectCollector.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 1;
    private static final int MAGIC = 5;
    private static final int UPGRADE_PLUS_MAGIC = 3;
    private static final int MAGIC2 = 7;

    public PerfectCollector() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;

        this.tags.add(EnumPatch.LEARNED);
        this.selfRetain = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, magicNumber), magicNumber));
    }

    public int getRemainCollect() {return max(secondMagicNumber-collectedCardSet.size(), 0);}
    public void makeDescription() {
        int remain_collect = getRemainCollect();
        super.applyPowers();
        if(remain_collect > 0) {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + remain_collect + cardStrings.EXTENDED_DESCRIPTION[1];
        } else {
            this.rawDescription = cardStrings.DESCRIPTION;
        }
        this.initializeDescription();
    }
    public void applyPowers() {
        PlayerPatch.hasPerfectCollecter = true;
        makeDescription();
        super.applyPowers();
    }
    public void onMoveToDiscard() {
        makeDescription();
        super.onMoveToDiscard();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if(getRemainCollect() == 0)
            return true;
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[2];
        return false;
    }
}
