package com.album_de_mama.back_end.importvalidation.model;

public record ImportManifestRow(
        int rowNumber,
        String id,
        String title,
        String type,
        String category,
        String date,
        String place,
        String file,
        String thumbnail,
        String description
) {
}
