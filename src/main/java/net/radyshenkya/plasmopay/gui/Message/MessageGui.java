package net.radyshenkya.plasmopay.gui.Message;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.radyshenkya.plasmopay.Utils;

public class MessageGui extends LightweightGuiDescription {
    public static final int PADDING = 5;
    public static final int SPACING = 1;
    public static final int LINE_HEIGHT = 15;

    public MessageGui(Text... messageLines) {
        WBox root = createRoot();

        int panelWidth = messageLines[0].getString().length() * 8 + PADDING * 2;

        for (Text line : messageLines) {
            if ((line.getString().length() * 8 + PADDING * 2) > panelWidth)
                panelWidth = line.getString().length() * 8 + PADDING * 2;
        }

        root.setSize(panelWidth, (messageLines.length + 1) * (SPACING + LINE_HEIGHT) + PADDING * 2);
        setRootPanel(root);

        addTexts(root, messageLines);
        addButton(root);
    }

    public MessageGui(Text message, int lineMaxLength, boolean breakWords) {
        WBox root = createRoot();

        Text[] messageLines = Utils.wrapTextByMaxLength(message, lineMaxLength, breakWords);

        int panelWidth = messageLines[0].getString().length() * 8 + PADDING * 2;

        for (Text line : messageLines) {
            if ((line.getString().length() * 8 + PADDING * 2) > panelWidth)
                panelWidth = line.getString().length() * 8 + PADDING * 2;
        }

        root.setSize(panelWidth, (messageLines.length + 1) * (SPACING + LINE_HEIGHT) + PADDING * 2);
        setRootPanel(root);

        addTexts(root, messageLines);
        addButton(root);
    }

    private void addTexts(WBox root, Text[] texts) {
        for (Text line : texts) {
            root.add(new WLabel(line), root.getWidth() - PADDING * 2, LINE_HEIGHT);
        }
    }

    private void addButton(WBox root) {
        WButton okButton = new WButton();
        okButton.setLabel(Text.translatable("gui.plasmopay.button.ok"));

        okButton.setOnClick(() -> {
            MinecraftClient.getInstance().setScreen(null);
        });

        root.add(okButton, root.getWidth() - PADDING * 2, LINE_HEIGHT);
    }

    private WBox createRoot() {
        WBox root = new WBox(Axis.VERTICAL);
        root.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.setVerticalAlignment(VerticalAlignment.CENTER);
        root.setSpacing(SPACING);

        return root;
    }
}
