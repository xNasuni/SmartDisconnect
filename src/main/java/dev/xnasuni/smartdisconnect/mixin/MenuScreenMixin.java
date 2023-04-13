package dev.xnasuni.smartdisconnect.mixin;

import dev.xnasuni.smartdisconnect.SmartDisconnect;
import dev.xnasuni.smartdisconnect.config.ModConfig;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GameMenuScreen.class)
public abstract class MenuScreenMixin extends Screen {
    CheckboxWidget CheckboxWidgetConfirm;

    public MenuScreenMixin(Text text) {
        super(text);
    }

    /**
     * @author xNasuni
     * @reason there was no other way to target that 1 thing
     */
    @Overwrite
    private void initWidgets() {
        if (ModConfig.INSTANCE.Enabled) {
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, new TranslatableText("menu.returnToGame"), (button) -> {
                this.client.setScreen((Screen) null);
                this.client.mouse.lockCursor();
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 48 + -16, 98, 20, new TranslatableText("gui.advancements"), (button) -> {
                this.client.setScreen(new AdvancementsScreen(this.client.player.networkHandler.getAdvancementHandler()));
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16, 98, 20, new TranslatableText("gui.stats"), (button) -> {
                this.client.setScreen(new StatsScreen(this, this.client.player.getStatHandler()));
            }));
            String string = SharedConstants.getGameVersion().isStable() ? "https://aka.ms/javafeedback?ref=game" : "https://aka.ms/snapshotfeedback?ref=game";
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 72 + -16, 98, 20, new TranslatableText("menu.sendFeedback"), (button) -> {
                this.client.setScreen(new ConfirmChatLinkScreen((confirmed) -> {
                    if (confirmed) {
                        Util.getOperatingSystem().open(string);
                    }

                    this.client.setScreen(this);
                }, string, true));
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 72 + -16, 98, 20, new TranslatableText("menu.reportBugs"), (button) -> {
                this.client.setScreen(new ConfirmChatLinkScreen((confirmed) -> {
                    if (confirmed) {
                        Util.getOperatingSystem().open("https://aka.ms/snapshotbugs?ref=game");
                    }

                    this.client.setScreen(this);
                }, "https://aka.ms/snapshotbugs?ref=game", true));
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 96 + -16, 98, 20, new TranslatableText("menu.options"), (button) -> {
                this.client.setScreen(new OptionsScreen(this, this.client.options));
            }));
            ButtonWidget buttonWidget = (ButtonWidget) this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 96 + -16, 98, 20, new TranslatableText("menu.shareToLan"), (button) -> {
                this.client.setScreen(new OpenToLanScreen(this));
            }));
            buttonWidget.active = this.client.isIntegratedServerRunning() && !this.client.getServer().isRemote();
            CheckboxWidgetConfirm = new CheckboxWidget(this.width / 2 - 102 + 204 - 20, this.height / 4 + 120 + -16, 20, 20, Text.of("Confirm"), false);
            this.addDrawableChild(CheckboxWidgetConfirm);
            Text text = this.client.isInSingleplayer() ? new TranslatableText("menu.returnToMenu") : new TranslatableText("menu.disconnect");
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 120 + -16, 204 - 24, 20, text, (button) -> {
                if (!CheckboxWidgetConfirm.isChecked()) {
                    return;
                }
                boolean bl = this.client.isInSingleplayer();
                boolean bl2 = this.client.isConnectedToRealms();
                button.active = false;
                this.client.world.disconnect();
                if (bl) {
                    this.client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
                } else {
                    this.client.disconnect();
                }
                TitleScreen titleScreen = new TitleScreen();
                if (bl) {
                    this.client.setScreen(titleScreen);
                } else if (bl2) {
                    this.client.setScreen(new RealmsMainScreen(titleScreen));
                } else {
                    this.client.setScreen(new MultiplayerScreen(titleScreen));
                }
            }));
        } else {
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, new TranslatableText("menu.returnToGame"), (button) -> {
                this.client.setScreen((Screen) null);
                this.client.mouse.lockCursor();
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 48 + -16, 98, 20, new TranslatableText("gui.advancements"), (button) -> {
                this.client.setScreen(new AdvancementsScreen(this.client.player.networkHandler.getAdvancementHandler()));
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16, 98, 20, new TranslatableText("gui.stats"), (button) -> {
                this.client.setScreen(new StatsScreen(this, this.client.player.getStatHandler()));
            }));
            String string = SharedConstants.getGameVersion().isStable() ? "https://aka.ms/javafeedback?ref=game" : "https://aka.ms/snapshotfeedback?ref=game";
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 72 + -16, 98, 20, new TranslatableText("menu.sendFeedback"), (button) -> {
                this.client.setScreen(new ConfirmChatLinkScreen((confirmed) -> {
                    if (confirmed) {
                        Util.getOperatingSystem().open(string);
                    }

                    this.client.setScreen(this);
                }, string, true));
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 72 + -16, 98, 20, new TranslatableText("menu.reportBugs"), (button) -> {
                this.client.setScreen(new ConfirmChatLinkScreen((confirmed) -> {
                    if (confirmed) {
                        Util.getOperatingSystem().open("https://aka.ms/snapshotbugs?ref=game");
                    }

                    this.client.setScreen(this);
                }, "https://aka.ms/snapshotbugs?ref=game", true));
            }));
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 96 + -16, 98, 20, new TranslatableText("menu.options"), (button) -> {
                this.client.setScreen(new OptionsScreen(this, this.client.options));
            }));
            ButtonWidget buttonWidget = (ButtonWidget) this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 96 + -16, 98, 20, new TranslatableText("menu.shareToLan"), (button) -> {
                this.client.setScreen(new OpenToLanScreen(this));
            }));
            buttonWidget.active = this.client.isIntegratedServerRunning() && !this.client.getServer().isRemote();
            Text text = this.client.isInSingleplayer() ? new TranslatableText("menu.returnToMenu") : new TranslatableText("menu.disconnect");
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 120 + -16, 204, 20, text, (button) -> {
                boolean bl = this.client.isInSingleplayer();
                boolean bl2 = this.client.isConnectedToRealms();
                button.active = false;
                this.client.world.disconnect();
                if (bl) {
                    this.client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
                } else {
                    this.client.disconnect();
                }

                TitleScreen titleScreen = new TitleScreen();
                if (bl) {
                    this.client.setScreen(titleScreen);
                } else if (bl2) {
                    this.client.setScreen(new RealmsMainScreen(titleScreen));
                } else {
                    this.client.setScreen(new MultiplayerScreen(titleScreen));
                }

            }));
        }
    }
}
