package com.ho8278.data.local

import com.ho8278.core.pref.DomainPreference
import com.ho8278.core.pref.Preference
import javax.inject.Inject

class FavoritePref @Inject constructor(
    preference: Preference
) : DomainPreference(preference, "favorite")