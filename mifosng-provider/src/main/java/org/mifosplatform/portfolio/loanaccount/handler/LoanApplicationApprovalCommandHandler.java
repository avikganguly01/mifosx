/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.portfolio.loanaccount.handler;

import org.mifosplatform.commands.handler.CommandHandlerWithHooks;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.hooks.CommandHookType;
import org.mifosplatform.portfolio.loanaccount.service.LoanApplicationWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanApplicationApprovalCommandHandler extends CommandHandlerWithHooks {

    private final LoanApplicationWritePlatformService writePlatformService;

    @Autowired
    public LoanApplicationApprovalCommandHandler(final LoanApplicationWritePlatformService writePlatformService) {
        super(CommandHookType.LoanApplicationApproval);
        this.writePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult actualProcessCommand(final JsonCommand command) {

        return this.writePlatformService.approveApplication(command.entityId(), command);
    }
}