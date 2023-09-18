package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class PeroroSmash extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(PeroroSmash.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("PeroroSmash.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 1;


    public PeroroSmash() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 0;
        this.tags.add(EnumPatch.PERORO);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
        } else {
            float x_  = 0;
            float y_  = 0;
            int count = 0;
            for(AbstractMonster mon_ : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(!mon_.isDying && mon_.currentHealth > 0 && !mon_.isEscaping) {
                    x_ += mon_.hb.cX;
                    y_ += mon_.hb.cY;
                    count++;
                }

            }
            if(count > 0) {
                x_ /= count;
                y_ /= count;

                this.addToBot(new VFXAction(new WeightyImpactEffect(x_, y_)));
            }
        }
        this.addToBot(new WaitAction(0.8F));

        if(upgraded) {
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        } else {
            this.addToBot(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }

        this.rawDescription = (upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION);
        this.initializeDescription();
    }

    public void applyPowers() {
        this.baseDamage = AbstractDungeon.player.discardPile.size();
        super.applyPowers();
        this.rawDescription = (upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION) + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = (upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION) + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isMultiDamage = true;
            this.target = CardTarget.ALL_ENEMY;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
