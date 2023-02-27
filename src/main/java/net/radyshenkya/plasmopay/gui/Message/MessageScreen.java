package net.radyshenkya.plasmopay.gui.Message;

import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;

public class MessageScreen extends BaseUIModelScreen<FlowLayout> {
    private Text name;
    private Text message;

    public MessageScreen(Text name, Text message) {
        super(FlowLayout.class, DataSource.asset(new Identifier("plasmopay:message")));

        this.name = name;
        this.message = message;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(LabelComponent.class, "message-name")
                .text(name);
        rootComponent.childById(LabelComponent.class, "message-text")
                .text(message);

        rootComponent.childById(ButtonComponent.class, "ok-button").onPress(button -> {
            MinecraftClient.getInstance().setScreen(null);
        });
    }
}
