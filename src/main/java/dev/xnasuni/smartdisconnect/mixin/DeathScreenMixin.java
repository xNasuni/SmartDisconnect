package dev.xnasuni.smartdisconnect.mixin;

import dev.xnasuni.smartdisconnect.SmartDisconnect;
import dev.xnasuni.smartdisconnect.config.ModConfig;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    CheckboxWidget CheckboxWidgetConfirm;
    @Shadow
    private int ticksSinceDeath;

    @Shadow
    @Final
    private List<ButtonWidget> buttons;

    @Shadow
    @Final
    private boolean isHardcore;

    @Shadow
    protected abstract void quitLevel();

    @Shadow
    private Text scoreText;

    @Shadow
    protected abstract void onConfirmQuit(boolean quit);

    public DeathScreenMixin(Text text) {
        super(text);
    }


    /**
     * @author xNasuni
     * @reason there was no other way to target that 1 thing
     */
    @Overwrite
    public void init() {
        if (ModConfig.INSTANCE.Enabled) {
            this.ticksSinceDeath = 0;
            this.buttons.clear();
            this.buttons.add((ButtonWidget) this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 72, 200, 20, this.isHardcore ? new TranslatableText("deathScreen.spectate") : new TranslatableText("deathScreen.respawn"), (button) -> {
                this.client.player.requestRespawn();
                this.client.setScreen((Screen) null);
            })));

            CheckboxWidgetConfirm = new CheckboxWidget(this.width / 2 - 100 + 200 - 20, this.height / 4 + 96, 20, 20, Text.of("Confirm"), false);
            this.addDrawableChild(CheckboxWidgetConfirm);
            this.buttons.add((ButtonWidget) this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 96, 200 - 24, 20, new TranslatableText("deathScreen.titleScreen"), (button) -> {
                if (!CheckboxWidgetConfirm.isChecked()) {
                    return;
                }

                if (this.isHardcore) {
                    this.quitLevel();
                } else {
                    this.onConfirmQuit(true);
                }
            })));

            ButtonWidget buttonWidget;
            for (Iterator var1 = this.buttons.iterator(); var1.hasNext(); buttonWidget.active = false) {
                buttonWidget = (ButtonWidget) var1.next();
            }

            this.scoreText = (new TranslatableText("deathScreen.score")).append(": ").append((new LiteralText(Integer.toString(this.client.player.getScore()))).formatted(Formatting.YELLOW));
        } else {

            this.ticksSinceDeath = 0;
            this.buttons.clear();
            this.buttons.add((ButtonWidget) this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 72, 200, 20, this.isHardcore ? new TranslatableText("deathScreen.spectate") : new TranslatableText("deathScreen.respawn"), (button) -> {
                this.client.player.requestRespawn();
                this.client.setScreen((Screen) null);
            })));
            this.buttons.add((ButtonWidget) this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 96, 200, 20, new TranslatableText("deathScreen.titleScreen"), (button) -> {
                if (this.isHardcore) {
                    this.quitLevel();
                } else {
                    ConfirmScreen confirmScreen = new ConfirmScreen(this::onConfirmQuit, new TranslatableText("deathScreen.quit.confirm"), LiteralText.EMPTY, new TranslatableText("deathScreen.titleScreen"), new TranslatableText("deathScreen.respawn"));
                    this.client.setScreen(confirmScreen);
                    confirmScreen.disableButtons(20);
                }
            })));

            ButtonWidget buttonWidget;
            for (Iterator var1 = this.buttons.iterator(); var1.hasNext(); buttonWidget.active = false) {
                buttonWidget = (ButtonWidget) var1.next();
            }

            this.scoreText = (new TranslatableText("deathScreen.score")).append(": ").append((new LiteralText(Integer.toString(this.client.player.getScore()))).formatted(Formatting.YELLOW));
        }
    }
}
