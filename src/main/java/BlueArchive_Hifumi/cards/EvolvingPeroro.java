package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.*;
import BlueArchive_Hifumi.cardmods.EvoveMod;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.patches.CannonPatch;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import BlueArchive_Hifumi.screens.EvoveScreen;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;
import static BlueArchive_Hifumi.cardmods.EvoveMod.EvoveEnum.ADD_VULUN;

public class EvolvingPeroro extends AbstractDynamicCard implements CollectCard {
    public static final String ID = DefaultMod.makeID(EvolvingPeroro.class.getSimpleName());
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("EvolvingPeroro.png");

    public static final String IMG_WITHOUT_EXTENSION = makeCardPath("EvolvingPeroro");
    public static final String EXTENSION = ".png";


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 2;
    private static final int DAMAGE = 14;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 2;

    int damageVal = DAMAGE;
    float damageMod = 1.0f;
    int attackMulti = 1;
    int blockVal = 0;
    float blockMod = 1.0f;
    int magicVal = 0;
    float magicMod = 1.0f;
    int magic2Val = 0;
    boolean isWeak = false;
    boolean isVul = false;
    boolean allAttack = false;
    boolean isDraw = false;
    boolean isExhaust = false;
    boolean isRetain = false;
    boolean isBuried = false;
    int curse = 0;
    private CardTarget currentTarget = CardTarget.ENEMY;

    public EvolvingPeroro() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        misc = MathUtils.random(100000);
        this.tags.add(EnumPatch.PERORO);
    }

    private static String getImgPath(int level) {
        if(level <= 1)
            return IMG_WITHOUT_EXTENSION + EXTENSION;
        else if(level <= 6) {
            return IMG_WITHOUT_EXTENSION + level + EXTENSION;
        } else {
            return IMG_WITHOUT_EXTENSION + 6 + EXTENSION;
        }
    }
    public int expectCollect(){
        if(thirdMagicNumber > 0) {
            if (!isDraw) {
                return thirdMagicNumber;
            }
        }
        return 0;
    };
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(block > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        }
        if(m == null && !allAttack) {
            DamageInfo cannon_damage = new DamageInfo(p, damage, this.damageTypeForTurn);
            CannonPatch.CannonPatcher.isCannon.set(cannon_damage, true);
            AbstractDungeon.actionManager.addToBottom(new AttackRandomAction(cannon_damage, AbstractGameAction.AttackEffect.FIRE));

            this.addToBot(new RandomEvoveAttackAction(this, attackMulti, cannon_damage));
        } else {
            for (int i = 0; i < attackMulti; i++) {
                if(!allAttack) {
                    this.addToBot(new EvoveAttackAction(this, m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                }
                else {
                    this.addToBot(new EvoveAttackAction(this, p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
            }
        }

        if(isWeak) {
            if(allAttack) {
                Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                AbstractMonster mo;
                while(var3.hasNext()) {
                    mo = (AbstractMonster)var3.next();
                    this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false), magicNumber));
                }
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
            }
        }
        if(isVul) {
            if(allAttack) {
                Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                AbstractMonster mo;
                while(var3.hasNext()) {
                    mo = (AbstractMonster)var3.next();
                    this.addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber));
                }
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
            }
        }
        if(thirdMagicNumber > 0) {
            if(isDraw) {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(thirdMagicNumber));
            }
        }
        if(curse>0){
            this.addToBot(new MakeTempCardInHandAction(new FakePeroro(), curse));
        }
        if(thirdMagicNumber > 0) {
            if(!isDraw) {
                AbstractDungeon.actionManager.addToBottom(new CollectAction(thirdMagicNumber, false));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new CannonAfterAction(this, currentTarget));
    }
    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if(this.tags.contains(EnumPatch.CANNON)) {
            this.dontTriggerOnUseCard = true;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        }
    }
    public void applyMod() {
        magicNumber = MAGIC;
        if(upgraded)
            magicNumber += UPGRADE_PLUS_MAGIC;
        damageVal = DAMAGE;
        damageMod = 1.0f;
        attackMulti = 1;
        blockVal = 0;
        blockMod = 1.0f;
        magicVal = 0;
        magicMod = 1.0f;
        magic2Val = 0;
        int level = 1;
        isMultiDamage = false;
        target = CardTarget.ENEMY;
        isWeak = false;
        isVul = false;
        allAttack = false;
        isExhaust = false;
        isRetain = false;
        isBuried = false;
        curse = 0;
        this.cardsToPreview = null;
        currentTarget = CardTarget.ENEMY;

        ArrayList<AbstractCardModifier> mods = CardModifierManager.modifiers(this);
        for(AbstractCardModifier mod : mods) {
            if(mod instanceof EvoveMod) {
                EvoveMod evoveMod = (EvoveMod)mod;
                if(evoveMod.type != null) {
                    magicNumber--;
                    level++;
                    switch(evoveMod.type) {
                        case ADD_DAMAGE:
                            damageVal+=3;
                            break;
                        case ADD_BLOCK:
                            if(blockVal == 0) {
                                blockVal = damageVal/2+2;
                                damageVal = damageVal/2+2;
                            } else {
                                blockVal += 3;
                            }
                            break;
                        case ADD_VULUN:
                            if(!(!isVul && isWeak)) {
                                magicVal++;
                            }
                            isVul = true;
                            break;
                        case ADD_WEAK:
                            if(!(isVul && !isWeak)) {
                                magicVal++;
                            }
                            isWeak = true;
                            break;
                        case ADD_MULTI:
                            if(attackMulti == 1) {
                                damageMod *= 0.5f;
                                attackMulti=2;
                            } else if(attackMulti == 2) {
                                damageMod *= 2;
                                damageMod *= 0.4f;
                                attackMulti=3;
                            }
                            break;
                        case COST_DOWN:
                            this.cost = 1;
                            this.costForTurn = 1;
                            damageMod *= 0.7f;
                            blockMod *= 0.7f;
                            break;
                        case COST_UP:
                            this.cost = 3;
                            this.costForTurn = 3;
                            damageMod *= 1.6f;
                            blockMod *= 1.6f;
                            this.upgradedCost = true;
                            break;
                        case ADD_CANNON:
                            this.cost = 1;
                            this.costForTurn = 1;
                            if(!this.tags.contains(EnumPatch.CANNON)){
                                this.tags.add(EnumPatch.CANNON);
                            }
                            damageMod *= 0.5f;
                            blockMod *= 0.5f;
                            this.upgradedCost = true;
                            break;
                        case ADD_ALL_ENEMY:
                            isMultiDamage = true;
                            target = CardTarget.ALL_ENEMY;
                            currentTarget = CardTarget.ALL_ENEMY;
                            allAttack = true;
                            damageMod *= 0.9f;
                            blockMod *= 0.9f;
                            break;
                        case ADD_EXHAUST:
                            exhaust = true;
                            isExhaust = true;
                            damageMod *= 1.4f;
                            blockMod *= 1.4f;
                            break;
                        case ADD_DRAW:
                            magic2Val++;
                            isDraw = true;
                            break;
                        case ADD_COLLECT:
                            magic2Val++;
                            isDraw = false;
                            break;
                        case ADD_RETAIN:
                            damageVal+=1;
                            isRetain = true;
                            selfRetain = true;
                            break;
                        case ADD_CURSE:
                            damageVal+=6;
                            curse++;
                            this.cardsToPreview = new FakePeroro();
                            break;
                        case ADD_BURIED:
                            damageMod *= 1.25f;
                            blockMod *= 1.25f;
                            isBuried = true;
                            if(!this.tags.contains(EnumPatch.BURIED)){
                                this.tags.add(EnumPatch.BURIED);
                            }
                            break;

                    }
                }
            }
        }
        if(damageMod <= 0.33f)
            damageMod = 0.34f;
        if(blockMod <= 0.33f)
            blockMod = 0.34f;
        baseDamage = (int)(damageVal * damageMod);
        baseBlock = (int)(blockVal * blockMod);
        baseMagicNumber = magicNumber;
        baseSecondMagicNumber = secondMagicNumber = (int)(magicVal * magicMod);
        baseThirdMagicNumber = thirdMagicNumber = (int)(magic2Val * magicMod);
        updateEvoveDescription();
        this.textureImg = getImgPath(level);
        if (textureImg != null) {
            try {
                this.loadCardImage(textureImg);
            } catch (Throwable ignore) {

            }
        }
    }

    public void updateEvoveDescription() {
        rawDescription = "";
        boolean needLineBreak = false;
        if(this.tags.contains(EnumPatch.CANNON)){
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[10].trim() + " ";
            needLineBreak = true;
        }
        if(isRetain) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[17].trim() + " ";
            needLineBreak = true;
        }
        if(isBuried) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[20].trim() + " ";
            needLineBreak = true;
        }
        if(needLineBreak) {
            rawDescription += "NL ";
        }
        if (blockVal > 0) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[1].trim() + " ";
        }
        if(attackMulti == 2) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[7].trim() + " ";
        } else if (attackMulti == 3) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[8].trim() + " ";
        } else if (allAttack == true) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[9].trim() + " ";
        } else {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[0].trim() + " ";
        }
        if (magicVal > 0) {
            if(isWeak && !isVul) {
                if (allAttack == true) {
                    rawDescription += cardStrings.EXTENDED_DESCRIPTION[11].trim() + " ";
                }
                else {
                    rawDescription += cardStrings.EXTENDED_DESCRIPTION[2].trim() + " ";
                }
            }
            else if(!isWeak && isVul) {
                if (allAttack == true) {
                    rawDescription += cardStrings.EXTENDED_DESCRIPTION[12].trim() + " ";
                }
                else {
                    rawDescription += cardStrings.EXTENDED_DESCRIPTION[3].trim() + " ";
                }
            }
            else if(isWeak && isVul) {
                if (allAttack == true) {
                    rawDescription += cardStrings.EXTENDED_DESCRIPTION[13].trim() + " ";
                }
                else {
                    rawDescription += cardStrings.EXTENDED_DESCRIPTION[4].trim() + " ";
                }
            }
        }
        if (magic2Val > 0) {
            if(isDraw) {
                rawDescription += cardStrings.EXTENDED_DESCRIPTION[15].trim() + " ";
            }
        }
        if (curse == 1) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[18].trim() + " ";
        } else if(curse == 2) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[19].trim() + " ";
        }


        if (magic2Val > 0) {
            if(!isDraw) {
                rawDescription += "NL ";
                rawDescription += cardStrings.EXTENDED_DESCRIPTION[16].trim() + " ";
            }
        }

        if(magicNumber >= 1) {
            rawDescription += "NL ";
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[5].trim() + " ";
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[6].trim() + " ";
        }

        if(isExhaust) {
            rawDescription += "NL ";
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[14].trim();
        }

        initializeDescription();
    }

    @Override
    public void onChoseThisOption() {
        EvoveScreen.selected = this;
    }
    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            if(CardModifierManager.hasModifier(this, EvoveMod.ID)) {
                updateEvoveDescription();
            } else {
                rawDescription = cardStrings.DESCRIPTION;
                initializeDescription();
            }
        }
    }
}
