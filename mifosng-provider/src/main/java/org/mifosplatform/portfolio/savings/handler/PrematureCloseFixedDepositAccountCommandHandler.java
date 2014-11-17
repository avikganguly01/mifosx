/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.portfolio.savings.handler;

import org.mifosplatform.commands.handler.CommandHandlerWithHooks;
import org.mifosplatform.infrastructure.codehooks.CommandHookType;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.portfolio.savings.service.DepositAccountWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrematureCloseFixedDepositAccountCommandHandler extends CommandHandlerWithHooks {

    private final DepositAccountWritePlatformService depositAccountWritePlatformService;

    @Autowired
    public PrematureCloseFixedDepositAccountCommandHandler(final DepositAccountWritePlatformService depositAccountWritePlatformService) {
        super(CommandHookType.PrematureCloseFixedDepositAccount);
        this.depositAccountWritePlatformService = depositAccountWritePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult actualProcessCommand(final JsonCommand command) {
        return this.depositAccountWritePlatformService.prematureCloseFDAccount(command.entityId(), command);
    }
}