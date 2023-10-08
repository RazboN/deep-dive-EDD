//package com.techbank.account.query.handlers;
//
//import com.techbank.account.common.dto.AccountType;
//import com.techbank.account.query.api.queries.*;
//import com.techbank.account.query.domain.AccountRepository;
//import com.techbank.account.query.domain.BankAccount;
//import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@DataJpaTest
//public class AccountEventHandlersTests {
//    @Mock
//    private AccountRepository _repo;
//
//    @MockBean
//    static QueryDispatcher queryDispatcher;
//
//    @MockBean
//    static QueryHandler queryHandler;
//
//    @BeforeAll
//    static void registerHandlers() {
//        queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
//        queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
//        queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
//        queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
//    }
//
//    @Test
//    public void onAccountOpenedEvent() {
//        var bankAccount = BankAccount.builder()
//                .id("1")
//                .accountHolder("Test Holder")
//                .accountType(AccountType.CURRENT)
//                .creationDate(new Date())
//                .balance(100)
//                .build();
//
//        var saveResult = _repo.save(bankAccount);
//
//        assertNotNull(saveResult);
//    }
//}
