fun main() {

    fun readDirectoryContent(input: List<String>): FSDir {
        return input.fold(listOf<FSDir>()) { stack, line ->
            when {
                line.isLeaveCommand -> stack.dropLast(1)
                line.isChangeDir -> {
                    val newDir = line.toDir()
                    stack.lastOrNull()?.addItem(newDir)
                    stack + newDir
                }
                line.isFile -> {
                    stack.last().addItem(line.toFile())
                    stack
                }
                else -> stack
            }
        }.first()
    }

    fun part1(input: List<String>): Int {
        val mainDir = readDirectoryContent(input)
        mainDir.print()
        return mainDir.allDirs.map { it.calculatedSize() }.filter { it <= 100_000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val mainDir = readDirectoryContent(input)
        val spaceUsed = mainDir.calculatedSize()
        val spaceFree = 70_000_000 - spaceUsed
        val spaceToBeFreed = 30_000_000 - spaceFree
        return mainDir.allDirs.map { it.calculatedSize() }.filter { it >= spaceToBeFreed }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    println(part1(testInput))
    check(part1(testInput) == 95_437)
    check(part2(testInput) == 24_933_642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private val String.isChangeDir: Boolean
    get() = startsWith("$ cd ")

private val String.isFile: Boolean
    get() = this[0].isDigit()

private val String.isLeaveCommand: Boolean
    get() = this == "$ cd .."

private fun String.toFile(): FSFile {
    val (fileSize, name) = split(" ")
    return FSFile(fileSize.toInt(), name)
}

private fun String.toDir(): FSDir {
    val dirName = this.substringAfter("$ cd ").trim()
    return FSDir(dirName)
}

private interface FSItem {
    fun calculatedSize(): Int
    fun print(indent: Int = 0)
}

private data class FSFile(val size: Int, val name: String) : FSItem {
    override fun calculatedSize() = size

    override fun print(indent: Int) {
        println("${" ".repeat(indent)} file $name $size")
    }
}

private data class FSDir(val name: String) : FSItem {
    private val items: MutableList<FSItem> = mutableListOf()

    override fun calculatedSize(): Int = items.sumOf { it.calculatedSize() }

    override fun print(indent: Int) {
        println("${" ".repeat(indent)} dir $name")
        items.forEach {
            it.print(indent + 2)
        }
    }

    fun addItem(item: FSItem) = items.add(item)

    val allDirs: List<FSDir>
        get() = items.mapNotNull {
            it as? FSDir
        }.flatMap { listOf(it) + it.allDirs }
}
