package game.shared

import godot.api.FileAccess
import godot.core.Error

data class FileAccessException(val error: Error) : Exception(error.name)

fun FileAccess?.checkFileAccessError(): FileAccess {
    if (this == null) {
        throw FileAccessException(FileAccess.getOpenError())
    }
    return this
}

fun FileAccess?.use(block: (FileAccess) -> Unit) {
    checkFileAccessError().apply {
        block(this)
        close()
    }
}

fun <T> T.checkFileAccessError(): T {
    val error = FileAccess.getOpenError()
    if (error != Error.OK) {
        throw FileAccessException(error)
    }
    return this
}


fun getFileAsStringOrThrow(path: String): String {
    return FileAccess.getFileAsString(path).checkFileAccessError()
}

fun copyFile(source: String, destination: String) {
    val destinationFile = FileAccess.open(destination, FileAccess.ModeFlags.WRITE).checkFileAccessError()
    val sourceContent = try {
        FileAccess.getFileAsString(source).checkFileAccessError()
    } finally {
        destinationFile.close()
    }
    destinationFile.storeString(sourceContent).checkFileAccessError()
}
