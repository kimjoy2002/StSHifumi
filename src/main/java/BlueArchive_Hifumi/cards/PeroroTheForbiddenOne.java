package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.CrusaderAction;
import BlueArchive_Hifumi.actions.ExodiaAction;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class PeroroTheForbiddenOne extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(PeroroTheForbiddenOne.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG_WITHOUT_EXTENSION = makeCardPath("PeroroTheForbiddenOne");
    public static final String EXTENSION = ".png";


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = -2;

    private static final int DAMAGE = 50;
    private static final int MAGIC = 5;

    public int forbiddenType = 0;


    private static String getImgPath(int forbiddenType) {
        if(forbiddenType <= 0)
            return IMG_WITHOUT_EXTENSION + EXTENSION;
        else {
            return IMG_WITHOUT_EXTENSION + (forbiddenType+1) + EXTENSION;
        }
    }

    public PeroroTheForbiddenOne() {
        this(-1);
    }
    public PeroroTheForbiddenOne(int forbiddenType) {
        super(ID, getImgPath(forbiddenType), COST, TYPE, COLOR, RARITY, TARGET);
        this.cardID = forbiddenType>1?ID+forbiddenType:ID;
        baseMagicNumber = magicNumber = DAMAGE;
        baseSecondMagicNumber = secondMagicNumber = MAGIC;
        this.forbiddenType = forbiddenType;
        this.tags.add(EnumPatch.NONCOLLECTABLE);
        this.tags.add(EnumPatch.PERORO);
        this.selfRetain = true;
        if(forbiddenType != -1) {
            this.name = cardStrings.EXTENDED_DESCRIPTION[2 + forbiddenType];
            initializeTitle();
            if(forbiddenType != 0) {
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
                initializeDescription();
            }
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }


    public void applyPowers() {
        super.applyPowers();
        if(forbiddenType == 0)
            this.addToBot(new ExodiaAction(magicNumber, secondMagicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        PeroroTheForbiddenOne temp = new PeroroTheForbiddenOne(forbiddenType);
        return temp;
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }
}
