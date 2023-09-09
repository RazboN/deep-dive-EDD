package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/depositFunds")
public class DepositFundsController {
    private final Logger logger = Logger.getLogger(DepositFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable(value = "id") String id,
                                                     @RequestBody DepositFundsCommand command) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(
                    new BaseResponse("Deposit Funds request completed successfully."),
                    HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException ex) {
            logger.log(Level.WARNING, MessageFormat.format("A bad request was made - {0}", ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeError = MessageFormat.format("An error was occured during processing to " +
                    "deposit funds with id - {0}", id);
            logger.log(Level.SEVERE, safeError, ex);
            return new ResponseEntity<>(new BaseResponse(safeError),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
