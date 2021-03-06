package no.ntnu.beardblaster.dbclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import no.ntnu.beardblaster.commons.spell.Element

@Entity(tableName = "element_table")
data class Element(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "element_id")
    val elementID : Int,
    @ColumnInfo(name = "element_name")
    val elementName : String
    )



