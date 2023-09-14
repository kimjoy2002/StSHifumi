package BlueArchive_Hifumi.shop;

import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import BlueArchive_Hifumi.screens.GachaShopScreen;
import BlueArchive_Hifumi.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.ArrayList;

import static BlueArchive_Hifumi.DefaultMod.getModID;

public class PeroroStoreRelic {
    public AbstractRelic relic;
    public ArrayList<AbstractRelic> cost;
    public int price = 0;
    private static final Texture PLUS =  TextureLoader.getTexture(getModID() + "Resources/images/ui/plus.png");

    private GachaShopScreen shopScreen;
    private int slot;
    public boolean isPurchased = false;
    private static final float RELIC_GOLD_OFFSET_X;
    private static final float RELIC_GOLD_OFFSET_Y;
    private static final float RELIC_COST_OFFSET_X;
    private static final float RELIC_COST_OFFSET_Y;
    private static final float RELIC_PRICE_OFFSET_X;
    private static final float RELIC_PRICE_OFFSET_Y;
    private static final float GOLD_IMG_WIDTH;

    public PeroroStoreRelic(AbstractRelic relic, int price, int slot, GachaShopScreen screenRef) {
        this.relic = relic;
        this.slot = slot;
        this.shopScreen = screenRef;
        this.price = price;
        this.cost = new ArrayList<>();
    }
    public PeroroStoreRelic(AbstractRelic relic, ArrayList<AbstractRelic> cost, int slot, GachaShopScreen screenRef) {
        this.relic = relic;
        this.slot = slot;
        this.shopScreen = screenRef;
        this.cost = cost;
    }

    public void update(float rugY) {
        if (this.relic != null) {
            if (!this.isPurchased) {
                this.relic.currentX = 350.0F * Settings.xScale + 230.0F * (float)this.slot * Settings.xScale;
                this.relic.currentY = rugY + 800.0F * Settings.yScale;
                this.relic.hb.move(this.relic.currentX, this.relic.currentY);
                this.relic.hb.update();
                if (this.relic.hb.hovered) {
                    this.shopScreen.moveHand(this.relic.currentX - 190.0F * Settings.xScale, this.relic.currentY - 70.0F * Settings.yScale);
                    if (InputHelper.justClickedLeft) {
                        this.relic.hb.clickStarted = true;
                    }

                    this.relic.scale = Settings.scale * 1.25F;
                } else {
                    this.relic.scale = MathHelper.scaleLerpSnap(this.relic.scale, Settings.scale);
                }

                if (this.relic.hb.hovered && InputHelper.justClickedRight) {
                    CardCrawlGame.relicPopup.open(this.relic);
                }

            }

            if (this.relic.hb.clicked || this.relic.hb.hovered && CInputActionSet.select.isJustPressed()) {
                this.relic.hb.clicked = false;
                if (!Settings.isTouchScreen) {
                    this.purchaseRelic();
                } else if (this.shopScreen.touchRelic == null) {
                    if (canPurchase()) {
                        this.shopScreen.playCantBuySfx();
                        this.shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
                    } else {
                        this.shopScreen.confirmButton.hideInstantly();
                        this.shopScreen.confirmButton.show();
                        this.shopScreen.confirmButton.isDisabled = false;
                        this.shopScreen.confirmButton.hb.clickStarted = false;
                        this.shopScreen.touchRelic = this;
                    }
                }
            }
        }

    }


    public boolean canPurchase() {
        if(price != 0) {
            if(AbstractDungeon.player.gold >= price) {
                return true;
            }
            return false;
        }
        for(AbstractRelic relic : cost) {
            if(!AbstractDungeon.player.hasRelic(relic.relicId)) {
                return false;
            }
        }
        return true;
    }
    public void purchasesRelicCost() {
        for(AbstractRelic relic : cost) {
            if(AbstractDungeon.player.hasRelic(relic.relicId)) {
                AbstractRelic purchaseRelic = AbstractDungeon.player.getRelic(relic.relicId);
                if(purchaseRelic.counter > 1) {
                    purchaseRelic.setCounter(purchaseRelic.counter-1);
                    purchaseRelic.flash();
                    ((PeroroGoodsRelic)purchaseRelic).updateDescription();
                } else {
                    AbstractDungeon.player.loseRelic(relic.relicId);
                }
            }
        }
    }

    public void purchaseRelic() {
        if (canPurchase()) {
            if(price != 0) {
                AbstractDungeon.player.loseGold(this.price);
            } else {
                purchasesRelicCost();
            }
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
            CardCrawlGame.metricData.addShopPurchaseData(this.relic.relicId);
            AbstractRelic copyRelic = this.relic.makeCopy();
            AbstractDungeon.getCurrRoom().relics.add(copyRelic);
            copyRelic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
            copyRelic.flash();
            this.shopScreen.playBuySfx();
            this.shopScreen.createSpeech(ShopScreen.getBuyMsg());
            if(price != 0) {
                this.isPurchased = true;
            }
        } else {
            this.shopScreen.playCantBuySfx();
            this.shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
        }

    }

    public void hide() {
        if (this.relic != null) {
            this.relic.currentY = (float)Settings.HEIGHT + 200.0F * Settings.scale;
        }

    }

    public void render(SpriteBatch sb) {
        if (this.relic != null) {
            this.relic.renderWithoutAmount(sb, new Color(0.0F, 0.0F, 0.0F, 0.25F));

            if(price != 0) {
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.UI_GOLD, this.relic.currentX + RELIC_GOLD_OFFSET_X, this.relic.currentY + RELIC_GOLD_OFFSET_Y, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
                Color color = Color.WHITE;
                if (this.price > AbstractDungeon.player.gold) {
                    color = Color.SALMON;
                }

                FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(this.price), this.relic.currentX + RELIC_PRICE_OFFSET_X, this.relic.currentY + RELIC_PRICE_OFFSET_Y, color);
            } else {
                float offset_ = 0.0f;
                for(AbstractRelic cost_relic : cost) {
                    sb.setColor(Color.WHITE);
                    sb.draw(cost_relic.img, this.relic.currentX + RELIC_COST_OFFSET_X - 64.0F + offset_, this.relic.currentY + RELIC_COST_OFFSET_Y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, 0.5f, 0.5f, 0, 0, 0, 128, 128, false, false);
                    offset_+=60.0f;
                }
                sb.setColor(Color.WHITE);
                sb.draw(PLUS, this.relic.currentX + RELIC_COST_OFFSET_X - 16.0f + 30.0f, this.relic.currentY + RELIC_COST_OFFSET_Y-16.0f, 32.0f, 32.0f);

            }
        }

    }

    static {
        RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
        RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
        RELIC_COST_OFFSET_X = -45.0F * Settings.scale;
        RELIC_COST_OFFSET_Y = -100.0F * Settings.scale;
        RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
        RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
        GOLD_IMG_WIDTH = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;
    }
}
