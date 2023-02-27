package net.radyshenkya.plasmopay.gui.Payment;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.radyshenkya.plasmopay.PlasmoPayClient;
import net.radyshenkya.plasmopay.gui.Message.MessageScreen;
import net.radyshenkya.plasmopay.gui.TextPrompt.TextPromptScreen;
import net.radyshenkya.plasmopay.plasmo_api.ApiCallException;
import net.radyshenkya.plasmopay.plasmo_api.PlasmoApi;
import net.radyshenkya.plasmopay.Utils;

public class PaymentScreen extends BaseUIModelScreen<FlowLayout> {
        private String toCard;
        private int amount;
        private String transferMessage;

        public PaymentScreen(String cardName, int amount, String paymentComment) {
                super(FlowLayout.class, DataSource.asset(new Identifier("plasmopay:transfer_menu")));
                // super(FlowLayout.class, DataSource.file("transfer_menu.xml"));

                this.toCard = cardName;
                this.amount = amount;
                this.transferMessage = paymentComment;
        }

        @Override
        protected void build(FlowLayout rootComponent) {
                TextBoxComponent toCardText = rootComponent.childById(TextBoxComponent.class, "to-textbox")
                                .text(Utils.clearBankNumber(toCard));
                TextBoxComponent amountText = rootComponent.childById(TextBoxComponent.class, "amount-textbox")
                                .text(Integer.toString(amount));
                TextBoxComponent messageText = rootComponent.childById(TextBoxComponent.class, "message-textbox")
                                .text(transferMessage);
                TextBoxComponent fromCardText = rootComponent.childById(TextBoxComponent.class, "from-textbox")
                                .text(Utils.clearBankNumber(PlasmoPayClient.config.lastUsedBankCard()));

                rootComponent.childById(ButtonComponent.class, "transfer-button").onPress(button -> {
                        PlasmoApi.setToken(PlasmoPayClient.config.plasmoRpToken());
                        PlasmoPayClient.config.lastUsedBankCard(fromCardText.getText());

                        Text resultMessageText = null;
                        Text resultMessageName = null;

                        try {
                                PlasmoApi.newTransfer(
                                                Utils.clearBankNumber(fromCardText.getText()),
                                                Utils.clearBankNumber(toCardText.getText()),
                                                Integer.parseInt(amountText.getText()),
                                                messageText.getText());

                                resultMessageText = Text.translatable(
                                                "gui.plasmopay.text.successful_transfer",
                                                Utils.clearBankNumber(fromCardText.getText()),
                                                Utils.clearBankNumber(toCardText.getText()),
                                                amountText.getText());

                                resultMessageName = Text.translatable("gui.plasmopay.text.success_message_name");
                        } catch (ApiCallException apicallExc) {
                                resultMessageText = Text.translatable(
                                                "gui.plasmopay.text.error",
                                                apicallExc.message);

                                resultMessageName = Text.translatable("gui.plasmopay.text.error_message_name");

                                if (apicallExc.causedBy != null)
                                        apicallExc.causedBy.printStackTrace();
                        } catch (NumberFormatException parseExc) {
                                resultMessageText = Text.translatable(
                                                "gui.plasmopay.text.error",
                                                Text.translatable("gui.plasmopay.text.error_amount_must_be_integer")
                                                                .getString());

                                resultMessageName = Text.translatable("gui.plasmopay.text.error_message_name");
                        }

                        MinecraftClient.getInstance()
                                        .setScreen(new MessageScreen(resultMessageName, resultMessageText));
                });

                rootComponent.childById(ButtonComponent.class, "set-token-button").onPress(button -> {
                        MinecraftClient.getInstance()
                                        .setScreen(new TextPromptScreen(
                                                        Text.translatable("gui.plasmopay.text.set_token_prompt_name"),
                                                        Text.translatable(
                                                                        "gui.plasmopay.text.set_token_prompt_placeholder")
                                                                        .getString(),
                                                        "",
                                                        (promptResult) -> {
                                                                PlasmoPayClient.config.plasmoRpToken(promptResult);
                                                                MinecraftClient.getInstance().setScreen(this);
                                                        }));
                });
        }
}
