package com.github.khanshoaib3.minecraft_access.config;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class BaseScreen extends Screen {
    int centerX;
    int buttonHeight;
    int marginY;
    int calculatedYPosition;
    int calculatedXPosition;
    int leftColumnX;
    int rightColumnX;
    boolean shouldRenderInLeftColumn;

    public BaseScreen(String title) {
        super(Text.of(I18n.translate("minecraft_access.gui.screen." + title)));
    }

    @Override
    protected void init() {
        this.centerX = this.width / 2;
        this.buttonHeight = 20;
        this.marginY = buttonHeight + buttonHeight / 4;
        this.calculatedYPosition = this.height / 6 - marginY; // Starting Y position (marginY will again be added in buildButtonWidget() so it is subtracted here to equate)
        this.leftColumnX = 10;
        this.rightColumnX = centerX + 10;
        shouldRenderInLeftColumn = true;
    }

    protected ButtonWidget buildButtonWidget(String translationKey, ButtonWidget.PressAction pressAction) {
        int calculatedButtonWidth = this.textRenderer.getWidth(I18n.translate((translationKey))) + 35;
        calculatedXPosition = (shouldRenderInLeftColumn) ? leftColumnX : rightColumnX;
        if (shouldRenderInLeftColumn) calculatedYPosition += marginY;
        shouldRenderInLeftColumn = !shouldRenderInLeftColumn;

        return ButtonWidget.builder(Text.translatable(translationKey), pressAction)
                .dimensions(calculatedXPosition, calculatedYPosition, calculatedButtonWidth, buttonHeight)
                .build();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
