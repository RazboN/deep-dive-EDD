package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.RestoreReadDbCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/v1/RestoreReadDb")
public class RestoreReadDbController {
    private  final Logger logger = Logger.getLogger(RestoreReadDbController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDb() {
        try {
            commandDispatcher.send(new RestoreReadDbCommand());
            return new ResponseEntity<>(
                    new BaseResponse("Restore read db completed successfully."),
                    HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            logger.log(Level.WARNING, MessageFormat.format("A bad request was made - {0}",
                    ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeError = "An error was occured during processing to " +
                    "restore read db";
            logger.log(Level.SEVERE, safeError, ex);
            return new ResponseEntity<>(new BaseResponse(safeError),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}