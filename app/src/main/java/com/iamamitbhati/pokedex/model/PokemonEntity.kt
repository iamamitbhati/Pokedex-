/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iamamitbhati.pokedex.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iamamitbhati.pokedex.model.PokemonEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class PokemonEntity(
    var page: Int = 0,
    @PrimaryKey val name: String,
    val url: String,

    val favorite: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "pokemon_entity"
    }
}
