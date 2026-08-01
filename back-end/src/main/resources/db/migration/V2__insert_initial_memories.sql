INSERT INTO memories (
    id,
    title,
    type,
    category,
    memory_date,
    place,
    file_path,
    thumbnail,
    description
)
VALUES
    (
        1,
        'Viaje familiar',
        'photo',
        'viajes',
        DATE '2024-03-15',
        'Guanacaste',
        'assets/photos/viaje-familiar.png',
        'assets/photos/viaje-familiar.png',
        'Un día especial en familia durante el viaje.'
    ),
    (
        2,
        'Cumpleaños familiar',
        'video',
        'celebraciones',
        DATE '2025-06-10',
        'Cartago',
        'assets/videos/cumpleanos-familiar.mp4',
        'assets/thumbnails/cumpleanos-familiar.jpg',
        'Celebración de cumpleaños con toda la familia.'
    ),
    (
        3,
        'Tarde en familia',
        'photo',
        'familia',
        DATE '2025-12-20',
        'San José',
        'assets/photos/tarde-en-familia.jpg',
        'assets/photos/tarde-en-familia.jpg',
        'Una tarde tranquila compartiendo juntos.'
    );
