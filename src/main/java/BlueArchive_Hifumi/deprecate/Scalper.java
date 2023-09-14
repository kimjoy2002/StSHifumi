package BlueArchive_Hifumi.deprecate;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.CollectSelectAction;
import BlueArchive_Hifumi.cards.AbstractDynamicCard;
import BlueArchive_Hifumi.cards.CollectCard;
import BlueArchive_Hifumi.characters.Hifumi;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class Scalper extends AbstractDynamicCard implements CollectCard {

    public static final String ID = DefaultMod.makeID(Scalper.class.getSimpleName());
    public static final String IMG = makeCardPath("Scalper.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 1;
    private static final int MAGIC = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;



    public Scalper() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new CollectSelectAction(magicNumber, false));
    }

    @Override
    public int expectCollect(){return 0;}
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
