# Adding an new slime

1. Add a SlimeData JSON
1. Add an `Entity` (Optional)
1. Add a `Renderer`
1. Add to `ModEntityTypes`
1. Add to `ModEntityTypes::registerEntityAttributes`
1. Add Entity to `ModEntitySpawns::init`
1. Add Renderer to `ClientEventBusSubscriber`
1. Add Loottable to `ModEntityLootTables`
1. Add English translation (if not like title) in the `ModLanguageProvider`
1. Add unique `Texture`