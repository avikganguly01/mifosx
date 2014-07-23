package org.mifosplatform.infrastructure.hooks;

public enum CommandHookType {
    AcceptClientTransfer,
    ActivateCenter,
    ActivateClient,
    ActivateFixedDepositAccount,
    ActivateGroup,
    ActivateHoliday,
    ActivateRecurringDepositAccount,
    ActivateSavingsAccount,
    AddLoanCharge,
    AddSavingsAccountCharge,
    ApplyAnnualFeeSavingsAccount,
    AssignClientStaff,
    AssignGroupStaff,
    AssignRole,
    AssociateClientsToGroup,
    BulkUpdateLoanOfficer,
    CalculateInterestFixedDepositAccount,
    CalculateInterestRecurringDepositAccount,
    CalculateInterestSavingsAccount,
    CloseCenter,
    CloseClient,
    CloseFixedDepositAccount,
    CloseGroup,
    CloseLoanAsRescheduled,
    CloseRecurringDepositAccount,
    CloseSavingsAccount,
    CreateAccountTransfer,
    CreateAccountingRule,
    CreateCalendar,
    CreateCenter,
    CreateChargeDefinition,
    CreateClient,
    CreateClientIdentifier,
    CreateCode,
    CreateCodeValue,
    CreateCollateral,
    CreateDatatable,
    CreateDatatableEntry,
    CreateFixedDepositProduct,
    CreateFund,
    CreateGLAccount,
    CreateGLClosure,
    CreateGroup,
    CreateGuarantor,
    CreateHoliday,
    CreateInterestRateChart,
    CreateInterestRateChartSlab,
    CreateJournalEntry,
    CreateLoanProduct,
    CreateMeeting,
    CreateNote,
    CreateOffice,
    CreateOfficeTransaction,
    CreateProductMix,
    CreateRecurringDepositProduct,
    CreateReport,
    CreateRole,
    CreateSavingsProduct,
    CreateSms,
    CreateStaff,
    CreateStandingInstruction,
    CreateTemplate,
    CreateUser,
    DeleteAccountingRule,
    DeleteCalendar,
    DeleteCenter,
    DeleteChargeDefinition,
    DeleteClient,
    DeleteClientIdentifier,
    DeleteCode,
    DeleteCodeValue,
    DeleteCollateral,
    DeleteDatatable,
    DeleteFinancialActivityAccount,
    DeleteFixedDepositProduct,
    DeleteGLAccount,
    DeleteGLClosure,
    DeleteGroup,
    DeleteGuarantor,
    DeleteHoliday,
    DeleteInterestRateChart,
    DeleteInterestRateChartSlab,
    DeleteLoanCharge,
    DeleteMeeting,
    DeleteNote,
    DeleteOfficeTransaction,
    DeleteOneToManyDatatableEntry,
    DeleteOneToOneDatatableEntry,
    DeleteProductMix,
    DeleteRecurringDepositProduct,
    DeleteReport,
    DeleteSavingsAccountCharge,
    DeleteSavingsProduct,
    DeleteSms,
    DeleteStandingInstruction,
    DeleteTemplate,
    DeleteUser,
    DepositSavingsAccount,
    DisassociateClientsFromGroup,
    DisburseLoan,
    DisburseLoanToSavings,
    ExecutePeriodicAccrual,
    FixedDepositAccountDeposit,
    FixedDepositTransactionAdjustment,
    FullFilSurvey,
    LoanApplicationApproval,
    LoanApplicationApprovalUndo,
    LoanApplicationDeletion,
    LoanApplicationModification,
    LoanApplicationRejected,
    LoanApplicationSubmittal,
    LoanApplicationWithdrawnByApplicant,
    LoanRecoveryPayment,
    LoanRepayment,
    LoanRepaymentAdjustment,
    PayLoanCharge,
    PaySavingsAccountCharge,
    PostInterestFixedDepositAccount,
    PostInterestRecurringDepositAccount,
    PostInterestSavingsAccount,
    PrematureCloseFixedDepositAccount,
    PrematureCloseRecurringDepositAccount,
    ProposeAndAcceptClientTransfer,
    ProposeClientTransfer,
    RecurringDepositAccountDeposit,
    RecurringDepositTransactionAdjustment,
    RegisterDatatable,
    RegisterSurvey,
    RejectClientTransfer,
    RemoveLoanOfficer,
    ReverseJournalEntry,
    SaveCenterCollectionSheet,
    SaveGroupCollectionSheet,
    SavingsAccountApplicationApproval,
    SavingsAccountApplicationApprovalUndo,
    SavingsAccountApplicationRejected,
    SavingsTransactionAdjustment,
    TransferClientsBetweenGroups,
    UnassignClientStaff,
    UnassignGroupStaff,
    UnassignRole,
    UnassignStaffFromCenter,
    UndoDisbursalLoan,
    UndoTransactionFixedDepositAccount,
    UndoTransactionRecurringDepositAccount,
    UndoTransactionSavingsAccount,
    UndoWriteOffLoan,
    UpdateAccountingRule,
    UpdateCalendar,
    UpdateCenter,
    UpdateChargeDefinition,
    UpdateClient,
    UpdateClientIdentifier,
    UpdateClientSavingsAccount,
    UpdateCode,
    UpdateCodeValue,
    UpdateCollateral,
    UpdateCollectionSheet,
    UpdateCurrency,
    UpdateDatatable,
    UpdateFinancialActivityAccount,
    UpdateFixedDepositProduct,
    UpdateFund,
    UpdateGLAccount,
    UpdateGLClosure,
    UpdateGlobalConfiguration,
    UpdateGroup,
    UpdateGroupRole,
    UpdateGuarantor,
    UpdateHoliday,
    UpdateInterestRateChart,
    UpdateInterestRateChartSlab,
    UpdateLikelihood,
    UpdateLoanCharge,
    UpdateLoanDisbuseDate,
    UpdateLoanOfficer,
    UpdateLoanProduct,
    UpdateMakerCheckerPermissions,
    UpdateMeeting,
    UpdateMeetingAttendance,
    UpdateNote,
    UpdateOffice,
    UpdateOneToManyDatatableEntry,
    UpdateOneToOneDatatableEntry,
    UpdateProductMix,
    UpdateRecurringDepositProduct,
    UpdateReport,
    UpdateRole,
    UpdateRolePermissions,
    UpdateRunningBalance,
    UpdateSavingsAccountCharge,
    UpdateSavingsProduct,
    UpdateSms,
    UpdateStaff,
    UpdateStandingInstruction,
    UpdateTaxonomyMapping,
    UpdateTemplate,
    UpdateUser,
    WaiveInterestPortionOnLoan,
    WaiveLoanCharge,
    WaiveSavingsAccountCharge,
    WithdrawClientTransfer,
    WithdrawSavingsAccount,
    WithdrawalFixedDepositAccount,
    WithdrawalRecurringDepositAccount,
    SavingsAccountApplicationSubmittal, FixedDepositAccountApplicationApproval, FixedDepositAccountApplicationDeletion, FixedDepositAccountApplicationWithdrawnByApplicant, FixedDepositAccountApplicationModification, RecurringDepositAccountApplicationSubmittal, RecurringDepositAccountApplicationModification, SavingsAccountApplicationWithdrawnByApplicant, FixedDepositAccountApplicationApprovalUndo, RecurringDepositAccountApplicationWithdrawnByApplicant, SavingsAccountApplicationModification, SavingsAccountApplicationDeletion, FixedDepositAccountApplicationSubmittal, UpdateJobDetail, RecurringDepositAccountApplicationDeletion, RecurringDepositAccountApplicationApproval, FixedDepositAccountApplicationRejected, RecurringDepositAccountApplicationApprovalUndo, RecurringDepositAccountApplicationRejected, CloseLoan, CreateFinancialActivityAccount, WriteOffLoan
}
