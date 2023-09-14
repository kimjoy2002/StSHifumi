package BlueArchive_Hifumi.deprecate;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.AbstractDynamicCard;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.powers.MineExpertPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class MineExpert extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(MineExpert.class.getSimpleName());
    public static final String IMG = makeCardPath("MineExpert.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 1;

    private static final int MAGIC = 4;
    private static final int UPGRADE_PLUS_MAGIC = 2;

    public MineExpert() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MineExpertPower(AbstractDungeon.player, AbstractDungeon.player, magicNumber)));
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
}
