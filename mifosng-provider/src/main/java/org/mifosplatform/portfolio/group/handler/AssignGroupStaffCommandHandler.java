/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.portfolio.group.handler;

import org.mifosplatform.commands.handler.CommandHandlerWithHooks;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.hooks.CommandHookType;
import org.mifosplatform.portfolio.group.service.GroupingTypesWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssignGroupStaffCommandHandler extends CommandHandlerWithHooks {

    private final GroupingTypesWritePlatformService groupWritePlatformService;

    @Autowired
    public AssignGroupStaffCommandHandler(final GroupingTypesWritePlatformService groupWritePlatformService) {
        super(CommandHookType.AssignGroupStaff);
        this.groupWritePlatformService = groupWritePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult actualProcessCommand(final JsonCommand command) {
        return this.groupWritePlatformService.assignGroupOrCenterStaff(command.entityId(), command);
    }
}
