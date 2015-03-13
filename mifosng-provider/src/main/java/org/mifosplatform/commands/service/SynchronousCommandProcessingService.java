/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.commands.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.mifosplatform.accounting.accrual.handler.ExecutePeriodicAccrualCommandHandler;
import org.mifosplatform.accounting.closure.handler.CreateGLClosureCommandHandler;
import org.mifosplatform.accounting.closure.handler.DeleteGLClosureCommandHandler;
import org.mifosplatform.accounting.closure.handler.UpdateGLClosureCommandHandler;
import org.mifosplatform.accounting.financialactivityaccount.handler.CreateFinancialActivityAccountHandler;
import org.mifosplatform.accounting.financialactivityaccount.handler.DeleteFinancialActivityAccountCommandHandler;
import org.mifosplatform.accounting.financialactivityaccount.handler.UpdateFinancialActivityAccountCommandHandler;
import org.mifosplatform.accounting.glaccount.handler.CreateGLAccountCommandHandler;
import org.mifosplatform.accounting.glaccount.handler.DeleteGLAccountCommandHandler;
import org.mifosplatform.accounting.glaccount.handler.UpdateGLAccountCommandHandler;
import org.mifosplatform.accounting.journalentry.handler.CreateJournalEntryCommandHandler;
import org.mifosplatform.accounting.journalentry.handler.ReverseJournalEntryCommandHandler;
import org.mifosplatform.accounting.journalentry.handler.UpdateRunningBalanceCommandHandler;
import org.mifosplatform.accounting.rule.handler.CreateAccountingRuleCommandHandler;
import org.mifosplatform.accounting.rule.handler.DeleteAccountingRuleCommandHandler;
import org.mifosplatform.accounting.rule.handler.UpdateAccountingRuleCommandHandler;
import org.mifosplatform.commands.domain.CommandSource;
import org.mifosplatform.commands.domain.CommandSourceRepository;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.exception.RollbackTransactionAsCommandIsNotApprovedByCheckerException;
import org.mifosplatform.commands.exception.UnsupportedCommandException;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.cache.command.UpdateCacheCommandHandler;
import org.mifosplatform.infrastructure.codes.handler.CreateCodeCommandHandler;
import org.mifosplatform.infrastructure.codes.handler.CreateCodeValueCommandHandler;
import org.mifosplatform.infrastructure.codes.handler.DeleteCodeCommandHandler;
import org.mifosplatform.infrastructure.codes.handler.DeleteCodeValueCommandHandler;
import org.mifosplatform.infrastructure.codes.handler.UpdateCodeCommandHandler;
import org.mifosplatform.infrastructure.codes.handler.UpdateCodeValueCommandHandler;
import org.mifosplatform.infrastructure.configuration.domain.ConfigurationDomainService;
import org.mifosplatform.infrastructure.configuration.handler.UpdateGlobalConfigurationCommandHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.serialization.ToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.infrastructure.dataqueries.handler.CreateDatatableCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.CreateDatatableEntryCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.CreateReportCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.DeleteDatatableCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.DeleteOneToManyDatatableEntryCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.DeleteOneToOneDatatableEntryCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.DeleteReportCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.RegisterDatatableCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.UpdateDatatableCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.UpdateOneToManyDatatableEntryCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.UpdateOneToOneDatatableEntryCommandHandler;
import org.mifosplatform.infrastructure.dataqueries.handler.UpdateReportCommandHandler;
import org.mifosplatform.infrastructure.hooks.event.HookEvent;
import org.mifosplatform.infrastructure.hooks.event.HookEventSource;
import org.mifosplatform.infrastructure.hooks.handler.CreateHookCommandHandler;
import org.mifosplatform.infrastructure.hooks.handler.DeleteHookCommandHandler;
import org.mifosplatform.infrastructure.hooks.handler.UpdateHookCommandHandler;
import org.mifosplatform.infrastructure.jobs.handler.UpdateJobDetailCommandhandler;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.infrastructure.sms.handler.CreateSmsCommandHandler;
import org.mifosplatform.infrastructure.sms.handler.DeleteSmsCommandHandler;
import org.mifosplatform.infrastructure.sms.handler.UpdateSmsCommandHandler;
import org.mifosplatform.infrastructure.survey.handler.FullFilSurveyCommandHandler;
import org.mifosplatform.infrastructure.survey.handler.RegisterSurveyCommandHandler;
import org.mifosplatform.infrastructure.survey.handler.UpdateLikelihoodCommandHandler;
import org.mifosplatform.mix.handler.UpdateTaxonomyMappingCommandHandler;
import org.mifosplatform.organisation.holiday.handler.ActivateHolidayCommandHandler;
import org.mifosplatform.organisation.holiday.handler.CreateHolidayCommandHandler;
import org.mifosplatform.organisation.holiday.handler.DeleteHolidayCommandHandler;
import org.mifosplatform.organisation.holiday.handler.UpdateHolidayCommandHandler;
import org.mifosplatform.organisation.monetary.handler.UpdateCurrencyCommandHandler;
import org.mifosplatform.organisation.office.handler.CreateOfficeCommandHandler;
import org.mifosplatform.organisation.office.handler.CreateOfficeTransactionCommandHandler;
import org.mifosplatform.organisation.office.handler.DeleteOfficeTransactionCommandHandler;
import org.mifosplatform.organisation.office.handler.UpdateOfficeCommandHandler;
import org.mifosplatform.organisation.staff.handler.CreateStaffCommandHandler;
import org.mifosplatform.organisation.staff.handler.UpdateStaffCommandHandler;
import org.mifosplatform.portfolio.account.handler.CreateAccountTransferCommandHandler;
import org.mifosplatform.portfolio.account.handler.CreateStandingInstructionCommandHandler;
import org.mifosplatform.portfolio.account.handler.DeleteStandingInstructionCommandHandler;
import org.mifosplatform.portfolio.account.handler.UpdateStandingInstructionCommandHandler;
import org.mifosplatform.portfolio.calendar.handler.CreateCalendarCommandHandler;
import org.mifosplatform.portfolio.calendar.handler.DeleteCalendarCommandHandler;
import org.mifosplatform.portfolio.calendar.handler.UpdateCalendarCommandHandler;
import org.mifosplatform.portfolio.charge.handler.CreateChargeDefinitionCommandHandler;
import org.mifosplatform.portfolio.charge.handler.DeleteChargeDefinitionCommandHandler;
import org.mifosplatform.portfolio.charge.handler.UpdateChargeDefinitionCommandHandler;
import org.mifosplatform.portfolio.client.handler.ActivateClientCommandHandler;
import org.mifosplatform.portfolio.client.handler.AssignClientStaffCommandHandler;
import org.mifosplatform.portfolio.client.handler.CloseClientCommandHandler;
import org.mifosplatform.portfolio.client.handler.CreateClientCommandHandler;
import org.mifosplatform.portfolio.client.handler.CreateClientIdentifierCommandHandler;
import org.mifosplatform.portfolio.client.handler.DeleteClientCommandHandler;
import org.mifosplatform.portfolio.client.handler.DeleteClientIdentifierCommandHandler;
import org.mifosplatform.portfolio.client.handler.UnassignClientStaffCommandHandler;
import org.mifosplatform.portfolio.client.handler.UpdateClientCommandHandler;
import org.mifosplatform.portfolio.client.handler.UpdateClientIdentifierCommandHandler;
import org.mifosplatform.portfolio.client.handler.UpdateClientSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.collateral.handler.CreateCollateralCommandHandler;
import org.mifosplatform.portfolio.collateral.handler.DeleteCollateralCommandHandler;
import org.mifosplatform.portfolio.collateral.handler.UpdateCollateralCommandHandler;
import org.mifosplatform.portfolio.collectionsheet.handler.UpdateCollectionSheetCommandHandler;
import org.mifosplatform.portfolio.fund.handler.CreateFundCommandHandler;
import org.mifosplatform.portfolio.fund.handler.UpdateFundCommandHandler;
import org.mifosplatform.portfolio.group.handler.ActivateCenterCommandHandler;
import org.mifosplatform.portfolio.group.handler.ActivateGroupCommandHandler;
import org.mifosplatform.portfolio.group.handler.AssignGroupStaffCommandHandler;
import org.mifosplatform.portfolio.group.handler.AssignRoleCommandHandler;
import org.mifosplatform.portfolio.group.handler.AssociateClientsToGroupCommandHandler;
import org.mifosplatform.portfolio.group.handler.AssociateGroupsToCenterCommandHandler;
import org.mifosplatform.portfolio.group.handler.CloseCenterCommandHandler;
import org.mifosplatform.portfolio.group.handler.CloseGroupCommandHandler;
import org.mifosplatform.portfolio.group.handler.CreateCenterCommandHandler;
import org.mifosplatform.portfolio.group.handler.CreateGroupCommandHandler;
import org.mifosplatform.portfolio.group.handler.DeleteCenterCommandHandler;
import org.mifosplatform.portfolio.group.handler.DeleteGroupCommandHandler;
import org.mifosplatform.portfolio.group.handler.DisassociateClientsFromGroupCommandHandler;
import org.mifosplatform.portfolio.group.handler.DisassociateGroupsFromCenterCommandHandler;
import org.mifosplatform.portfolio.group.handler.SaveCenterCollectionSheetCommandHandler;
import org.mifosplatform.portfolio.group.handler.SaveGroupCollectionSheetCommandHandler;
import org.mifosplatform.portfolio.group.handler.UnassignGroupStaffCommandHandler;
import org.mifosplatform.portfolio.group.handler.UnassignRoleCommandHandler;
import org.mifosplatform.portfolio.group.handler.UpdateCenterCommandHandler;
import org.mifosplatform.portfolio.group.handler.UpdateGroupCommandHandler;
import org.mifosplatform.portfolio.group.handler.UpdateGroupRoleCommandHandler;
import org.mifosplatform.portfolio.interestratechart.handler.CreateInterestRateChartCommandHandler;
import org.mifosplatform.portfolio.interestratechart.handler.CreateInterestRateChartSlabCommandHandler;
import org.mifosplatform.portfolio.interestratechart.handler.DeleteInterestRateChartCommandHandler;
import org.mifosplatform.portfolio.interestratechart.handler.DeleteInterestRateChartSlabCommandHandler;
import org.mifosplatform.portfolio.interestratechart.handler.UpdateInterestRateChartCommandHandler;
import org.mifosplatform.portfolio.interestratechart.handler.UpdateInterestRateChartSlabCommandHandler;
import org.mifosplatform.portfolio.loanaccount.guarantor.handler.CreateGuarantorCommandHandler;
import org.mifosplatform.portfolio.loanaccount.guarantor.handler.DeleteGuarantorCommandHandler;
import org.mifosplatform.portfolio.loanaccount.guarantor.handler.UpdateGuarantorCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.AddLoanChargeCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.BulkUpdateLoanOfficerCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.CloseLoanAsRescheduledCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.CloseLoanCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.DeleteLoanChargeCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.DisburseLoanCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.DisburseLoanToSavingsCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanApplicationApprovalCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanApplicationApprovalUndoCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanApplicationDeletionCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanApplicationModificationCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanApplicationRejectedCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanApplicationSubmittalCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanApplicationWithdrawnByApplicantCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanRecoveryPaymentCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanRepaymentAdjustmentCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.LoanRepaymentCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.PayLoanChargeCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.RemoveLoanOfficerCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.UndoDisbursalLoanCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.UndoWriteOffLoanCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.UpdateLoanChargeCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.UpdateLoanDisbuseDateCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.UpdateLoanOfficerCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.WaiveInterestPortionOnLoanCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.WaiveLoanChargeCommandHandler;
import org.mifosplatform.portfolio.loanaccount.handler.WriteOffLoanCommandHandler;
import org.mifosplatform.portfolio.loanaccount.rescheduleloan.handler.ApproveLoanRescheduleRequestCommandHandler;
import org.mifosplatform.portfolio.loanaccount.rescheduleloan.handler.CreateLoanRescheduleRequestCommandHandler;
import org.mifosplatform.portfolio.loanaccount.rescheduleloan.handler.RejectLoanRescheduleRequestCommandHandler;
import org.mifosplatform.portfolio.loanproduct.handler.CreateLoanProductCommandHandler;
import org.mifosplatform.portfolio.loanproduct.handler.UpdateLoanProductCommandHandler;
import org.mifosplatform.portfolio.loanproduct.productmix.handler.CreateProductMixCommandHandler;
import org.mifosplatform.portfolio.loanproduct.productmix.handler.DeleteProductMixCommandHandler;
import org.mifosplatform.portfolio.loanproduct.productmix.handler.UpdateProductMixCommandHandler;
import org.mifosplatform.portfolio.meeting.handler.CreateMeetingCommandHandler;
import org.mifosplatform.portfolio.meeting.handler.DeleteMeetingCommandHandler;
import org.mifosplatform.portfolio.meeting.handler.UpdateMeetingAttendanceCommandHandler;
import org.mifosplatform.portfolio.meeting.handler.UpdateMeetingCommandHandler;
import org.mifosplatform.portfolio.note.handler.CreateNoteCommandHandler;
import org.mifosplatform.portfolio.note.handler.DeleteNoteCommandHandler;
import org.mifosplatform.portfolio.note.handler.UpdateNoteCommandHandler;
import org.mifosplatform.portfolio.savings.handler.ActivateFixedDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.ActivateRecurringDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.ActivateSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.AddSavingsAccountChargeCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CalculateInterestFixedDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CalculateInterestRecurringDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CalculateInterestSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CloseFixedDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CloseRecurringDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CloseSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CreateFixedDepositProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CreateRecurringDepositProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.CreateSavingsProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.DeleteFixedDepositProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.DeleteRecurringDepositProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.DeleteSavingsAccountChargeCommandHandler;
import org.mifosplatform.portfolio.savings.handler.DeleteSavingsProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.DepositSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountApplicationApprovalCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountApplicationApprovalUndoCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountApplicationDeletionCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountApplicationModificationCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountApplicationRejectedCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountApplicationSubmittalCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountApplicationWithdrawnByApplicantCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositAccountDepositCommandHandler;
import org.mifosplatform.portfolio.savings.handler.FixedDepositTransactionAdjustmentCommandHandler;
import org.mifosplatform.portfolio.savings.handler.InactivateSavingsAccountChargeCommandHandler;
import org.mifosplatform.portfolio.savings.handler.PaySavingsAccountChargeCommandHandler;
import org.mifosplatform.portfolio.savings.handler.PostInterestFixedDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.PostInterestRecurringDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.PostInterestSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.PrematureCloseFixedDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.PrematureCloseRecurringDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountApplicationApprovalCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountApplicationApprovalUndoCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountApplicationDeletionCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountApplicationModificationCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountApplicationRejectedCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountApplicationSubmittalCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountApplicationWithdrawnByApplicantCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositAccountDepositCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RecurringDepositTransactionAdjustmentCommandHandler;
import org.mifosplatform.portfolio.savings.handler.RemoveSavingsOfficerCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsAccountApplicationApprovalCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsAccountApplicationApprovalUndoCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsAccountApplicationDeletionCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsAccountApplicationModificationCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsAccountApplicationRejectedCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsAccountApplicationSubmittalCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsAccountApplicationWithdrawnByApplicantCommandHandler;
import org.mifosplatform.portfolio.savings.handler.SavingsTransactionAdjustmentCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UndoTransactionFixedDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UndoTransactionRecurringDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UndoTransactionSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UpdateFixedDepositProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UpdateRecurringDepositProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UpdateSavingsAccountChargeCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UpdateSavingsOfficerCommandHandler;
import org.mifosplatform.portfolio.savings.handler.UpdateSavingsProductCommandHandler;
import org.mifosplatform.portfolio.savings.handler.WaiveSavingsAccountChargeCommandHandler;
import org.mifosplatform.portfolio.savings.handler.WithdrawSavingsAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.WithdrawalFixedDepositAccountCommandHandler;
import org.mifosplatform.portfolio.savings.handler.WithdrawalRecurringDepositAccountCommandHandler;
import org.mifosplatform.portfolio.transfer.handler.AcceptClientTransferCommandHandler;
import org.mifosplatform.portfolio.transfer.handler.ProposeAndAcceptClientTransferCommandHandler;
import org.mifosplatform.portfolio.transfer.handler.ProposeClientTransferCommandHandler;
import org.mifosplatform.portfolio.transfer.handler.RejectClientTransferCommandHandler;
import org.mifosplatform.portfolio.transfer.handler.TransferClientsBetweenGroupsCommandHandler;
import org.mifosplatform.portfolio.transfer.handler.WithdrawClientTransferCommandHandler;
import org.mifosplatform.template.handler.CreateTemplateCommandHandler;
import org.mifosplatform.template.handler.DeleteTemplateCommandHandler;
import org.mifosplatform.template.handler.UpdateTemplateCommandHandler;
import org.mifosplatform.useradministration.domain.AppUser;
import org.mifosplatform.useradministration.handler.CreateRoleCommandHandler;
import org.mifosplatform.useradministration.handler.CreateUserCommandHandler;
import org.mifosplatform.useradministration.handler.DeleteUserCommandHandler;
import org.mifosplatform.useradministration.handler.UpdateMakerCheckerPermissionsCommandHandler;
import org.mifosplatform.useradministration.handler.UpdateRoleCommandHandler;
import org.mifosplatform.useradministration.handler.UpdateRolePermissionsCommandHandler;
import org.mifosplatform.useradministration.handler.UpdateUserCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SynchronousCommandProcessingService implements CommandProcessingService {

    private PlatformSecurityContext context;
    private final ApplicationContext applicationContext;
    private final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer;
    private final ToApiJsonSerializer<CommandProcessingResult> toApiResultJsonSerializer;
    private CommandSourceRepository commandSourceRepository;
    private final ConfigurationDomainService configurationDomainService;

    @Autowired
    public SynchronousCommandProcessingService(final PlatformSecurityContext context, final ApplicationContext applicationContext,
            final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer,
            final ToApiJsonSerializer<CommandProcessingResult> toApiResultJsonSerializer,
            final CommandSourceRepository commandSourceRepository,
            final ConfigurationDomainService configurationDomainService) {
        this.context = context;
        this.context = context;
        this.applicationContext = applicationContext;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.toApiResultJsonSerializer = toApiResultJsonSerializer;
        this.commandSourceRepository = commandSourceRepository;
        this.commandSourceRepository = commandSourceRepository;
        this.configurationDomainService = configurationDomainService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processAndLogCommand(final CommandWrapper wrapper, final JsonCommand command,
            final boolean isApprovedByChecker) {
    	
        final boolean rollbackTransaction = this.configurationDomainService.isMakerCheckerEnabledForTask(wrapper.taskPermissionName());
        final NewCommandSourceHandler handler = findCommandHandler(wrapper);
        final CommandProcessingResult result = handler.processCommand(command);

        final AppUser maker = this.context.authenticatedUser(wrapper);

        CommandSource commandSourceResult = null;
        if (command.commandId() != null) {
            commandSourceResult = this.commandSourceRepository.findOne(command.commandId());
            commandSourceResult.markAsChecked(maker, DateTime.now());
        } else {
            commandSourceResult = CommandSource.fullEntryFrom(wrapper, command, maker);
        }
        commandSourceResult.updateResourceId(result.resourceId());
        commandSourceResult.updateForAudit(result.getOfficeId(), result.getGroupId(), result.getClientId(), result.getLoanId(),
                result.getSavingsId(), result.getProductId(), result.getTransactionId());

        String changesOnlyJson = null;
        if (result.hasChanges()) {
            changesOnlyJson = this.toApiJsonSerializer.serializeResult(result.getChanges());
            commandSourceResult.updateJsonTo(changesOnlyJson);
        }

        if (!result.hasChanges() && wrapper.isUpdateOperation() && !wrapper.isUpdateDatatable()) {
            commandSourceResult.updateJsonTo(null);
        }

        if (commandSourceResult.hasJson()) {
            this.commandSourceRepository.save(commandSourceResult);
        }

        if ((rollbackTransaction || result.isRollbackTransaction()) && !isApprovedByChecker) {
            /*
             * JournalEntry will generate a new transactionId every time.
             * Updating the transactionId with old transactionId, because as
             * there are no entries are created with new transactionId, will
             * throw an error when checker approves the transaction
             */
            commandSourceResult.updateTransaction(command.getTransactionId());
            /*
             * Update CommandSource json data with JsonCommand json data, line
             * 77 and 81 may update the json data
             */
            commandSourceResult.updateJsonTo(command.json());
            throw new RollbackTransactionAsCommandIsNotApprovedByCheckerException(commandSourceResult);
        }
        result.setRollbackTransaction(null);
        
        publishEvent(wrapper.entityName(), wrapper.actionName(), result);
        
        return result;
    }

    @Transactional
    @Override
    public CommandProcessingResult logCommand(CommandSource commandSourceResult) {

        commandSourceResult.markAsAwaitingApproval();
        commandSourceResult = this.commandSourceRepository.save(commandSourceResult);

        return new CommandProcessingResultBuilder().withCommandId(commandSourceResult.getId())
                .withEntityId(commandSourceResult.getResourceId()).build();
    }

    private NewCommandSourceHandler findCommandHandler(final CommandWrapper wrapper) {
        Map<String, ? extends NewCommandSourceHandler> handlers = null;
        
        if (wrapper.isAccountTransferResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateAccountTransferCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isConfigurationResource()) {
        	handlers = this.applicationContext.getBeansOfType(UpdateGlobalConfigurationCommandHandler.class);
        } else if (wrapper.isDatatableResource()) {
            if (wrapper.isCreateDatatable()) {
            	handlers = this.applicationContext.getBeansOfType(CreateDatatableCommandHandler.class);
            } else if (wrapper.isDeleteDatatable()) {
            	handlers = this.applicationContext.getBeansOfType(DeleteDatatableCommandHandler.class);
            } else if (wrapper.isUpdateDatatable()) {
            	handlers = this.applicationContext.getBeansOfType(UpdateDatatableCommandHandler.class);
            } else if (wrapper.isCreate()) {
            	handlers = this.applicationContext.getBeansOfType(CreateDatatableEntryCommandHandler.class);
            } else if (wrapper.isUpdateMultiple()) {
                handlers = this.applicationContext.getBeansOfType(UpdateOneToManyDatatableEntryCommandHandler.class);
            } else if (wrapper.isUpdateOneToOne()) {
                handlers = this.applicationContext.getBeansOfType(UpdateOneToOneDatatableEntryCommandHandler.class);
            } else if (wrapper.isDeleteMultiple()) {
                handlers = this.applicationContext.getBeansOfType(DeleteOneToManyDatatableEntryCommandHandler.class);
            } else if (wrapper.isDeleteOneToOne()) {
                handlers = this.applicationContext.getBeansOfType(DeleteOneToOneDatatableEntryCommandHandler.class);
            } else if (wrapper.isRegisterDatatable()) {
                handlers = this.applicationContext.getBeansOfType(RegisterDatatableCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isNoteResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateNoteCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateNoteCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteNoteCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isClientIdentifierResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateClientIdentifierCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateClientIdentifierCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteClientIdentifierCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isClientResource() && !wrapper.isClientNoteResource() && !wrapper.isClientIdentifierResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateClientCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateClientCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteClientCommandHandler.class);
            } else if (wrapper.isClientActivation()) {
                handlers = this.applicationContext.getBeansOfType(ActivateClientCommandHandler.class);
            } else if (wrapper.isClientUnassignStaff()) {
                handlers = this.applicationContext.getBeansOfType(UnassignClientStaffCommandHandler.class);
            } else if (wrapper.isClientAssignStaff()) {
                handlers = this.applicationContext.getBeansOfType(AssignClientStaffCommandHandler.class);
            } else if (wrapper.isClientClose()) {
                handlers = this.applicationContext.getBeansOfType(CloseClientCommandHandler.class);
            } else if (wrapper.isProposeClientTransfer()) {
                handlers = this.applicationContext.getBeansOfType(ProposeClientTransferCommandHandler.class);
            } else if (wrapper.isProposeAndAcceptClientTransfer()) {
                handlers = this.applicationContext.getBeansOfType(ProposeAndAcceptClientTransferCommandHandler.class);
            } else if (wrapper.isWithdrawClientTransfer()) {
                handlers = this.applicationContext.getBeansOfType(WithdrawClientTransferCommandHandler.class);
            } else if (wrapper.isAcceptClientTransfer()) {
                handlers = this.applicationContext.getBeansOfType(AcceptClientTransferCommandHandler.class);
            } else if (wrapper.isRejectClientTransfer()) {
                handlers = this.applicationContext.getBeansOfType(RejectClientTransferCommandHandler.class);
            } else if (wrapper.isUpdateClientSavingsAccount()) {
                handlers = this.applicationContext.getBeansOfType(UpdateClientSavingsAccountCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
            // end of client
        } else if (wrapper.isUpdateRolePermissions()) {
            handlers = this.applicationContext.getBeansOfType(UpdateRolePermissionsCommandHandler.class);
        } else if (wrapper.isPermissionResource()) {
            handlers = this.applicationContext.getBeansOfType(UpdateMakerCheckerPermissionsCommandHandler.class);
        } else if (wrapper.isRoleResource()) {

            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateRoleCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateRoleCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }

        } else if (wrapper.isUserResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateUserCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateUserCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteUserCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isHookResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateHookCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateHookCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteHookCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isStaffResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateStaffCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateStaffCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isGuarantorResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateGuarantorCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateGuarantorCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteGuarantorCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCollateralResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateCollateralCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateCollateralCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteCollateralCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCodeResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateCodeCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateCodeCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteCodeCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCodeValueResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateCodeValueCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateCodeValueCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteCodeValueCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isSmsResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateSmsCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateSmsCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteSmsCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCurrencyResource()) {
            handlers = this.applicationContext.getBeansOfType(UpdateCurrencyCommandHandler.class);
        } else if (wrapper.isFundResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateFundCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateFundCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isOfficeResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateOfficeCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateOfficeCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isOfficeTransactionResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateOfficeTransactionCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteOfficeTransactionCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isChargeDefinitionResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateChargeDefinitionCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateChargeDefinitionCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteChargeDefinitionCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isLoanProductResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateLoanProductCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateLoanProductCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isLoanResource()) {
            if (wrapper.isApproveLoanApplication()) {
                handlers = this.applicationContext.getBeansOfType(LoanApplicationApprovalCommandHandler.class);
            } else if (wrapper.isUndoApprovalOfLoanApplication()) {
                handlers = this.applicationContext.getBeansOfType(LoanApplicationApprovalUndoCommandHandler.class);
            } else if (wrapper.isApplicantWithdrawalFromLoanApplication()) {
                handlers = this.applicationContext.getBeansOfType(LoanApplicationWithdrawnByApplicantCommandHandler.class);
            } else if (wrapper.isRejectionOfLoanApplication()) {
                handlers = this.applicationContext.getBeansOfType(LoanApplicationRejectedCommandHandler.class);
            } else if (wrapper.isDisbursementOfLoan()) {
                handlers = this.applicationContext.getBeansOfType(DisburseLoanCommandHandler.class);
            } else if (wrapper.isDisbursementOfLoanToSavings()) {
                handlers = this.applicationContext.getBeansOfType(DisburseLoanToSavingsCommandHandler.class);
            } else if (wrapper.isUndoDisbursementOfLoan()) {
                handlers = this.applicationContext.getBeansOfType(UndoDisbursalLoanCommandHandler.class);
            } else if (wrapper.isLoanRepayment()) {
                handlers = this.applicationContext.getBeansOfType(LoanRepaymentCommandHandler.class);
            } else if (wrapper.isLoanRecoveryPayment()) {
                handlers = this.applicationContext.getBeansOfType(LoanRecoveryPaymentCommandHandler.class);
            } else if (wrapper.isLoanRepaymentAdjustment()) {
                handlers = this.applicationContext.getBeansOfType(LoanRepaymentAdjustmentCommandHandler.class);
            } else if (wrapper.isWaiveInterestPortionOnLoan()) {
                handlers = this.applicationContext.getBeansOfType(WaiveInterestPortionOnLoanCommandHandler.class);
            } else if (wrapper.isLoanWriteOff()) {
                handlers = this.applicationContext.getBeansOfType(WriteOffLoanCommandHandler.class);
            } else if (wrapper.isCloseLoanAsObligationsMet()) {
                handlers = this.applicationContext.getBeansOfType(CloseLoanCommandHandler.class);
            } else if (wrapper.isCloseLoanAsRescheduled()) {
                handlers = this.applicationContext.getBeansOfType(CloseLoanAsRescheduledCommandHandler.class);
            } else if (wrapper.isUpdateLoanOfficer()) {
                handlers = this.applicationContext.getBeansOfType(UpdateLoanOfficerCommandHandler.class);
            } else if (wrapper.isRemoveLoanOfficer()) {
                handlers = this.applicationContext.getBeansOfType(RemoveLoanOfficerCommandHandler.class);
            } else if (wrapper.isBulkUpdateLoanOfficer()) {
                handlers = this.applicationContext.getBeansOfType(BulkUpdateLoanOfficerCommandHandler.class);
            } else if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(LoanApplicationSubmittalCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(LoanApplicationModificationCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(LoanApplicationDeletionCommandHandler.class);
            } else if (wrapper.isUndoLoanWriteOff()) {
                handlers = this.applicationContext.getBeansOfType(UndoWriteOffLoanCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isLoanChargeResource()) {
            if (wrapper.isAddLoanCharge()) {
                handlers = this.applicationContext.getBeansOfType(AddLoanChargeCommandHandler.class);
            } else if (wrapper.isDeleteLoanCharge()) {
                handlers = this.applicationContext.getBeansOfType(DeleteLoanChargeCommandHandler.class);
            } else if (wrapper.isUpdateLoanCharge()) {
                handlers = this.applicationContext.getBeansOfType(UpdateLoanChargeCommandHandler.class);
            } else if (wrapper.isWaiveLoanCharge()) {
                handlers = this.applicationContext.getBeansOfType(WaiveLoanChargeCommandHandler.class);
            } else if (wrapper.isPayLoanCharge()) {
                handlers = this.applicationContext.getBeansOfType(PayLoanChargeCommandHandler.class);
            }
        } else if (wrapper.isLoanDisburseDetailResource()) {
            if (wrapper.isUpdateDisbursementDate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateLoanDisbuseDateCommandHandler.class);
            }
        } else if (wrapper.isGLAccountResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateGLAccountCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateGLAccountCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteGLAccountCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isGLClosureResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateGLClosureCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateGLClosureCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteGLClosureCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isJournalEntryResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateJournalEntryCommandHandler.class);
            } else if (wrapper.isRevertJournalEntry()) {
                handlers = this.applicationContext.getBeansOfType(ReverseJournalEntryCommandHandler.class);
            } else if (wrapper.isUpdateRunningbalance()) {
                handlers = this.applicationContext.getBeansOfType(UpdateRunningBalanceCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isPeriodicAccrualResource()) {
            if (wrapper.isExecute()) {
                handlers = this.applicationContext.getBeansOfType(ExecutePeriodicAccrualCommandHandler.class);
            }
        } else if (wrapper.isSavingsProductResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateSavingsProductCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateSavingsProductCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteSavingsProductCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isFixedDepositProductResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateFixedDepositProductCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateFixedDepositProductCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteFixedDepositProductCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isRecurringDepositProductResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateRecurringDepositProductCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateRecurringDepositProductCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteRecurringDepositProductCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isSavingsAccountResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext
                        .getBeansOfType(SavingsAccountApplicationSubmittalCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(SavingsAccountApplicationModificationCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(SavingsAccountApplicationDeletionCommandHandler.class);
            } else if (wrapper.isRejectionOfSavingsAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(SavingsAccountApplicationRejectedCommandHandler.class);
            } else if (wrapper.isWithdrawFromSavingsAccountApplicationByApplicant()) {
                handlers = this.applicationContext.getBeansOfType(SavingsAccountApplicationWithdrawnByApplicantCommandHandler.class);
            } else if (wrapper.isApprovalOfSavingsAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(SavingsAccountApplicationApprovalCommandHandler.class);
            } else if (wrapper.isUndoApprovalOfSavingsAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(SavingsAccountApplicationApprovalUndoCommandHandler.class);
            } else if (wrapper.isSavingsAccountDeposit()) {
                handlers = this.applicationContext.getBeansOfType(DepositSavingsAccountCommandHandler.class);
            } else if (wrapper.isSavingsAccountWithdrawal()) {
                handlers = this.applicationContext.getBeansOfType(WithdrawSavingsAccountCommandHandler.class);
            } else if (wrapper.isSavingsAccountActivation()) {
                handlers = this.applicationContext.getBeansOfType(ActivateSavingsAccountCommandHandler.class);
            } else if (wrapper.isSavingsAccountInterestCalculation()) {
                handlers = this.applicationContext.getBeansOfType(CalculateInterestSavingsAccountCommandHandler.class);
            } else if (wrapper.isSavingsAccountInterestPosting()) {
                handlers = this.applicationContext.getBeansOfType(PostInterestSavingsAccountCommandHandler.class);
            } /*
               * else if (wrapper.isSavingsAccountApplyAnnualFee()) { handlers =
               * this.applicationContext.getBeansOfType(
               * "applyAnnualFeeSavingsAccountCommandHandler",
               * NewCommandSourceHandler.class); }
               */else if (wrapper.isSavingsAccountUndoTransaction()) {
                handlers = this.applicationContext.getBeansOfType(UndoTransactionSavingsAccountCommandHandler.class);
            } else if (wrapper.isAdjustSavingsAccountTransaction()) {
                handlers = this.applicationContext.getBeansOfType(SavingsTransactionAdjustmentCommandHandler.class);
            } else if (wrapper.isSavingsAccountClose()) {
                handlers = this.applicationContext.getBeansOfType(CloseSavingsAccountCommandHandler.class);
            }else if(wrapper.isUpdateSavingsOfficer()) {
            	handlers = this.applicationContext.getBeansOfType(UpdateSavingsOfficerCommandHandler.class);
            }else if(wrapper.isRemoveSavingsOfficer()) {
            	handlers = this.applicationContext.getBeansOfType(RemoveSavingsOfficerCommandHandler.class);
            }
            else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isSavingsAccountChargeResource()) {
            if (wrapper.isAddSavingsAccountCharge()) {
                handlers = this.applicationContext.getBeansOfType(AddSavingsAccountChargeCommandHandler.class);
            } else if (wrapper.isDeleteSavingsAccountCharge()) {
                handlers = this.applicationContext.getBeansOfType(DeleteSavingsAccountChargeCommandHandler.class);
            } else if (wrapper.isUpdateSavingsAccountCharge()) {
                handlers = this.applicationContext.getBeansOfType(UpdateSavingsAccountChargeCommandHandler.class);
            } else if (wrapper.isWaiveSavingsAccountCharge()) {
                handlers = this.applicationContext.getBeansOfType(WaiveSavingsAccountChargeCommandHandler.class);
            } else if (wrapper.isPaySavingsAccountCharge()) {
                handlers = this.applicationContext.getBeansOfType(PaySavingsAccountChargeCommandHandler.class);
            } else if (wrapper.isInactivateSavingsAccountCharge()) {
                handlers = this.applicationContext.getBeansOfType(InactivateSavingsAccountChargeCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isFixedDepositAccountResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountApplicationSubmittalCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountApplicationModificationCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountApplicationDeletionCommandHandler.class);
            } else if (wrapper.isRejectionOfFixedDepositAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountApplicationRejectedCommandHandler.class);
            } else if (wrapper.isWithdrawFixedDepositAccountApplicationByApplicant()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountApplicationWithdrawnByApplicantCommandHandler.class);
            } else if (wrapper.isApprovalOfFixedDepositAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountApplicationApprovalCommandHandler.class);
            } else if (wrapper.isUndoApprovalOfFixedDepositAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountApplicationApprovalUndoCommandHandler.class);
            } else if (wrapper.isDeposit()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositAccountDepositCommandHandler.class);
            } else if (wrapper.isWithdrawal()) {
                handlers = this.applicationContext.getBeansOfType(WithdrawalFixedDepositAccountCommandHandler.class);
            } else if (wrapper.isActivation()) {
                handlers = this.applicationContext.getBeansOfType(ActivateFixedDepositAccountCommandHandler.class);
            } else if (wrapper.isInterestCalculation()) {
                handlers = this.applicationContext.getBeansOfType(CalculateInterestFixedDepositAccountCommandHandler.class);
            } else if (wrapper.isInterestPosting()) {
                handlers = this.applicationContext.getBeansOfType(PostInterestFixedDepositAccountCommandHandler.class);
            } else if (wrapper.isUndoTransaction()) {
                handlers = this.applicationContext
                        .getBeansOfType(UndoTransactionFixedDepositAccountCommandHandler.class);
            } else if (wrapper.isAdjustTransaction()) {
                handlers = this.applicationContext.getBeansOfType(FixedDepositTransactionAdjustmentCommandHandler.class);
            } else if (wrapper.isDepositAccountClose()) {
                handlers = this.applicationContext.getBeansOfType(CloseFixedDepositAccountCommandHandler.class);
            } else if (wrapper.isDepositAccountPrematureClose()) {
                handlers = this.applicationContext.getBeansOfType(PrematureCloseFixedDepositAccountCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isRecurringDepositAccountResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountApplicationSubmittalCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountApplicationModificationCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountApplicationDeletionCommandHandler.class);
            } else if (wrapper.isRejectionOfRecurringDepositAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountApplicationRejectedCommandHandler.class);
            } else if (wrapper.isWithdrawRecurringDepositAccountApplicationByApplicant()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountApplicationWithdrawnByApplicantCommandHandler.class);
            } else if (wrapper.isApprovalOfRecurringDepositAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountApplicationApprovalCommandHandler.class);
            } else if (wrapper.isUndoApprovalOfRecurringDepositAccountApplication()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountApplicationApprovalUndoCommandHandler.class);
            } else if (wrapper.isDeposit()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositAccountDepositCommandHandler.class);
            } else if (wrapper.isWithdrawal()) {
                handlers = this.applicationContext.getBeansOfType(WithdrawalRecurringDepositAccountCommandHandler.class);
            } else if (wrapper.isActivation()) {
                handlers = this.applicationContext.getBeansOfType(ActivateRecurringDepositAccountCommandHandler.class);
            } else if (wrapper.isInterestCalculation()) {
                handlers = this.applicationContext.getBeansOfType(CalculateInterestRecurringDepositAccountCommandHandler.class);
            } else if (wrapper.isInterestPosting()) {
                handlers = this.applicationContext.getBeansOfType(PostInterestRecurringDepositAccountCommandHandler.class);
            } else if (wrapper.isUndoTransaction()) {
                handlers = this.applicationContext.getBeansOfType(UndoTransactionRecurringDepositAccountCommandHandler.class);
            } else if (wrapper.isAdjustTransaction()) {
                handlers = this.applicationContext.getBeansOfType(RecurringDepositTransactionAdjustmentCommandHandler.class);
            } else if (wrapper.isDepositAccountClose()) {
                handlers = this.applicationContext.getBeansOfType(CloseRecurringDepositAccountCommandHandler.class);
            } else if (wrapper.isDepositAccountPrematureClose()) {
                handlers = this.applicationContext.getBeansOfType(PrematureCloseRecurringDepositAccountCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isInterestRateChartResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateInterestRateChartCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateInterestRateChartCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteInterestRateChartCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isInterestRateChartSlabResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateInterestRateChartSlabCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateInterestRateChartSlabCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteInterestRateChartSlabCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCalendarResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateCalendarCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateCalendarCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteCalendarCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isGroupResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateGroupCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateGroupCommandHandler.class);
            } else if (wrapper.isUnassignStaff()) {
                handlers = this.applicationContext.getBeansOfType(UnassignGroupStaffCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteGroupCommandHandler.class);
            } else if (wrapper.isGroupActivation()) {
                handlers = this.applicationContext.getBeansOfType(ActivateGroupCommandHandler.class);
            } else if (wrapper.isAssociateClients()) {
                handlers = this.applicationContext.getBeansOfType(AssociateClientsToGroupCommandHandler.class);
            } else if (wrapper.isDisassociateClients()) {
                handlers = this.applicationContext.getBeansOfType(DisassociateClientsFromGroupCommandHandler.class);
            } else if (wrapper.isSaveGroupCollectionSheet()) {
                handlers = this.applicationContext.getBeansOfType(SaveGroupCollectionSheetCommandHandler.class);
            } else if (wrapper.isAssignGroupRole()) {
                handlers = this.applicationContext.getBeansOfType(AssignRoleCommandHandler.class);
            } else if (wrapper.isUnAssignGroupRole()) {
                handlers = this.applicationContext.getBeansOfType(UnassignRoleCommandHandler.class);
            } else if (wrapper.isUpdateGroupRole()) {
                handlers = this.applicationContext.getBeansOfType(UpdateGroupRoleCommandHandler.class);
            } else if (wrapper.isAssignStaff()) {
                handlers = this.applicationContext.getBeansOfType(AssignGroupStaffCommandHandler.class);
            } else if (wrapper.isTransferClientsBetweenGroups()) {
                handlers = this.applicationContext.getBeansOfType(TransferClientsBetweenGroupsCommandHandler.class);
            } else if (wrapper.isGroupClose()) {
                handlers = this.applicationContext.getBeansOfType(CloseGroupCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCenterResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateCenterCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateCenterCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteCenterCommandHandler.class);
            } else if (wrapper.isCenterActivation()) {
                handlers = this.applicationContext.getBeansOfType(ActivateCenterCommandHandler.class);
            } else if (wrapper.isSaveCenterCollectionSheet()) {
                handlers = this.applicationContext.getBeansOfType(SaveCenterCollectionSheetCommandHandler.class);
            } else if (wrapper.isCenterClose()) {
                handlers = this.applicationContext.getBeansOfType(CloseCenterCommandHandler.class);
            } else if (wrapper.isCenterDisassociateGroups()) {
                handlers = this.applicationContext.getBeansOfType(DisassociateGroupsFromCenterCommandHandler.class);
            } else if (wrapper.isCenterAssociateGroups()) {
                handlers = this.applicationContext.getBeansOfType(AssociateGroupsToCenterCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCollectionSheetResource()) {
            if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateCollectionSheetCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isReportResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateReportCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateReportCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteReportCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isAccountingRuleResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateAccountingRuleCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateAccountingRuleCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteAccountingRuleCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isXBRLMappingResource()) {
            if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateTaxonomyMappingCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isHolidayResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateHolidayCommandHandler.class);
            } else if (wrapper.isHolidayActivation()) {
                handlers = this.applicationContext.getBeansOfType(ActivateHolidayCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateHolidayCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteHolidayCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isProductMixResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateProductMixCommandHandler.class);
            } else if (wrapper.isUpdateOperation()) {
                handlers = this.applicationContext.getBeansOfType(UpdateProductMixCommandHandler.class);
            } else if (wrapper.isDeleteOperation()) {
                handlers = this.applicationContext.getBeansOfType(DeleteProductMixCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isSchedulerResource()) {
            if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateJobDetailCommandhandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isCacheResource()) {
            if (wrapper.isUpdateOperation()) {
                handlers = this.applicationContext.getBeansOfType(UpdateCacheCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isMeetingResource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateMeetingCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateMeetingCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteMeetingCommandHandler.class);
            } else if (wrapper.isSaveOrUpdateAttendance()) {
                handlers = this.applicationContext.getBeansOfType(UpdateMeetingAttendanceCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isTemplateRessource()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateTemplateCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateTemplateCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteTemplateCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }

        } else if (wrapper.isStandingInstruction()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateStandingInstructionCommandHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateStandingInstructionCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteStandingInstructionCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }

        } else if (wrapper.isFinancialActivityAccountMapping()) {
            if (wrapper.isCreate()) {
                handlers = this.applicationContext.getBeansOfType(CreateFinancialActivityAccountHandler.class);
            } else if (wrapper.isUpdate()) {
                handlers = this.applicationContext.getBeansOfType(UpdateFinancialActivityAccountCommandHandler.class);
            } else if (wrapper.isDelete()) {
                handlers = this.applicationContext.getBeansOfType(DeleteFinancialActivityAccountCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isLikelihoodResource()) {
            if (wrapper.isUpdateLikelihood()) {
                handlers = this.applicationContext.getBeansOfType(UpdateLikelihoodCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isSurveyResource()) {
            if (wrapper.isRegisterSurvey()) {
                handlers = this.applicationContext.getBeansOfType(RegisterSurveyCommandHandler.class);
            } else if (wrapper.isFullFilSurvey()) {
                handlers = this.applicationContext.getBeansOfType(FullFilSurveyCommandHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isLoanRescheduleResource()) { 
        	if (wrapper.isCreate()) {
        		handlers = this.applicationContext.getBeansOfType(CreateLoanRescheduleRequestCommandHandler.class);
        	} else if(wrapper.isApprove()) {
        		handlers = this.applicationContext.getBeansOfType(ApproveLoanRescheduleRequestCommandHandler.class);
        	} else if(wrapper.isReject()) {
        		handlers = this.applicationContext.getBeansOfType(RejectLoanRescheduleRequestCommandHandler.class);
        	} else {
        		throw new UnsupportedCommandException(wrapper.commandName());
        	}
        } else {

            throw new UnsupportedCommandException(wrapper.commandName());
        }
        Collection<? extends NewCommandSourceHandler> handlerCollection = handlers.values();
        List<? extends NewCommandSourceHandler> handlerImplementations = 
        		new ArrayList<>(handlerCollection);
        AnnotationAwareOrderComparator.sort(handlerImplementations);
        if(handlerImplementations.size() > 1)
        	return handlerImplementations.get(handlerImplementations.size() - 1);
        else
        	return  handlerImplementations.get(0);
    }

    @Override
    public boolean validateCommand(final CommandWrapper commandWrapper, final AppUser user) {
        boolean rollbackTransaction = this.configurationDomainService.isMakerCheckerEnabledForTask(commandWrapper.taskPermissionName());
        user.validateHasPermissionTo(commandWrapper.getTaskPermissionName());
        return rollbackTransaction;
    }
    
    private void publishEvent(final String entityName, final String actionName, final CommandProcessingResult result) {
    	
    	final String authToken = ThreadLocalContextUtil.getAuthToken();
    	final String tenantIdentifier = ThreadLocalContextUtil.getTenant().getTenantIdentifier();
    	final AppUser appUser = this.context.authenticatedUser();
    	
    	final HookEventSource hookEventSource = new HookEventSource(entityName, actionName);
    	
    	final String serializedResult = this.toApiResultJsonSerializer.serialize(result);
    	
    	final HookEvent applicationEvent = 
    			new HookEvent(hookEventSource, serializedResult, tenantIdentifier, appUser, authToken);
    			
    	applicationContext.publishEvent(applicationEvent); 
    }
}
