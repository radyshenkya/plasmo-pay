package net.radyshenkya.plasmopay;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.radyshenkya.plasmopay.gui.Payment.PaymentScreen;
import net.radyshenkya.plasmopay.PlasmoPayConfig;

public class PlasmoPayClient implements ClientModInitializer {
    public static final PlasmoPayConfig config = PlasmoPayConfig.createAndLoad();
    private static KeyBinding transferGuiKeyBinding;

    @Override
    public void onInitializeClient() {
        registerKeys();
        registerCallbacks();
    }

    private void registerKeys() {
        transferGuiKeyBinding = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.plasmopay.open_transfer_gui",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_U,
                        "category.plasmopay.plasmopay"));
    }

    private void registerCallbacks() {
        // For transfer dialog keybind
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (transferGuiKeyBinding.wasPressed()) {
                openTransferGui("", 1, "");
            }
        });

        // For sign click
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient()) {
                return ActionResult.PASS;
            }

            BlockState block = world.getBlockState(hitResult.getBlockPos());

            if (block.getBlock() instanceof SignBlock) {

                SignBlockEntity signBlockEntity = (SignBlockEntity) world.getBlockEntity(hitResult.getBlockPos());
                if (tryParseSignPayment(signBlockEntity, player)) {
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
    }

    private boolean tryParseSignPayment(SignBlockEntity signBlockEntity, PlayerEntity player) {
        if (!signBlockEntity.getTextOnRow(0, false).getString().startsWith("PlasmoPay")) {
            return false;
        }

        String cardNumber = signBlockEntity.getTextOnRow(1, false).getString();
        String amount = signBlockEntity.getTextOnRow(2, false).getString();
        String comment = signBlockEntity.getTextOnRow(3, false).getString();

        try {
            int parsedAmount = Integer.parseInt(amount);
            openTransferGui(cardNumber, parsedAmount, comment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void openTransferGui(String cardNumber, int amount, String comment) {
        MinecraftClient.getInstance()
                .setScreen(
                        new PaymentScreen(cardNumber, amount, comment));
    }
}
