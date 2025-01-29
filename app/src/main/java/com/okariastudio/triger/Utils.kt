package com.okariastudio.triger

import com.okariastudio.triger.data.model.SortOption

class Utils {

    companion object {
        fun changeSortOption(currentOption: SortOption, newOption: SortOption): SortOption {
            return if(isSameOptions(currentOption, newOption)){
                when(currentOption){
                    SortOption.BRETON_AZ -> SortOption.BRETON_ZA
                    SortOption.BRETON_ZA -> SortOption.BRETON_AZ
                    SortOption.FRANCAIS_AZ -> SortOption.FRANCAIS_ZA
                    SortOption.FRANCAIS_ZA -> SortOption.FRANCAIS_AZ
                    SortOption.DATE_NEWEST -> SortOption.DATE_OLDEST
                    SortOption.DATE_OLDEST -> SortOption.DATE_NEWEST
                    SortOption.LEVEL_ASC -> SortOption.LEVEL_DESC
                    SortOption.LEVEL_DESC -> SortOption.LEVEL_ASC
                    SortOption.BRETON -> SortOption.BRETON_AZ
                    SortOption.FRANCAIS -> SortOption.FRANCAIS_AZ
                    SortOption.DATE -> SortOption.DATE_NEWEST
                    SortOption.LEVEL -> SortOption.LEVEL_ASC
                }
            } else {
                return when(newOption){
                    SortOption.BRETON -> SortOption.BRETON_AZ
                    SortOption.FRANCAIS -> SortOption.FRANCAIS_AZ
                    SortOption.DATE -> SortOption.DATE_NEWEST
                    SortOption.LEVEL -> SortOption.LEVEL_ASC
                    else -> newOption
                }
            }
        }

        private fun isSameOptions(option1: SortOption, option2: SortOption): Boolean {
            return when(option1){
                SortOption.BRETON_AZ -> option2 == SortOption.BRETON_ZA || option2 == SortOption.BRETON
                SortOption.BRETON_ZA -> option2 == SortOption.BRETON_AZ || option2 == SortOption.BRETON
                SortOption.FRANCAIS_AZ -> option2 == SortOption.FRANCAIS_ZA || option2 == SortOption.FRANCAIS
                SortOption.FRANCAIS_ZA -> option2 == SortOption.FRANCAIS_AZ || option2 == SortOption.FRANCAIS
                SortOption.DATE_NEWEST -> option2 == SortOption.DATE_OLDEST || option2 == SortOption.DATE
                SortOption.DATE_OLDEST -> option2 == SortOption.DATE_NEWEST || option2 == SortOption.DATE
                SortOption.LEVEL_ASC -> option2 == SortOption.LEVEL_DESC || option2 == SortOption.LEVEL
                SortOption.LEVEL_DESC -> option2 == SortOption.LEVEL_ASC || option2 == SortOption.LEVEL
                else -> option1 == option2
            }
        }
    }
}