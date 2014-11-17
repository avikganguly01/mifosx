package org.mifosplatform.commands.handler;

import org.mifosplatform.infrastructure.codehooks.CommandHookResult;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface CommandHook {
    public CommandHookResult preHook(JsonCommand command);
    public void postHook(JsonCommand command, CommandProcessingResult result);
}