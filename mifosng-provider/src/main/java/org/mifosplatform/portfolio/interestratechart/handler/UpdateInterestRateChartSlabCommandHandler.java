/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.portfolio.interestratechart.handler;

import org.mifosplatform.commands.handler.CommandHandlerWithHooks;
import org.mifosplatform.infrastructure.codehooks.CommandHookType;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.portfolio.interestratechart.service.InterestRateChartSlabWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateInterestRateChartSlabCommandHandler extends CommandHandlerWithHooks {

    private final InterestRateChartSlabWritePlatformService writePlatformService;

    @Autowired
    public UpdateInterestRateChartSlabCommandHandler(final InterestRateChartSlabWritePlatformService writePlatformService) {
        super(CommandHookType.UpdateInterestRateChartSlab);
        this.writePlatformService = writePlatformService;
    }

    @Override
    public CommandProcessingResult actualProcessCommand(final JsonCommand command) {
        //command.subentityId();//returns chart id
        return this.writePlatformService.update(command.entityId(), command.subentityId(), command);
    }
}