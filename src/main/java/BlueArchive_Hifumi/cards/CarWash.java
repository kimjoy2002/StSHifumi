package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.CarWashAction;
import BlueArchive_Hifumi.actions.CollectAction;
import BlueArchive_Hifumi.actions.TimeBoxAction;
import BlueArchive_Hifumi.characters.Hifumi;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class CarWash extends AbstractDynamicCard implements CollectCard {

    public static final String ID = DefaultMod.makeID(CarWash.class.getSimpleName());
    public static final String IMG = makeCardPath("CarWash.png");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 1;
    private static final int MAGIC = 1;

    public CarWash() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new CarWashAction(1));
        if(upgraded){
            AbstractDungeon.actionManager.addToBottom(new CollectAction(magicNumber, false));
        }

    }
    @Override
    public int expectCollect(){return upgraded?1:0;}
    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
