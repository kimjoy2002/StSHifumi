package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.UsePeroroGoodsAction;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class PeroroLimitedEdition extends AbstractDynamicCard implements CollectCard {

    public static final String ID = DefaultMod.makeID(PeroroLimitedEdition.class.getSimpleName());
    public static final String IMG = makeCardPath("PeroroLimitedEdition.png");


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    private boolean collectedTurn;

    public PeroroLimitedEdition() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        collectedTurn = false;
        tags.add(EnumPatch.PERORO);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if(collectedTurn) {
            AbstractDungeon.actionManager.addToBottom(new UsePeroroGoodsAction(false));
        }
    }

    @Override
    public void resetAttributes() {
        collectedTurn = false;
        super.resetAttributes();
    }
    @Override
    public void triggerWhenCollect() {
        collectedTurn = true;
    }

    @Override
    public AbstractCard makeSameInstanceOf() {
        AbstractCard card = super.makeSameInstanceOf();
        if(card instanceof PeroroLimitedEdition)
            ((PeroroLimitedEdition)card).collectedTurn = this.collectedTurn;
        return card;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = BlueArchive_Hifumi.cards.AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (collectedTurn) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
