data class Picture(
    val id: Int,
    val author: String,
    val url: String
)
private var nextId = 1

fun generateSamplePictures(): List<Picture> {
    return listOf(
        Picture(1, "Всеволод", "https://i.pravatar.cc/304"),
        Picture(2, "Андрей", "https://i.pravatar.cc/300"),
        Picture(3, "Никита", "https://i.pravatar.cc/309"),
        Picture(4, "Ольга", "https://i.pravatar.cc/301"),
        Picture(5, "Владислав", "https://i.pravatar.cc/311")
    )
}

fun createNewPicture(): Picture {
    val newAuthor = listOf("Елизавета", "Константин", "Екатерина").random()
    val newPicture = Picture(
        id = nextId,
        author = newAuthor,
        url = "https://i.pravatar.cc/${nextId}"
    )
    nextId++
    return newPicture
}