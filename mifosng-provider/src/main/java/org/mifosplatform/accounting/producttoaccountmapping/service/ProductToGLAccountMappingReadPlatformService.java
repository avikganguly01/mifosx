/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.accounting.producttoaccountmapping.service;

import java.util.List;
import java.util.Map;

import org.mifosplatform.accounting.producttoaccountmapping.data.PaymentTypeToGLAccountMapper;

public interface ProductToGLAccountMappingReadPlatformService {

    Map<String, Object> fetchAccountMappingDetailsForLoanProduct(final Long loanProductId, final Integer accountingType);

    List<PaymentTypeToGLAccountMapper> fetchPaymentTypeToFundSourceMappingsForLoanProduct(final Long loanProductId);
}