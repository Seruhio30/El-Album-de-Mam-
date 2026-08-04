package com.album_de_mama.back_end.importvalidation.model;

public record ImportValidationIssue(
        int rowNumber,
        String field,
        String message
) {
}
