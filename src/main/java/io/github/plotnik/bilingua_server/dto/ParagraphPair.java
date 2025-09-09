package io.github.plotnik.bilingua_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// This record will be used for both JSON responses and request bodies
@Schema(description = "A pair of paragraphs in two languages")
public record ParagraphPair(
    @Schema(description = "Left paragraph content", example = "Hello world")
    String left, 
    @Schema(description = "Right paragraph content", example = "Hola mundo")
    String right
) {
}