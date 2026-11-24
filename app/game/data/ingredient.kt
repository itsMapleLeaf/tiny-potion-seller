package game.data


enum class Ingredient(override val displayName: String, val magimins: MagiminCounts) : InventoryItem {
    Feyberry("Feyberry", magiminCounts(a = 6)),
    MandrakeRoot("Mandrake Root", magiminCounts(b = 6)),
    SackOfSlime("Sack of Slime", magiminCounts(c = 6)),
}
