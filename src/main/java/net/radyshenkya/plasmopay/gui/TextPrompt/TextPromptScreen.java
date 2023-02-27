package net.radyshenkya.plasmopay.gui.TextPrompt;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TextPromptScreen extends BaseUIModelScreen<FlowLayout> {
    private Text name;
    private String placeholder;
    private String value;
    private IOnTextPromptOk okCallback;

    public TextPromptScreen(Text promptName, String promptPlaceHolder, String value, IOnTextPromptOk okCallback) {
        super(FlowLayout.class, DataSource.asset(new Identifier("plasmopay:prompt")));

        this.name = promptName;
        this.placeholder = promptPlaceHolder;
        this.value = value;
        this.okCallback = okCallback;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(LabelComponent.class, "message-name")
                .text(name);

        TextBoxComponent promptText = (TextBoxComponent) rootComponent.childById(TextBoxComponent.class, "prompt-text")
                .text(value)
                .tooltip(Text.of(placeholder));

        rootComponent.childById(ButtonComponent.class, "ok-button").onPress(button -> {
            okCallback.onOkClicked(promptText.getText());
        });

        rootComponent.childById(ButtonComponent.class, "cancel-button").onPress(button -> {
            MinecraftClient.getInstance().setScreen(null);
        });
    }
}