/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.portfolio.savings.handler;

import org.mifosplatform.commands.handler.CommandHandlerWithHooks;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.hooks.CommandHookType;
import org.mifosplatform.portfolio.savings.service.RecurringDepositProductWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateRecurringDepositProductCommandHandler extends CommandHandlerWithHooks {

    private final RecurringDepositProductWritePlatformService writePlatformService;

    @Autowired
    public UpdateRecurringDepositProductCommandHandler(final RecurringDepositProductWritePlatformService writePlatformService) {
        super(CommandHookType.UpdateRecurringDepositProduct);
        this.writePlatformService = writePlatformService;
    }

    @Override
    public CommandProcessingResult actualProcessCommand(final JsonCommand command) {
        return this.writePlatformService.update(command.entityId(), command);
    }
}