package tk.minersonline.ExampleMod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.minersonline.FabricHello.providers.MessageProvider;

import java.util.Arrays;
import java.util.List;

@Mixin(value = MessageProvider.class, remap = false)
public class MessageMixin {
	@Inject(method = "print", at = @At("RETURN"), cancellable = true)
	private static void print(CallbackInfoReturnable<List<String>> cir) {
		cir.setReturnValue(Arrays.asList("abcd", "qwerty"));
	}
}
