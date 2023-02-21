package net.radyshenkya.plasmopay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.radyshenkya.plasmopay.gui.Payment.PaymentGui;
import net.radyshenkya.plasmopay.gui.Payment.PaymentScreen;

public class PlasmoPayClient implements ClientModInitializer {
    public static final PlasmoPayConfig config = PlasmoPayConfig.createAndLoad();

    @Override
    public void onInitializeClient() {
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
            MinecraftClient.getInstance()
                    .setScreen(new PaymentScreen(new PaymentGui(cardNumber, parsedAmount, comment)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
