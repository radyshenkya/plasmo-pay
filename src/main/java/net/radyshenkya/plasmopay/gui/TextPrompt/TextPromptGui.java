package net.radyshenkya.plasmopay.gui.TextPrompt;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;

public class TextPromptGui extends LightweightGuiDescription {
    public static final int PADDING = 5;

    public TextPromptGui(Text name, String placeholder, String defaultText, int maxLength, IOnTextPromptOk okCallback) {
        WBox root = new WBox(Axis.VERTICAL);
        root.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.setVerticalAlignment(VerticalAlignment.CENTER);
        int panelWidth = name.getString().length() * 8 + PADDING * 2;
        root.setSize(panelWidth, 70);
        setRootPanel(root);

        root.add(new WLabel(name), panelWidth - PADDING * 2, 15);

        WTextField promptTextField = new WTextField();
        promptTextField.setSuggestion(Text.of(placeholder));
        promptTextField.setText(defaultText);
        promptTextField.setMaxLength(maxLength);

        root.add(promptTextField, panelWidth - PADDING * 2, 15);

        WButton okButton = new WButton();
        okButton.setLabel(Text.translatable("gui.plasmopay.button.ok"));

        okButton.setOnClick(() -> {
            okCallback.onOkClicked(promptTextField.getText());
        });

        root.add(okButton, panelWidth - PADDING * 2, 15);
    }
}
