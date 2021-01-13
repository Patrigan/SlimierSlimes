# Adding an new slime

1. Add an `Entity`
2. Add a `Renderer`
3. Add to `ModEntityTypes`
4. Add to `ModEntityTypes::registerEntityAttributes`
5. Add Entity to `ModEntitySpawns::init`
6. Add exception to `ModEntitySpawns::biomeLoading` (if necessary)
7. Add Renderer to `ClientEventBusSubscriber`
8. Add Loottable to `ModEntityLootTables`
9. Add English translation (if not like title) in the `ModLanguageProvider`
10. Add unique `Texture`