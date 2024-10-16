package ru.skypaws.mobileapp.data.datasource.local.user

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import ru.skypaws.data.UserOuterClass.User
import java.io.InputStream
import java.io.OutputStream


object UserSerializer : Serializer<User> {
    override val defaultValue: User = User.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return User.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.userProtoDataStore: DataStore<User> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler { User.getDefaultInstance() }
)