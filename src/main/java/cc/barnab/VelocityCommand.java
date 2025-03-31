package cc.barnab;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VelocityCommand implements ModInitializer {
	public static final String MOD_ID = "velocity-command";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {;

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(literal("velocity")
					.requires(source -> source.hasPermissionLevel(2))
					.then(argument("target", EntityArgumentType.entities())
						.then(argument("velocity", Vec3ArgumentType.vec3(false))
							.executes(ChangeVelocityCommand::executeCommand)
							.then(argument("rotationContext", StringArgumentType.word()).suggests(new ContextSuggestionProvider())
								.executes(ChangeVelocityCommand::executeCommand)
							)
						)
					)
			);
		});
	}


	static class ContextSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
		@Override
		public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context,
															 SuggestionsBuilder builder) throws CommandSyntaxException {
			builder.suggest("entity");
			builder.suggest("command");
			return builder.buildFuture();
		}
	}
}