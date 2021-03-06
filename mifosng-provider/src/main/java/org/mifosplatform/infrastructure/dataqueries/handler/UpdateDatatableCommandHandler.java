/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.infrastructure.dataqueries.handler;

import org.mifosplatform.commands.handler.CommandHandlerWithHooks;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.dataqueries.service.ReadWriteNonCoreDataService;
import org.mifosplatform.infrastructure.hooks.CommandHookType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateDatatableCommandHandler extends CommandHandlerWithHooks {

    private final ReadWriteNonCoreDataService writePlatformService;

    @Autowired
    public UpdateDatatableCommandHandler(final ReadWriteNonCoreDataService writePlatformService) {
        super(CommandHookType.UpdateDatatable);
        this.writePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult actualProcessCommand(final JsonCommand command) {

        final String datatableName = command.getUrl().replaceAll("/datatables/", "");

        this.writePlatformService.updateDatatable(datatableName, command);

        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withResourceIdAsString(datatableName).build();
    }
}