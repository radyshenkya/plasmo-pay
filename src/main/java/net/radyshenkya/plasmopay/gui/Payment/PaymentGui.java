package net.radyshenkya.plasmopay.gui.Payment;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.radyshenkya.plasmopay.PlasmoPayClient;
import net.radyshenkya.plasmopay.gui.Message.MessageGui;
import net.radyshenkya.plasmopay.gui.Message.MessageScreen;
import net.radyshenkya.plasmopay.gui.TextPrompt.TextPromptGui;
import net.radyshenkya.plasmopay.gui.TextPrompt.TextPromptScreen;
import net.radyshenkya.plasmopay.plasmo_api.ApiCallException;
import net.radyshenkya.plasmopay.plasmo_api.PlasmoApi;

public class PaymentGui extends LightweightGuiDescription {
    public static final int PANEL_LEFT_RIGHT_MARGIN = 5;
    public static final int WIDGET_HEIGHT = 15;
    public static final int WIDGET_SPACING = 1;
    public static final int PANEL_WIDTH = 150;
    public static final int PANEL_HEIGHT = 170;

    private static WBox root;
    private static WTextField cardNameField;
    private static WTextField amountField;
    private static WTextField commentField;
    private static WTextField fromCardNameField;

    public PaymentGui(String cardName, int amount, String paymentComment) {
        root = new WBox(Axis.VERTICAL);
        root.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        root.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.setVerticalAlignment(VerticalAlignment.CENTER);
        root.setSpacing(WIDGET_SPACING);
        setRootPanel(root);

        // Name
        root.add(new WLabel(Text.translatable("gui.plasmopay.text.transfer")),
                PANEL_WIDTH - PANEL_LEFT_RIGHT_MARGIN * 2, WIDGET_HEIGHT);

        // Transfer info
        cardNameField = new WTextField(
                Text.translatable("gui.plasmopay.placeholder.to_card"));
        cardNameField.setText(cardName);

        amountField = new WTextField(Text.translatable("gui.plasmopay.placeholder.amount"));
        amountField.setText(Integer.toString(amount));

        commentField = new WTextField(Text.translatable("gui.plasmopay.placeholder.message"));
        commentField.setText(paymentComment);

        fromCardNameField = new WTextField(Text.translatable("gui.plasmopay.placeholder.from_card"));
        fromCardNameField.setText(PlasmoPayClient.config.lastUsedBankCard());

        root.add(cardNameField, PANEL_WIDTH - PANEL_LEFT_RIGHT_MARGIN * 2, WIDGET_HEIGHT);
        root.add(amountField, PANEL_WIDTH - PANEL_LEFT_RIGHT_MARGIN * 2, WIDGET_HEIGHT);
        root.add(commentField, PANEL_WIDTH - PANEL_LEFT_RIGHT_MARGIN * 2, WIDGET_HEIGHT);
        root.add(fromCardNameField, PANEL_WIDTH - PANEL_LEFT_RIGHT_MARGIN * 2, WIDGET_HEIGHT);

        addSetTokenButton();
        addTransferButton();
    }

    private void addTransferButton() {
        WButton payButton = new WButton(Text.translatable("gui.plasmopay.button.transfer"));

        root.add(payButton, PANEL_WIDTH - PANEL_LEFT_RIGHT_MARGIN * 2, WIDGET_HEIGHT);

        payButton.setOnClick(() -> {
            PlasmoApi.setToken(PlasmoPayClient.config.plasmoRpToken());
            PlasmoPayClient.config.lastUsedBankCard(fromCardNameField.getText());

            Text resultMessageText = null;

            try {
                PlasmoApi.newTransfer(fromCardNameField.getText(),
                        cardNameField.getText(),
                        Integer.parseInt(amountField.getText()),
                        commentField.getText());

                resultMessageText = Text.translatable(
                        "gui.plasmopay.text.successful_transfer",
                        fromCardNameField.getText(),
                        cardNameField.getText(),
                        amountField.getText());
            } catch (ApiCallException apicallExc) {
                resultMessageText = Text.translatable(
                        "gui.plasmopay.text.error",
                        apicallExc.message);

                if (apicallExc.causedBy != null)
                    apicallExc.causedBy.printStackTrace();
            } catch (NumberFormatException parseExc) {
                resultMessageText = Text.translatable(
                        "gui.plasmopay.text.error",
                        Text.translatable("gui.plasmopay.text.error_amount_must_be_integer").getString());
            }

            MinecraftClient.getInstance().setScreen(
                    new MessageScreen(new MessageGui(resultMessageText, 30, false)));
        });
    }

    private void addSetTokenButton() {
        WButton setToken = new WButton(Text.translatable("gui.plasmopay.button.set_token"));

        root.add(setToken, PANEL_WIDTH - PANEL_LEFT_RIGHT_MARGIN * 2, WIDGET_HEIGHT);

        setToken.setOnClick(() -> {
            TextPromptScreen promptScreen = new TextPromptScreen(
                    new TextPromptGui(
                            Text.translatable("gui.plasmopay.text.set_token_prompt_name"),
                            Text.translatable("gui.plasmopay.text.set_token_prompt_placeholder").getString(),
                            "",
                            128,
                            (promptResult) -> {
                                PlasmoPayClient.config.plasmoRpToken(promptResult);
                            }));
            MinecraftClient.getInstance().setScreen(promptScreen);
        });
    }
}
