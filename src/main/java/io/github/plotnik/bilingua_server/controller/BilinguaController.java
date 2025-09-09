package io.github.plotnik.bilingua_server.controller;

import io.github.plotnik.bilingua_server.dto.ParagraphPair;
import io.github.plotnik.bilingua_server.service.BilinguaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

@RestController
@Tag(name = "Bilingua API", description = "API for managing bilingual text paragraphs")
public class BilinguaController {

    @Autowired
    private BilinguaService bilinguaService;

    @GetMapping("/ptr")
    @Operation(summary = "Get current pointer position", description = "Returns the current position pointer in the text")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved pointer position")
    public int getPointer() {
        return bilinguaService.getPtr();
    }

    @PostMapping("/ptr")
    @Operation(summary = "Set pointer position", description = "Sets the position pointer in the text to a specific value")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pointer position successfully set"),
        @ApiResponse(responseCode = "400", description = "Invalid pointer value (negative number)"),
        @ApiResponse(responseCode = "500", description = "Internal server error (file operation failed)")
    })
    public ResponseEntity<Void> setPointer(@Parameter(description = "New pointer position") @RequestParam("n") int value) {
        try {
            bilinguaService.setPtr(value);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Return 400 Bad Request for negative values
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build(); // Return 500 for file errors
        }
    }

    @GetMapping("/pars")
    @Operation(summary = "Get paragraph pair", description = "Retrieves a paragraph pair with optional shift from current position")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved paragraph pair")
    public ParagraphPair getParagraphs(@Parameter(description = "Shift from current position (default: 0)") @RequestParam(defaultValue = "0") int shift) {
        return bilinguaService.getPars(shift);
    }

    @PostMapping("/save")
    @Operation(summary = "Save paragraph pair", description = "Saves a paragraph pair to storage")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paragraph pair successfully saved"),
        @ApiResponse(responseCode = "500", description = "Internal server error (file operation failed)")
    })
    public ResponseEntity<Void> saveParagraphs(@Parameter(description = "Paragraph pair to save") @RequestBody ParagraphPair body) {
        try {
            bilinguaService.save(body);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}