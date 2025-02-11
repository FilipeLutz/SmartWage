package com.finalproject.smartwage.utils

import com.finalproject.smartwage.data.local.entities.User as LocalUser
import com.finalproject.smartwage.data.model.User as RemoteUser

object UserMapper {
    fun remoteToLocal(remoteUser: RemoteUser): LocalUser {
        return LocalUser(
            id = remoteUser.id,
            name = remoteUser.name,
            email = remoteUser.email,
            phoneNumber = remoteUser.phoneNumber,
            taxCredit = remoteUser.taxCredit,
            profilePicture = remoteUser.profilePicture
        )
    }

    fun localToRemote(localUser: LocalUser): RemoteUser {
        return RemoteUser(
            id = localUser.id,
            name = localUser.name,
            email = localUser.email,
            phoneNumber = localUser.phoneNumber,
            taxCredit = localUser.taxCredit,
            profilePicture = localUser.profilePicture
        )
    }
}