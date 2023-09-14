package BlueArchive_Hifumi.shop;

import BlueArchive_Hifumi.cards.PeroroTicketRare;
import BlueArchive_Hifumi.cards.PeroroTicketUncommon;
import BlueArchive_Hifumi.relics.PeroroMembershipCardRelic;
import BlueArchive_Hifumi.relics.peroro.RarePeroroBlockRelic;
import BlueArchive_Hifumi.relics.peroro.RarePeroroDamageRelic;
import BlueArchive_Hifumi.relics.peroro.RarePeroroEnergyRelic;
import BlueArchive_Hifumi.screens.GachaShopScreen;
import BlueArchive_Hifumi.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static BlueArchive_Hifumi.DefaultMod.getModID;

public class StoreGachaPon {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public ArrayList<AbstractRelic> relic;
    public ArrayList<PowerTip> tips = new ArrayList();
    private static final Texture GACHAPON_COMMON =  TextureLoader.getTexture(getModID() + "Resources/images/ui/Gachapon_common.png");
    private static final Texture GACHAPON_UNCOMMON =  TextureLoader.getTexture(getModID() + "Resources/images/ui/Gachapon_uncommon.png");
    private static final Texture GACHAPON_RARE =  TextureLoader.getTexture(getModID() + "Resources/images/ui/Gachapon_rare.png");
    private static final Texture TICKET_UNCOMMON =  TextureLoader.getTexture(getModID() + "Resources/images/ui/silver_ticket.png");
    private static final Texture TICKET_RARE =  TextureLoader.getTexture(getModID() + "Resources/images/ui/gold_ticket.png");

    public static final float PAD_X;
    private GachaShopScreen shopScreen;
    public int price;
    public int price_add;
    private int slot;

    public Hitbox hb;
    private AbstractRelic.RelicTier tier;
    public boolean isPurchased = false;
    private float currentX;
    private float currentY;
    private float scale;
    private static final float RELIC_GOLD_OFFSET_X;
    private static final float RELIC_GOLD_OFFSET_Y;
    private static final float RELIC_PRICE_OFFSET_X;
    private static final float RELIC_PRICE_OFFSET_Y;
    private static final float RELIC_TICKET_OFFSET_X;
    private static final float RELIC_TICKET_OFFSET_Y;
    private static final float GOLD_IMG_WIDTH;
    public String description;

    public StoreGachaPon(ArrayList<AbstractRelic> relic, int slot, AbstractRelic.RelicTier tier, GachaShopScreen screenRef) {
        this.relic = relic;
        this.price = 75;
        this.price_add = 25;
        this.slot = slot;
        this.shopScreen = screenRef;
        this.tier = tier;
        this.hb = new Hitbox(PAD_X, PAD_X);
        this.description = getDescription();
        this.tips.add(new PowerTip(TEXT[0], this.description));
    }

    public String getDescription() {
        if(tier == AbstractRelic.RelicTier.UNCOMMON) {
            return TEXT[3];
        }
        else if(tier == AbstractRelic.RelicTier.RARE) {
            return TEXT[4];
        }
        else {
            return TEXT[1] + price_add + TEXT[2];
        }
    }

    public void update(float rugY) {

        if(this.price != PeroroMembershipCardRelic.AMOUNT && AbstractDungeon.player.hasRelic(PeroroMembershipCardRelic.ID)) {
            this.tips.clear();
            this.price = PeroroMembershipCardRelic.AMOUNT;
            this.price_add = 0;
            this.description = getDescription();
            this.tips.add(new PowerTip(TEXT[0], this.description));
        }

        this.currentX = 500.0F * Settings.xScale + 400.0F * (float)this.slot * Settings.xScale;
        this.currentY = rugY + 400.0F * Settings.yScale;
        this.hb.move(this.currentX, this.currentY);
        this.hb.update();
        if (this.hb.hovered) {
            this.shopScreen.moveHand(this.currentX - 190.0F * Settings.xScale, this.currentY - 70.0F * Settings.yScale);
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }

            this.scale = Settings.scale * 1.25F;
        } else {
            this.scale = MathHelper.scaleLerpSnap(this.scale, Settings.scale);
        }


        if (this.hb.clicked || this.hb.hovered && CInputActionSet.select.isJustPressed()) {
            this.hb.clicked = false;
            if (!Settings.isTouchScreen) {
                this.purchaseGacha();
            } else if (this.shopScreen.touchRelic == null) {
                if (canPurchase()) {
                    this.shopScreen.playCantBuySfx();
                    this.shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
                } else {
                    this.shopScreen.confirmButton.hideInstantly();
                    this.shopScreen.confirmButton.show();
                    this.shopScreen.confirmButton.isDisabled = false;
                    this.shopScreen.confirmButton.hb.clickStarted = false;
                    this.shopScreen.touchGacha = this;
                }
            }
        }
    }


    public AbstractRelic getRandomRelic() {
        return (AbstractRelic)relic.get(AbstractDungeon.merchantRng.random(relic.size() - 1));
    }

    public boolean canPurchase() {
        if(tier == AbstractRelic.RelicTier.UNCOMMON) {
            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c instanceof PeroroTicketUncommon) {
                    return true;
                }
            }
        }
        else if(tier == AbstractRelic.RelicTier.RARE) {
            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c instanceof PeroroTicketRare) {
                    return true;
                }
            }
        }
        else if(AbstractDungeon.player.gold >= this.price) {
            return true;
        }
        return false;
    }

    public void purchase() {
        if(tier == AbstractRelic.RelicTier.UNCOMMON) {
            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c instanceof PeroroTicketUncommon) {
                    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c));
                    AbstractDungeon.player.masterDeck.removeCard(c);
                    break;
                }
            }
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
        }
        else if(tier == AbstractRelic.RelicTier.RARE) {
            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c instanceof PeroroTicketRare) {
                    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c));
                    AbstractDungeon.player.masterDeck.removeCard(c);
                    break;
                }
            }
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
        }
        else {
            AbstractDungeon.player.loseGold(this.price);
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
        }
    }

    public void purchaseGacha() {
        if (canPurchase()) {
            int val_ =(tier == AbstractRelic.RelicTier.UNCOMMON) ?2 : 1;
            purchase();
            for(int i = 0; i<val_; i++) {
                AbstractRelic relic_ = getRandomRelic();
                CardCrawlGame.metricData.addShopPurchaseData(relic_.relicId);
                AbstractDungeon.getCurrRoom().relics.add(relic_);
                relic_.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
                relic_.flash();
            }
            this.shopScreen.playBuySfx();
            this.shopScreen.createSpeech(ShopScreen.getBuyMsg());
            this.price += price_add;
        } else {
            this.shopScreen.playCantBuySfx();
            this.shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
        }
    }

    public void hide() {
    }

    public void render(SpriteBatch sb) {
        Texture texture = GACHAPON_COMMON;
        if(tier == AbstractRelic.RelicTier.UNCOMMON) {
            texture = GACHAPON_UNCOMMON;
        }
        if(tier == AbstractRelic.RelicTier.RARE) {
            texture = GACHAPON_RARE;
        }

        sb.setColor(Color.WHITE);
        sb.draw(texture, this.currentX- 125.f, this.currentY- 125.f, 128.0F, 128.0F, 256.0f, 256.0F, scale, scale, 0, 0, 0, 256, 256, false, false);




        if(tier == AbstractRelic.RelicTier.UNCOMMON) {
            sb.setColor(Color.WHITE);
            sb.draw(TICKET_UNCOMMON, this.currentX + RELIC_TICKET_OFFSET_X, this.currentY + RELIC_TICKET_OFFSET_Y, TICKET_UNCOMMON.getWidth(), TICKET_UNCOMMON.getWidth());

            Color color = Color.WHITE;
            if (!canPurchase()) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(1), this.currentX + RELIC_PRICE_OFFSET_X, this.currentY + RELIC_PRICE_OFFSET_Y, color);
        } else if(tier == AbstractRelic.RelicTier.RARE) {

            sb.setColor(Color.WHITE);
            sb.draw(TICKET_RARE, this.currentX + RELIC_TICKET_OFFSET_X, this.currentY + RELIC_TICKET_OFFSET_Y, TICKET_UNCOMMON.getWidth(), TICKET_UNCOMMON.getWidth());

            Color color = Color.WHITE;
            if (!canPurchase()) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(1), this.currentX + RELIC_PRICE_OFFSET_X, this.currentY + RELIC_PRICE_OFFSET_Y, color);
        }
        else {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.UI_GOLD, this.currentX + RELIC_GOLD_OFFSET_X, this.currentY + RELIC_GOLD_OFFSET_Y, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);


            Color color = Color.WHITE;
            if (this.price > AbstractDungeon.player.gold) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(this.price), this.currentX + RELIC_PRICE_OFFSET_X, this.currentY + RELIC_PRICE_OFFSET_Y, color);

        }
        if (this.hb.hovered) {
            renderTip(sb);
        }
        this.hb.render(sb);
    }

    public void renderTip(SpriteBatch sb) {
        if ((float)InputHelper.mX < 1400.0F * Settings.scale) {
            TipHelper.queuePowerTips((float)InputHelper.mX + 50.0F * Settings.scale, (float)InputHelper.mY + 50.0F * Settings.scale, this.tips);
        } else {
            TipHelper.queuePowerTips((float)InputHelper.mX - 350.0F * Settings.scale, (float)InputHelper.mY - 50.0F * Settings.scale, this.tips);
        }

    }


    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:GachaPon");
        TEXT = uiStrings.TEXT;
        RELIC_GOLD_OFFSET_X = -45.0F * Settings.scale;
        RELIC_GOLD_OFFSET_Y = -225.0F * Settings.scale;
        RELIC_TICKET_OFFSET_X = -45.0F * Settings.scale;
        RELIC_TICKET_OFFSET_Y = -225.0F * Settings.scale;
        RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
        RELIC_PRICE_OFFSET_Y = -187.0F * Settings.scale;
        PAD_X = 256.0F * Settings.scale;
        GOLD_IMG_WIDTH = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;
    }
}
